/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.hmasrafchi.leddisplay.administration.CompiledFrames;
import me.hmasrafchi.leddisplay.administration.Frame;
import me.hmasrafchi.leddisplay.administration.model.Led;
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
@Path(PathLiterals.SCENES)
public class SceneResource {
	@Inject
	private MatrixRepository matrixRepository;

	@Inject
	private JMSContext jmsContext;

	@Resource(mappedName = "java:jboss/exported/jms/queue/test")
	private Queue queue;

	@POST
	@Path("{sceneId}/overlays/{overlayId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response appendOverlayToOverlayedScene(@PathParam("matrixId") final int matrixId,
			@PathParam("sceneId") final int compositeSceneId, @PathParam("overlayId") final int overlayedSceneId,
			final Overlay overlay, @Context final UriInfo uriInfo) {
		final Matrix matrix = matrixRepository.find(matrixId);
		if (matrix != null) {
			final SceneComposite sceneComposite = (SceneComposite) matrix.getScene();
			return sceneComposite.getScenes().stream().filter(scene -> scene.getId().equals(overlayedSceneId))
					.findFirst().map(overlayedScene -> {
						final SceneOverlayed overlayedSceneActual = (SceneOverlayed) overlayedScene;
						overlayedSceneActual.getOverlays().add(overlay);
						sendMatrixChangedEvent(matrixId);
						return Response.noContent().build();
					}).orElse(Response.status(Response.Status.NOT_FOUND).build());
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	private void sendMatrixChangedEvent(final int matrixId) {
		final Matrix matrix = matrixRepository.find(matrixId);
		final CompiledFrames compiledFrames = matrix.getCompiledFrames();

		final List<List<List<Led>>> compiledFramesList = new ArrayList<>();
		final List<Frame> compiledFramesData = compiledFrames.getCompiledFramesData();
		for (final Frame frame : compiledFramesData) {
			compiledFramesList.add(frame.getFrameData());
		}

		final MatrixUpdatedEvent event = new MatrixUpdatedEvent(matrixId, compiledFramesList);
		final ObjectMapper objectMapper = new ObjectMapper();
		try {
			final String writeValueAsString = objectMapper.writeValueAsString(event);
			jmsContext.createProducer().send(queue, writeValueAsString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}