/**
 * 
 */
package me.hmasrafchi.leddisplay.rest;

import static java.lang.String.valueOf;

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

/**
 * @author michelin
 *
 */
@Path("matrices")
@Stateless
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

	@DELETE
	@Path("{matrixId}")
	public Response delete(@PathParam("matrixId") final int matrixId) {
		return matrixRepository.get(matrixId).map(matrixEntity -> {
			matrixRepository.delete(matrixId);
			return Response.noContent().build();
		}).orElse(Response.status(Response.Status.NOT_FOUND).build());
	}

	@GET
	@Path("{matrixId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("matrixId") final int matrixId) {
		return matrixRepository.get(matrixId).map(matrixEntity -> {
			// List<Scene> scenes = matrixEntity.getScenes();
			// Scene scene = scenes.get(0);
			// List<Overlay> overlays = scene.getOverlays();
			// Overlay overlay = overlays.get(0);
			return Response.ok(matrixEntity).build();
		}).orElse(Response.status(Response.Status.NOT_FOUND).build());
	}

	@Path("{matrixId}/scenes")
	public SceneResource getSceneResource() {
		return sceneResource;
	}

	@Path("{matrixId}/frames")
	public CompiledFramesResource getCompiledFramesResource() {
		return compiledFramesResource;
	}
}