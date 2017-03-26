/**
 * 
 */
package me.hmasrafchi.leddisplay.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import lombok.Data;
import me.hmasrafchi.leddisplay.api.CompiledFrames;
import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.api.Matrix;
import me.hmasrafchi.leddisplay.model.Overlay;
import me.hmasrafchi.leddisplay.model.SceneFactory;
import me.hmasrafchi.leddisplay.rest.persist.LedStateRow;
import me.hmasrafchi.leddisplay.rest.persist.MatrixEntity;
import me.hmasrafchi.leddisplay.rest.persist.MatrixRepository;
import me.hmasrafchi.leddisplay.rest.persist.Scene;
import me.hmasrafchi.leddisplay.rest.persist.inmem.MatrixDoesntExistsException;

/**
 * @author michelin
 *
 */
@Stateless
public class CompiledFramesResource {
	@Inject
	private MatrixRepository matrixRepository;

	@Inject
	private Matrix matrix;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMatrixFrames(@PathParam("matrixId") final int matrixId) {
		try {
			final MatrixEntity matrixEntity = matrixRepository.read(matrixId);
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

						me.hmasrafchi.leddisplay.rest.persist.LedRgbColor onColor2 = overlayStationary.getOnColor();
						me.hmasrafchi.leddisplay.api.RgbColor onColor = new me.hmasrafchi.leddisplay.api.RgbColor(
								onColor2.getR(), onColor2.getG(), onColor2.getB());
						me.hmasrafchi.leddisplay.rest.persist.LedRgbColor offColor2 = overlayStationary.getOffColor();
						me.hmasrafchi.leddisplay.api.RgbColor offColor = new me.hmasrafchi.leddisplay.api.RgbColor(
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
						me.hmasrafchi.leddisplay.api.RgbColor onColor = new me.hmasrafchi.leddisplay.api.RgbColor(
								onColor2.getR(), onColor2.getG(), onColor2.getB());
						me.hmasrafchi.leddisplay.rest.persist.LedRgbColor offColor2 = overlayRollHorizontally
								.getOffColor();
						me.hmasrafchi.leddisplay.api.RgbColor offColor = new me.hmasrafchi.leddisplay.api.RgbColor(
								offColor2.getR(), offColor2.getG(), offColor2.getB());
						int beginIndexMark = overlayRollHorizontally.getBeginIndexMark();
						int yposition = overlayRollHorizontally.getYposition();

						me.hmasrafchi.leddisplay.model.Overlay overlayRollHorizontally2 = sceneFactory
								.getOverlayRollHorizontally(modelStates, onColor, offColor, beginIndexMark, yposition);
						modelOverlays.add(overlayRollHorizontally2);
					}
				}
				modelScenes.add(sceneFactory.getSceneOverlayed(modelOverlays));
			}

			final me.hmasrafchi.leddisplay.api.Scene sceneComposited = sceneFactory.getCompositeScene(modelScenes);
			final CompiledFrames compile = matrix.compile(sceneComposited, matrixEntity.getRowCount(),
					matrixEntity.getColumnCount());
			final List<List<List<Led>>> collect = compile.getCompiledFramesData().stream()
					.map(frame -> frame.getFrameData()).collect(Collectors.toList());
			return Response.ok(new MyClass(matrixEntity.getId(), matrixEntity.getRowCount(),
					matrixEntity.getColumnCount(), collect)).build();
		} catch (final MatrixDoesntExistsException e) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@Data
	static class MyClass {
		int id;
		int rowCount;
		int columnCount;
		List<List<List<Led>>> frames;

		public MyClass(int id, int rowCount, int columnCount, List<List<List<Led>>> frames) {
			this.id = id;
			this.rowCount = rowCount;
			this.columnCount = columnCount;
			this.frames = frames;
		}
	}
}