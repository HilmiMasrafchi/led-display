/**
 * 
 */
package me.hmasrafchi.leddisplay.rest;

import static java.lang.String.valueOf;

import java.net.URI;
import java.util.function.Supplier;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import me.hmasrafchi.leddisplay.rest.persist.MatrixRepository;
import me.hmasrafchi.leddisplay.rest.persist.Overlay;
import me.hmasrafchi.leddisplay.rest.persist.OverlayRollHorizontally;
import me.hmasrafchi.leddisplay.rest.persist.OverlayStationary;
import me.hmasrafchi.leddisplay.rest.persist.Scene;
import me.hmasrafchi.leddisplay.rest.persist.inmem.MatrixDoesntExistsException;
import me.hmasrafchi.leddisplay.rest.persist.inmem.OverlayDoesntExistsException;
import me.hmasrafchi.leddisplay.rest.persist.inmem.SceneDoesntExistsException;

/**
 * @author michelin
 *
 */
@Stateless
public class SceneResource {
	@Inject
	private MatrixRepository matrixRepository;

	public SceneResource() {
	}

	@POST
	public Response createScene(@PathParam("matrixId") final int matrixId, @Context final UriInfo uriInfo) {
		final Scene addedScene = matrixRepository.createNewScene(matrixId);
		final UriBuilder builder = uriInfo.getAbsolutePathBuilder();
		builder.path(valueOf(addedScene.getId()));

		return Response.created(builder.build()).build();
	}

	@POST
	@Path("{sceneId}/" + PathLiterals.OVERLAY_STATIONARY)
	public Response appendOverlayStationaryToScene(@PathParam("matrixId") final int matrixId,
			@PathParam("sceneId") final int sceneId, final OverlayStationary overlayStationary,
			@Context final UriInfo uriInfo) {
		return getResponseIfSceneAndMatrixExists(() -> {
			final Overlay appendedOverlay = matrixRepository.appendOverlay(matrixId, sceneId, overlayStationary);

			final UriBuilder builder = uriInfo.getAbsolutePathBuilder();
			final String absolutePath = builder.build().toString();
			final String absolutePathWithoutLastSegment = absolutePath.substring(0, absolutePath.lastIndexOf("/"));
			final UriBuilder newUriBuilder = UriBuilder.fromUri(String.format("%s/%s/%s",
					absolutePathWithoutLastSegment, PathLiterals.OVERLAYS, appendedOverlay.getId()));
			final URI overlayLocation = newUriBuilder.build();

			return Response.created(overlayLocation).build();
		});
	}

	private Response getResponseIfSceneAndMatrixExists(final Supplier<Response> supplier) {
		try {
			return supplier.get();
		} catch (final MatrixDoesntExistsException | SceneDoesntExistsException | OverlayDoesntExistsException e) {
			System.out.println(e);
			return Response.status(Response.Status.NOT_FOUND).build();
		} catch (final Exception e) {
			System.out.println(e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@POST
	@Path("{sceneId}/" + PathLiterals.OVERLAY_ROLL_HORIZONTALLY)
	public Response appendOverlayRollHorizontallyToScene(@PathParam("matrixId") final int matrixId,
			@PathParam("sceneId") final int sceneId, final OverlayRollHorizontally overlayRollHorizontally,
			@Context final UriInfo uriInfo) {
		return getResponseIfSceneAndMatrixExists(() -> {
			final Overlay appendedOverlay = matrixRepository.appendOverlay(matrixId, sceneId, overlayRollHorizontally);

			final UriBuilder builder = uriInfo.getAbsolutePathBuilder();
			final String absolutePath = builder.build().toString();
			final String absolutePathWithoutLastSegment = absolutePath.substring(0, absolutePath.lastIndexOf("/"));
			final UriBuilder newUriBuilder = UriBuilder.fromUri(String.format("%s/%s/%s",
					absolutePathWithoutLastSegment, PathLiterals.OVERLAYS, appendedOverlay.getId()));
			final URI overlayLocation = newUriBuilder.build();

			return Response.created(overlayLocation).build();
		});
	}

	@PUT
	@Path("{sceneId}/" + PathLiterals.OVERLAYS + "/{overlayId}")
	public Response updateOverlayStationaryToScene(@PathParam("matrixId") final int matrixId,
			@PathParam("sceneId") final int sceneId, @PathParam("overlayId") final int overlayId,
			final Overlay newOverlay, @Context UriInfo uriInfo) {
		return getResponseIfSceneAndMatrixExists(() -> {
			matrixRepository.updateOverlay(matrixId, sceneId, overlayId, newOverlay);
			return Response.ok().build();
		});
	}

	@DELETE
	@Path("{sceneId}")
	public Response deleteScene(@PathParam("matrixId") final int matrixId, @PathParam("sceneId") final int sceneId) {
		return getResponseIfSceneAndMatrixExists(() -> {
			matrixRepository.deleteScene(matrixId, sceneId);
			return Response.noContent().build();
		});
	}

	@DELETE
	@Path("{sceneId}/" + PathLiterals.OVERLAYS + "/{overlayId}")
	public Response deleteOverlay(@PathParam("matrixId") final int matrixId, @PathParam("sceneId") final int sceneId,
			@PathParam("overlayId") final int overlayId) {
		return getResponseIfSceneAndMatrixExists(() -> {
			matrixRepository.deleteOverlay(matrixId, sceneId, overlayId);
			return Response.noContent().build();
		});
	}
}