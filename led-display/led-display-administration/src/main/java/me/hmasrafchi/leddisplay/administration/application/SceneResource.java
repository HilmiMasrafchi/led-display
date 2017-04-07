/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application;

import static java.lang.String.valueOf;
import static java.util.Arrays.asList;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import me.hmasrafchi.leddisplay.administration.CompiledFrames;
import me.hmasrafchi.leddisplay.administration.model.Overlay;
import me.hmasrafchi.leddisplay.administration.model.Scene;
import me.hmasrafchi.leddisplay.administration.model.SceneComposite;
import me.hmasrafchi.leddisplay.administration.model.SceneOverlayed;
import me.hmasrafchi.leddisplay.administration.model.SceneRepository;

/**
 * @author michelin
 *
 */
@Stateless
@Path(PathLiterals.SCENES)
public class SceneResource {
	@Inject
	private SceneRepository sceneRepository;

	@POST
	public Response createScene(@Context final UriInfo uriInfo) {
		final Scene scene = new SceneComposite();
		sceneRepository.add(scene);

		final UriBuilder builder = uriInfo.getAbsolutePathBuilder();
		builder.path(valueOf(scene.getId()));
		return Response.created(builder.build()).build();
	}

	@POST
	@Path("{sceneId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response appendOverlayToScene(@PathParam("sceneId") final Integer sceneId, final Overlay overlay,
			@Context UriInfo uriInfo) {
		return sceneRepository.find(sceneId).map(sceneToBeUpdated -> {
			final SceneComposite sceneCompositeToBeUpdated = (SceneComposite) sceneToBeUpdated;
			final SceneOverlayed sceneOverlayed = new SceneOverlayed(asList(overlay));
			sceneCompositeToBeUpdated.getScenes().add(sceneOverlayed);

			return Response.noContent().build();
		}).orElse(Response.status(Response.Status.NOT_FOUND).build());
	}

	@POST
	@Path("{sceneId}/overlays/{overlayId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response appendOverlayToOverlayedScene(@PathParam("sceneId") final Integer sceneId,
			@PathParam("overlayId") final Integer overlayId, final Overlay overlay, @Context UriInfo uriInfo) {
		return sceneRepository.find(sceneId).map(sceneToBeUpdated -> {
			final SceneComposite sceneCompositeToBeUpdated = (SceneComposite) sceneToBeUpdated;
			return sceneCompositeToBeUpdated.getScenes().stream().filter(scene -> scene.getId().equals(overlayId))
					.findFirst().map(overlayedScene -> {
						// TODO: instead using scene overlayed use scene
						// composite ?!!?!?!
						final SceneOverlayed overlayedSceneActual = (SceneOverlayed) overlayedScene;
						overlayedSceneActual.getOverlays().add(overlay);
						return Response.noContent().build();
					}).orElse(Response.status(Response.Status.NOT_FOUND).build());
		}).orElse(Response.status(Response.Status.NOT_FOUND).build());

	}

	@GET
	@Path("{sceneId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getScene(@PathParam("sceneId") final Integer sceneId) {
		return sceneRepository.find(sceneId).map(scene -> Response.ok(scene).build())
				.orElse(Response.status(Response.Status.NOT_FOUND).build());
	}

	@GET
	@Path("{sceneId}/compiled_frames")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCompiledFrames(@PathParam("sceneId") final Integer sceneId,
			@QueryParam("rowCount") final int rowCount, @QueryParam("columnCount") final int columnCount) {
		return sceneRepository.find(sceneId).map(scene -> {
			System.out.println(scene);
			final CompiledFrames compiledFrames = scene.getCompiledFrames(rowCount, columnCount);
			if (compiledFrames == null) {
				return Response.status(Status.NOT_FOUND).build();
			}
			return Response.ok(compiledFrames).build();
		}).orElse(Response.status(Response.Status.NOT_FOUND).build());
	}

	@GET
	@Path("{sceneId}/test")
	public Response test2(@PathParam("sceneId") final Integer sceneId) {
		return sceneRepository.find(sceneId).map(scene -> {
			// System.out.println("hello");
			// final SceneComposite sceneComposite = (SceneComposite) scene;
			// final Set<Scene> scenes = sceneComposite.getScenes();
			// if (scenes.isEmpty()) {
			// scenes.add(new SceneOverlayed());
			// }
			//
			// final List<LedStateRow> overlayStationaryStates = asList( //
			// new LedStateRow(asList(ON, ON, ON, ON, ON)), //
			// new LedStateRow(asList(TRANSPARENT, TRANSPARENT, TRANSPARENT,
			// TRANSPARENT, TRANSPARENT)), //
			// new LedStateRow(asList(ON, TRANSPARENT, TRANSPARENT, TRANSPARENT,
			// ON)), //
			// new LedStateRow(asList(OFF, TRANSPARENT, TRANSPARENT,
			// TRANSPARENT, OFF)), //
			// new LedStateRow(asList(ON, TRANSPARENT, TRANSPARENT, TRANSPARENT,
			// ON)), //
			// new LedStateRow(asList(ON, ON, ON, ON, ON)));
			// final OverlayStationary overlayStationaryToPost = new
			// OverlayStationary(overlayStationaryStates,
			// RgbColor.RED, RgbColor.YELLOW, 1);
			//
			// final Scene sceneOverlayed = scenes.get(0);
			// final SceneOverlayed sceneOverlayedCasted = (SceneOverlayed)
			// sceneOverlayed;
			// if (sceneOverlayedCasted.getOverlays() == null) {
			// sceneOverlayedCasted.setOverlays(new ArrayList<>());
			// }
			//
			// final List<Overlay> overlays =
			// sceneOverlayedCasted.getOverlays();
			// overlays.add(overlayStationaryToPost);

			return Response.status(Status.OK).build();
		}).orElse(Response.status(Status.NOT_FOUND).build());
	}
}