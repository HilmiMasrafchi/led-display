/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application;

import static java.lang.String.valueOf;

import java.util.Arrays;

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
	@Path("{sceneId}/{sceneType: (roll|stationary)}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response appendOverlayToScene(@PathParam("sceneId") final Integer sceneId, final Overlay overlay,
			@Context UriInfo uriInfo) {
		return sceneRepository.find(sceneId).map(sceneToBeUpdated -> {
			final SceneComposite sceneCompositeToBeUpdated = (SceneComposite) sceneToBeUpdated;
			final SceneOverlayed sceneOverlayed = new SceneOverlayed(Arrays.asList(overlay));
			sceneCompositeToBeUpdated.getScenes().add(sceneOverlayed);

			return Response.noContent().build();
		}).orElse(Response.status(Response.Status.NOT_FOUND).build());
	}

	@POST
	@Path("{sceneId}/overlays/{overlayId}/{sceneType: (roll|stationary)}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response appendOverlayToOverlayedScene(@PathParam("sceneId") final Integer sceneId,
			@PathParam("overlayId") final Integer overlayId, final Overlay overlay, @Context UriInfo uriInfo) {
		return sceneRepository.find(sceneId).map(sceneToBeUpdated -> {
			final SceneComposite sceneCompositeToBeUpdated = (SceneComposite) sceneToBeUpdated;
			return sceneCompositeToBeUpdated.getScenes().stream().filter(scene -> scene.getId().equals(overlayId))
					.findFirst().map(overlayedScene -> {
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
	public Response getCompiledFrames(@PathParam("sceneId") final Integer sceneId) {
		return sceneRepository.find(sceneId).map(scene -> Response.ok(scene.getCompiledFrames(10, 10)).build())
				.orElse(Response.status(Response.Status.NOT_FOUND).build());
	}
}