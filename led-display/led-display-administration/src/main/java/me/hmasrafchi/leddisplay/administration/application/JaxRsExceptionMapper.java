/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

import javax.ejb.EJBException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import me.hmasrafchi.leddisplay.administration.model.domain.DomainContraintViolationException;

/**
 * @author michelin
 *
 */
@Provider
public class JaxRsExceptionMapper implements ExceptionMapper<EJBException> {
	@Override
	public Response toResponse(final EJBException exception) {
		final Throwable cause1 = exception.getCause();
		if (cause1 != null && cause1 instanceof DomainContraintViolationException) {
			final String errorMessage = cause1.getMessage();
			return Response.status(BAD_REQUEST).entity(errorMessage).type(MediaType.APPLICATION_JSON).build();
		}
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	}
}