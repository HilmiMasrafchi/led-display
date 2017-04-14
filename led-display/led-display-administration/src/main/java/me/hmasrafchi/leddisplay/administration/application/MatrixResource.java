/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application;

import static java.lang.String.valueOf;
import static java.util.Arrays.asList;

import java.util.function.Supplier;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
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

import me.hmasrafchi.leddisplay.administration.infrastructure.MatrixDoesntExistsException;
import me.hmasrafchi.leddisplay.administration.model.Matrix;
import me.hmasrafchi.leddisplay.administration.model.MatrixRepository;
import me.hmasrafchi.leddisplay.administration.model.Overlay;
import me.hmasrafchi.leddisplay.administration.model.SceneComposite;
import me.hmasrafchi.leddisplay.administration.model.SceneOverlayed;

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

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createMatrix(final Matrix matrix, @Context UriInfo uriInfo) {
		matrix.setScene(new SceneComposite());
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
			final Matrix matrix = matrixRepository.find(matrixId);
			return Response.ok(matrix).build();
		});
	}

	@POST
	@Path("{matrixId}/" + PathLiterals.SCENES)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response appendSceneToMatrix(@PathParam("matrixId") final int matrixId, final Overlay overlay,
			@Context UriInfo uriInfo) {
		final Matrix matrix = matrixRepository.find(matrixId);
		if (matrix != null) {
			final SceneComposite sceneComposite = (SceneComposite) matrix.getScene();
			final SceneOverlayed sceneOverlayed = new SceneOverlayed(asList(overlay));
			sceneComposite.getScenes().add(sceneOverlayed);
			return Response.noContent().build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	// TODO: refactor this
	private Response getResponseIfMatrixExists(final Object matrixId, final Supplier<Response> supplier) {
		try {
			return supplier.get();
		} catch (final MatrixDoesntExistsException mdee) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@Path("{matrixId}/" + PathLiterals.SCENES)
	public SceneResource getSceneResource() {
		return sceneResource;
	}
}