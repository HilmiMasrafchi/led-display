/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application;

import static java.lang.String.valueOf;

import java.net.URI;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.ws.rs.Consumes;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import me.hmasrafchi.leddisplay.administration.infrastructure.MatrixRepository;
import me.hmasrafchi.leddisplay.administration.model.view.CreateMatrixCommand;
import me.hmasrafchi.leddisplay.administration.model.view.MatrixView;
import me.hmasrafchi.leddisplay.domain.event.LedView;
import me.hmasrafchi.leddisplay.domain.event.MatrixUpdatedEvent;

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
		final MatrixView matrixView = new MatrixView(createMatrixCommand.getRowCount(),
				createMatrixCommand.getColumnCount(), createMatrixCommand.getScenes());

		final MatrixView matrixViewCreated = matrixRepository.create(matrixView);

		final List<List<List<LedView>>> compiledFrames = matrixViewCreated.getCompiledFrames();

		if (compiledFrames != null && !compiledFrames.isEmpty()) {
			try {
				final ObjectMapper objectMapper = new ObjectMapper();

				final MatrixUpdatedEvent matrixUpdatedEvent = new MatrixUpdatedEvent(matrixViewCreated.getId(),
						compiledFrames);

				final String compiledFramesJson = objectMapper.writeValueAsString(matrixUpdatedEvent);
				jms.createProducer().send(outgoingQueue, compiledFramesJson);
			} catch (final Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

		final Integer matrixId = matrixViewCreated.getId();
		final UriBuilder builder = uriInfo.getAbsolutePathBuilder();
		final URI createdMatrixLocationURI = builder.path(valueOf(matrixId)).build();

		return Response.created(createdMatrixLocationURI).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateMatrix(final MatrixView matrix) {
		matrixRepository.update(matrix);
		return Response.noContent().build();
	}

	@GET
	@Path("{matrixId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("matrixId") final int matrixId) {
		return matrixRepository.find(matrixId).map(matrix -> Response.ok(matrix).build())
				.orElse(Response.status(Status.NOT_FOUND).build());
	}
}