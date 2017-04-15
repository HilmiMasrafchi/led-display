/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ejb.EJBTransactionRolledbackException;
import javax.persistence.PersistenceException;
import javax.transaction.RollbackException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

/**
 * @author michelin
 *
 */
@Provider
public class JaxRsMapper implements ExceptionMapper<EJBTransactionRolledbackException> {
	@Override
	@Produces(MediaType.APPLICATION_JSON)
	public Response toResponse(final EJBTransactionRolledbackException exception) {
		final Throwable cause1 = exception.getCause();
		if (cause1 != null && cause1 instanceof RollbackException) {
			final Throwable cause2 = cause1.getCause();
			if (cause2 != null && cause2 instanceof PersistenceException) {
				final Throwable cause3 = cause2.getCause();
				if (cause3 != null && cause3 instanceof ConstraintViolationException) {
					final ConstraintViolationException e = (ConstraintViolationException) cause3;
					final Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
					final List<String> messages = new ArrayList<>();
					for (final ConstraintViolation<?> violation : violations) {
						messages.add(violation.getMessage());
					}

					final ErrorMessage errorMessage = new ErrorMessage(messages);
					return Response.status(Status.BAD_REQUEST).entity(errorMessage).type(MediaType.APPLICATION_JSON)
							.build();
				}
			}
		}
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	}

	@Getter
	private final class ErrorMessage {
		private final List<String> messages;

		@JsonCreator
		public ErrorMessage(@JsonProperty("messages") final List<String> messages) {
			this.messages = messages;
		}
	}
}