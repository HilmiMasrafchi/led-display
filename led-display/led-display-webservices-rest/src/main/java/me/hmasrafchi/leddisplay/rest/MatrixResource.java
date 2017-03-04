/**
 * 
 */
package me.hmasrafchi.leddisplay.rest;

import static java.lang.String.valueOf;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

import lombok.Data;
import me.hmasrafchi.leddisplay.api.CompiledFrames;
import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.api.Matrix;
import me.hmasrafchi.leddisplay.model.MatrixDefault;
import me.hmasrafchi.leddisplay.model.Overlay;
import me.hmasrafchi.leddisplay.model.SceneFactory;
import me.hmasrafchi.leddisplay.model.SceneOverlayed;
import me.hmasrafchi.leddisplay.rest.persist.LedStateRow;
import me.hmasrafchi.leddisplay.rest.persist.MatrixEntity;
import me.hmasrafchi.leddisplay.rest.persist.MatrixRepository;
import me.hmasrafchi.leddisplay.rest.persist.Scene;
import me.hmasrafchi.leddisplay.util.TwoDimensionalListRectangular;

/**
 * @author michelin
 *
 */
@Path("/matrix")
@Stateless
public class MatrixResource {
	@Inject
	private MatrixRepository matrixRepository;

	@Inject
	private SceneResource sceneResource;

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
	public Response getMatrix(@PathParam("matrixId") final Integer id) {
		return matrixRepository.get(id).map(matrixEntity -> Response.ok(matrixEntity).build())
				.orElse(Response.status(Response.Status.NOT_FOUND).build());
	}

	@GET
	@Path("{matrixId}/frames")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMatrixFrames(@PathParam("matrixId") final Integer id) {
		return matrixRepository.get(id).map(matrixEntity -> {
			final SceneFactory sceneFactory = new SceneFactory();
			final List<Scene> scenes = matrixEntity.getScenes();
			final List<me.hmasrafchi.leddisplay.api.Scene> modelScenes = new ArrayList<>();
			for (final Scene scene : scenes) {
				final List<Overlay> modelOverlays = new ArrayList<>();
				for (final me.hmasrafchi.leddisplay.rest.persist.Overlay overlay : scene.getOverlays()) {
					if (overlay instanceof me.hmasrafchi.leddisplay.rest.persist.OverlayStationary) {
						final me.hmasrafchi.leddisplay.rest.persist.OverlayStationary overlayStationary = (me.hmasrafchi.leddisplay.rest.persist.OverlayStationary) overlay;

						final List<LedStateRow> states2 = overlayStationary.getStates();
						final List<List<me.hmasrafchi.leddisplay.api.LedState>> modelStates = new ArrayList<>();
						for (final LedStateRow ledStateRow : states2) {
							final List<me.hmasrafchi.leddisplay.api.LedState> row = ledStateRow.getLedStates().stream()
									.map(ledState -> me.hmasrafchi.leddisplay.api.LedState.valueOf(ledState.name()))
									.collect(Collectors.toList());
							modelStates.add(row);
						}

						TwoDimensionalListRectangular<me.hmasrafchi.leddisplay.api.LedState> states = new TwoDimensionalListRectangular<>(
								modelStates);
						me.hmasrafchi.leddisplay.rest.persist.LedRgbColor onColor2 = overlayStationary.getOnColor();
						me.hmasrafchi.leddisplay.api.LedRgbColor onColor = new me.hmasrafchi.leddisplay.api.LedRgbColor(
								onColor2.getR(), onColor2.getG(), onColor2.getB());
						me.hmasrafchi.leddisplay.rest.persist.LedRgbColor offColor2 = overlayStationary.getOffColor();
						me.hmasrafchi.leddisplay.api.LedRgbColor offColor = new me.hmasrafchi.leddisplay.api.LedRgbColor(
								offColor2.getR(), offColor2.getG(), offColor2.getB());
						int duration = overlayStationary.getDuration();
						final Overlay overlayStationary2 = sceneFactory.getOverlayStationary(modelStates, onColor,
								offColor, duration);
						modelOverlays.add(overlayStationary2);
					}

					if (overlay instanceof me.hmasrafchi.leddisplay.rest.persist.OverlayRollHorizontally) {
						final me.hmasrafchi.leddisplay.rest.persist.OverlayRollHorizontally overlayRollHorizontally = (me.hmasrafchi.leddisplay.rest.persist.OverlayRollHorizontally) overlay;
						final List<LedStateRow> states2 = overlayRollHorizontally.getStates();
						final List<List<me.hmasrafchi.leddisplay.api.LedState>> modelStates = new ArrayList<>();
						for (final LedStateRow ledStateRow : states2) {
							final List<me.hmasrafchi.leddisplay.api.LedState> row = ledStateRow.getLedStates().stream()
									.map(ledState -> me.hmasrafchi.leddisplay.api.LedState.valueOf(ledState.name()))
									.collect(Collectors.toList());
							modelStates.add(row);
						}

						me.hmasrafchi.leddisplay.rest.persist.LedRgbColor onColor2 = overlayRollHorizontally
								.getOnColor();
						me.hmasrafchi.leddisplay.api.LedRgbColor onColor = new me.hmasrafchi.leddisplay.api.LedRgbColor(
								onColor2.getR(), onColor2.getG(), onColor2.getB());
						me.hmasrafchi.leddisplay.rest.persist.LedRgbColor offColor2 = overlayRollHorizontally
								.getOffColor();
						me.hmasrafchi.leddisplay.api.LedRgbColor offColor = new me.hmasrafchi.leddisplay.api.LedRgbColor(
								offColor2.getR(), offColor2.getG(), offColor2.getB());
						int beginIndexMark = overlayRollHorizontally.getBeginIndexMark();
						int yposition = overlayRollHorizontally.getYposition();

						me.hmasrafchi.leddisplay.model.Overlay overlayRollHorizontally2 = sceneFactory
								.getOverlayRollHorizontally(modelStates, onColor, offColor, beginIndexMark, yposition);
						modelOverlays.add(overlayRollHorizontally2);
					}
				}
				modelScenes.add(new SceneOverlayed(modelOverlays));
			}

			final Matrix matrix = new MatrixDefault();
			final me.hmasrafchi.leddisplay.api.Scene sceneComposited = sceneFactory.getCompositeScene(modelScenes);
			final CompiledFrames compile = matrix.compile(sceneComposited, matrixEntity.getRowCount(),
					matrixEntity.getColumnCount());
			final List<List<List<Led>>> collect = compile.getCompiledFramesData().stream()
					.map(frame -> frame.getFrameData()).collect(Collectors.toList());
			return Response.ok(new MyClass(matrixEntity.getId(), matrixEntity.getRowCount(),
					matrixEntity.getColumnCount(), collect)).build();
		}).orElse(Response.status(Response.Status.NOT_FOUND).build());
	}

	@Data
	static class MyClass {
		Integer id;
		Integer rowCount;
		Integer columnCount;
		List<List<List<Led>>> frames;

		public MyClass(Integer id, Integer rowCount, Integer columnCount, List<List<List<Led>>> frames) {
			this.id = id;
			this.rowCount = rowCount;
			this.columnCount = columnCount;
			this.frames = frames;
		}
	}

	@Path("{matrixId}/scenes")
	public SceneResource getSceneResource() {
		return sceneResource;
	}
}