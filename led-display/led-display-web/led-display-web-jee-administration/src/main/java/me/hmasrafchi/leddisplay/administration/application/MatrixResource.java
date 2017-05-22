/**
* 
*/
package me.hmasrafchi.leddisplay.administration.application;

import static java.lang.String.valueOf;

import java.math.BigInteger;
import java.net.URI;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import me.hmasrafchi.leddisplay.administration.infrastructure.MatrixRepository;
import me.hmasrafchi.leddisplay.model.event.MatrixUpdatedEvent;
import me.hmasrafchi.leddisplay.model.view.CreateMatrixCommand;
import me.hmasrafchi.leddisplay.model.view.LedView;
import me.hmasrafchi.leddisplay.model.view.MatrixView;

/**
 * @author michelin
 *
 */
@Stateless
@Path(PathLiterals.MATRICES)
public class MatrixResource {
	@Inject
	private MatrixRepository matrixRepository;

	@Resource(name = "java:jboss/exported/jms/queue/test")
	private Queue outgoingQueue;

	@Inject
	private JMSContext jms;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createMatrix(final CreateMatrixCommand createMatrixCommand, @Context final UriInfo uriInfo) {
		final MatrixView matrixView = new MatrixView(createMatrixCommand.getName(), createMatrixCommand.getRowCount(),
				createMatrixCommand.getColumnCount(), createMatrixCommand.getScenes());

		final MatrixView matrixViewCreated = matrixRepository.create(matrixView);
		sendMatrixUpdatedEvent(matrixViewCreated);

		final BigInteger matrixId = matrixViewCreated.getId();
		final UriBuilder builder = uriInfo.getAbsolutePathBuilder();
		final URI createdMatrixLocationURI = builder.path(valueOf(matrixId)).build();

		return Response.created(createdMatrixLocationURI).build();
	}

	private void sendMatrixUpdatedEvent(final MatrixView matrixView) {
		final List<List<List<LedView>>> compiledFrames = matrixView.getCompiledFrames();

		if (compiledFrames != null && !compiledFrames.isEmpty()) {
			try {
				final BigInteger id = matrixView.getId();
				final int rowCount = matrixView.getRowCount();
				final int columnCount = matrixView.getColumnCount();
				final MatrixUpdatedEvent matrixUpdatedEvent = new MatrixUpdatedEvent(id, rowCount, columnCount,
						compiledFrames);

				final ObjectMessage createObjectMessage = jms.createObjectMessage();
				createObjectMessage.setObject(matrixUpdatedEvent);
				jms.createProducer().send(outgoingQueue, createObjectMessage);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateMatrix(final MatrixView matrix) {
		final MatrixView matrixUpdated = matrixRepository.update(matrix);
		sendMatrixUpdatedEvent(matrixUpdated);

		return Response.noContent().build();
	}

	@GET
	@Path("{matrixId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("matrixId") final BigInteger matrixId) {
		return matrixRepository.find(matrixId).map(matrix -> Response.ok(matrix).build())
				.orElse(Response.status(Status.NOT_FOUND).build());
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll() {
		final List<MatrixView> allMatrices = matrixRepository.findAll();
		return Response.ok(allMatrices).build();
	}

	@DELETE
	@Path("{matrixId}")
	public Response deleteMatrix(@PathParam("matrixId") final BigInteger matrixId) {
		if (matrixRepository.delete(matrixId)) {
			return Response.ok().build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
}