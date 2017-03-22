/**
 * 
 */
package me.hmasrafchi.leddisplay.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import me.hmasrafchi.leddisplay.rest.persist.MatrixRepository;
import me.hmasrafchi.leddisplay.rest.persist.Overlay;
import me.hmasrafchi.leddisplay.rest.persist.OverlayRollHorizontally;
import me.hmasrafchi.leddisplay.rest.persist.OverlayStationary;
import me.hmasrafchi.leddisplay.rest.persist.Scene;

/**
 * @author michelin
 *
 */
@Stateless
public class SceneResource {
	@Inject
	MatrixRepository matrixRepository;

	public SceneResource() {
	}

	@POST
	@Path("stationary")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response appendOverlayStationary(@PathParam("matrixId") final int matrixId,
			final OverlayStationary addOverlayStationary, @Context UriInfo uriInfo) {
		return appendOverlay(matrixId, addOverlayStationary, uriInfo);
	}

	@POST
	@Path("roll")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response appendOverlayRollHorizontally(@PathParam("matrixId") final int matrixId,
			final OverlayRollHorizontally addRollHorizontally, @Context UriInfo uriInfo) {
		return appendOverlay(matrixId, addRollHorizontally, uriInfo);
	}

	private Response appendOverlay(final int matrixId, final Overlay overlay, final UriInfo uriInfo) {
		return matrixRepository.get(matrixId).map(matrixEntity -> {
			final List<Scene> scenes = matrixEntity.getScenes();
			final Scene scene = new Scene();
			scene.getOverlays().add(overlay);
			scenes.add(scene);
			matrixRepository.update(matrixEntity);

			final UriBuilder builder = uriInfo.getAbsolutePathBuilder();
			// TODO: put the scene id
			return Response.created(builder.build()).build();
		}).orElse(Response.status(Response.Status.NOT_FOUND).build());
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{sceneIndex}/overlays/stationary")
	public Response appendOverlayStationaryToScene(@PathParam("matrixId") final int matrixId,
			@PathParam("sceneIndex") final int sceneIndex, final OverlayStationary addOverlayStationary,
			@Context UriInfo uriInfo) {
		return appendOverlayToScene(matrixId, sceneIndex, addOverlayStationary, uriInfo);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{sceneIndex}/overlays/roll")
	public Response appendOverlayRollHorizontallyToScene(@PathParam("matrixId") final int matrixId,
			@PathParam("sceneIndex") final int sceneIndex, final OverlayRollHorizontally addOverlayRollHorizontally,
			@Context UriInfo uriInfo) {
		return appendOverlayToScene(matrixId, sceneIndex, addOverlayRollHorizontally, uriInfo);
	}

	private Response appendOverlayToScene(final int matrixId, final int sceneIndex, final Overlay overlay,
			final UriInfo uriInfo) {
		return matrixRepository.get(matrixId).map(matrixEntity -> {
			final List<Scene> scenes = matrixEntity.getScenes();
			if (sceneIndex == scenes.size()) {
				return Response.status(Response.Status.NOT_FOUND).build();
			}

			scenes.get(sceneIndex).getOverlays().add(overlay);
			matrixRepository.update(matrixEntity);

			final UriBuilder builder = uriInfo.getAbsolutePathBuilder();
			return Response.created(builder.build()).build();
		}).orElse(Response.status(Response.Status.NOT_FOUND).build());
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{sceneIndex}/overlays/{overlayIndex}/stationary")
	public Response replaceWithOverlayStationaryAtScene(@PathParam("matrixId") final int matrixId,
			@PathParam("sceneIndex") final int sceneIndex, @PathParam("overlayIndex") final int overlayIndex,
			final OverlayStationary addOverlayStationary, @Context final UriInfo uriInfo) {
		return replaceWithOverlayAtScene(matrixId, sceneIndex, overlayIndex, addOverlayStationary, uriInfo);
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{sceneIndex}/overlays/{overlayIndex}/roll")
	public Response replaceWithOverlayRollHorizontallyAtScene(@PathParam("matrixId") final int matrixId,
			@PathParam("sceneIndex") final int sceneIndex, @PathParam("overlayIndex") final int overlayIndex,
			final OverlayRollHorizontally addOverlayRollHorizontally, @Context final UriInfo uriInfo) {
		return replaceWithOverlayAtScene(matrixId, sceneIndex, overlayIndex, addOverlayRollHorizontally, uriInfo);
	}

	public Response replaceWithOverlayAtScene(final int matrixId, final int sceneIndex, final int overlayIndex,
			final Overlay overlay, final UriInfo uriInfo) {
		return matrixRepository.get(matrixId).map(matrixEntity -> {
			final List<Scene> scenes = matrixEntity.getScenes();
			if (sceneIndex == scenes.size()) {
				return Response.status(Response.Status.NOT_FOUND).build();
			}

			final Scene scene = scenes.get(sceneIndex);
			final List<Overlay> overlays = scene.getOverlays();
			if (overlayIndex == overlays.size()) {
				return Response.status(Response.Status.NOT_FOUND).build();
			}

			overlays.set(overlayIndex, overlay);
			matrixRepository.update(matrixEntity);

			final UriBuilder builder = uriInfo.getAbsolutePathBuilder();
			return Response.created(builder.build()).build();
		}).orElse(Response.status(Response.Status.NOT_FOUND).build());
	}

	@DELETE
	@Path("{sceneIndex}")
	public Response removeScene(@PathParam("matrixId") final int matrixId,
			@PathParam("sceneIndex") final int sceneIndex) {
		return matrixRepository.get(matrixId).map(matrixEntity -> {
			final List<Scene> scenes = matrixEntity.getScenes();
			scenes.remove(sceneIndex);
			matrixRepository.update(matrixEntity);

			return Response.noContent().build();
		}).orElse(Response.status(Response.Status.NOT_FOUND).build());
	}

	@DELETE
	@Path("{sceneIndex}/overlays/{overlayIndex}")
	public Response removeOverlayAtScene(@PathParam("matrixId") final int matrixId,
			@PathParam("sceneIndex") final int sceneIndex, @PathParam("overlayIndex") final int overlayIndex) {
		return matrixRepository.get(matrixId).map(matrixEntity -> {
			final List<Scene> scenes = matrixEntity.getScenes();
			final Scene scene = scenes.get(sceneIndex);
			final List<Overlay> overlays = scene.getOverlays();
			overlays.remove(overlayIndex);

			matrixRepository.update(matrixEntity);

			return Response.noContent().build();
		}).orElse(Response.status(Response.Status.NOT_FOUND).build());
	}
}