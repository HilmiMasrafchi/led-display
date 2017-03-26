/**
 * 
 */
package me.hmasrafchi.leddisplay.rest;

import static java.lang.String.valueOf;

import java.util.function.Supplier;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import me.hmasrafchi.leddisplay.rest.persist.MatrixEntity;
import me.hmasrafchi.leddisplay.rest.persist.MatrixRepository;
import me.hmasrafchi.leddisplay.rest.persist.inmem.MatrixDoesntExistsException;

/**
 * @author michelin
 *
 */
@Stateless
@Path(PathLiterals.MATRICES)
public class MatrixResource {
	@Inject
	private MatrixRepository matrixRepository;

	@Inject
	private SceneResource sceneResource;
	@Inject
	private CompiledFramesResource compiledFramesResource;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(final MatrixEntity matrix, @Context UriInfo uriInfo) {
		matrixRepository.create(matrix);

		final UriBuilder builder = uriInfo.getAbsolutePathBuilder();
		builder.path(valueOf(matrix.getId()));
		return Response.created(builder.build()).build();
	}

	@GET
	@Path("{matrixId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("matrixId") final int matrixId) {
		return getResponseIfMatrixExists(matrixId, () -> {
			final MatrixEntity matrixEntity = matrixRepository.read(matrixId);
			return Response.ok(matrixEntity).build();
		});
	}

	private Response getResponseIfMatrixExists(final Object matrixId, final Supplier<Response> supplier) {
		try {
			return supplier.get();
		} catch (final MatrixDoesntExistsException mdee) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@DELETE
	@Path("{matrixId}")
	public Response delete(@PathParam("matrixId") final int matrixId) {
		return getResponseIfMatrixExists(matrixId, () -> {
			matrixRepository.delete(matrixId);
			return Response.noContent().build();
		});
	}

	@Path("{matrixId}/" + PathLiterals.SCENES)
	public SceneResource getSceneResource() {
		return sceneResource;
	}

	@Path("{matrixId}/" + PathLiterals.FRAMES)
	public CompiledFramesResource getCompiledFramesResource() {
		return compiledFramesResource;
	}
}