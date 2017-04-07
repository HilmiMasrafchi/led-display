/**
 * 
 */
package test.app;

import static java.lang.String.valueOf;

import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import test.model.OverlayStationary;
import test.model.Scene;
import test.model.SceneComposite;
import test.model.SceneOverlayed;
import test.model.SceneRepository;

/**
 * @author michelin
 *
 */
@Stateless
@Path("scenes")
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

	@GET
	@Path("{sceneId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getScene(@PathParam("sceneId") final Integer sceneId) {
		return sceneRepository.find(sceneId).map(scene -> Response.ok(scene).build())
				.orElse(Response.status(Response.Status.NOT_FOUND).build());
	}

	@GET
	@Path("{sceneId}/test")
	public Response test1(@PathParam("sceneId") final Integer sceneId) {
		return sceneRepository.find(sceneId).map(scene -> {
			final SceneComposite sceneComposite = (SceneComposite) scene;
			final Set<Scene> scenes = sceneComposite.getScenes();
			final SceneOverlayed sceneOverlayed = (SceneOverlayed) scenes.iterator().next();
			final Set<Scene> overlays = sceneOverlayed.getOverlays();

			final OverlayStationary overlayStationaryToPost = new OverlayStationary();
			overlays.add(overlayStationaryToPost);

			System.out.println(sceneComposite);

			return Response.status(Status.OK).build();
		}).orElse(Response.status(Status.NOT_FOUND).build());
	}

	@GET
	@Path("{sceneId}/test2")
	public Response test2(@PathParam("sceneId") final Integer sceneId) {
		return sceneRepository.find(sceneId).map(scene -> {
			// final SceneComposite sceneComposite = (SceneComposite) scene;
			// final List<Scene> scenes = sceneComposite.getScenes();
			// final SceneComposite sceneOverlayed = (SceneComposite)
			// scenes.get(0);
			// final List<Scene> overlays = sceneOverlayed.getScenes();
			//
			// final OverlayStationary overlayStationaryToPost = new
			// OverlayStationary();
			// overlays.add(overlayStationaryToPost);
			//
			// System.out.println(sceneComposite);

			return Response.status(Status.OK).build();
		}).orElse(Response.status(Status.NOT_FOUND).build());
	}
}