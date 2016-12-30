/**
 * 
 */
package me.hmasrafchi.leddisplay.ws;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import me.hmasrafchi.leddisplay.model.CompiledFrames;
import me.hmasrafchi.leddisplay.model.Led;
import me.hmasrafchi.leddisplay.model.Led.RgbColor;
import me.hmasrafchi.leddisplay.model.Matrix;
import me.hmasrafchi.leddisplay.model.MatrixDefault;
import me.hmasrafchi.leddisplay.model.Overlay;
import me.hmasrafchi.leddisplay.model.OverlayRollHorizontally;
import me.hmasrafchi.leddisplay.model.OverlayStationary;
import me.hmasrafchi.leddisplay.model.SceneComposited;
import me.hmasrafchi.leddisplay.model.SceneOverlayed;
import me.hmasrafchi.leddisplay.model.SceneRandomColor;
import me.hmasrafchi.leddisplay.util.CyclicIterator;
import me.hmasrafchi.leddisplay.util.TwoDimensionalListRectangular;

/**
 * @author michelin
 *
 */
public final class TestWs extends Application {
	private final static Type TYPE = new TypeToken<TwoDimensionalListRectangular<Led>>() {
		private static final long serialVersionUID = 1L;
	}.getType();

	private final Gson gson = new GsonBuilder().create();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		final int matrixRowsCount = 6;
		final int matrixColumnsCount = 5;

		// OverlayRollHorizontal
		final List<List<Overlay.State>> statesRoll = Arrays.asList(
				Arrays.asList(Overlay.State.ON, Overlay.State.ON, Overlay.State.ON, Overlay.State.ON, Overlay.State.ON,
						Overlay.State.ON, Overlay.State.ON),
				Arrays.asList(Overlay.State.ON, Overlay.State.OFF, Overlay.State.ON, Overlay.State.TRANSPARENT,
						Overlay.State.ON, Overlay.State.TRANSPARENT, Overlay.State.ON),
				Arrays.asList(Overlay.State.ON, Overlay.State.ON, Overlay.State.ON, Overlay.State.ON, Overlay.State.ON,
						Overlay.State.ON, Overlay.State.ON));
		RgbColor expectedRollColor = RgbColor.GREEN;

		final int yPosition = 1;
		final Overlay overlayRoll = new OverlayRollHorizontally(new TwoDimensionalListRectangular<>(statesRoll),
				expectedRollColor, RgbColor.BLUE, matrixColumnsCount, yPosition);

		// OverlayStationary
		final List<List<Overlay.State>> statesStationary = Arrays.asList(
				Arrays.asList(Overlay.State.ON, Overlay.State.ON, Overlay.State.ON, Overlay.State.ON, Overlay.State.ON),
				Arrays.asList(Overlay.State.TRANSPARENT, Overlay.State.TRANSPARENT, Overlay.State.TRANSPARENT,
						Overlay.State.TRANSPARENT, Overlay.State.TRANSPARENT),
				Arrays.asList(Overlay.State.ON, Overlay.State.TRANSPARENT, Overlay.State.TRANSPARENT,
						Overlay.State.TRANSPARENT, Overlay.State.ON),
				Arrays.asList(Overlay.State.OFF, Overlay.State.TRANSPARENT, Overlay.State.TRANSPARENT,
						Overlay.State.TRANSPARENT, Overlay.State.OFF),
				Arrays.asList(Overlay.State.ON, Overlay.State.TRANSPARENT, Overlay.State.TRANSPARENT,
						Overlay.State.TRANSPARENT, Overlay.State.ON),
				Arrays.asList(Overlay.State.ON, Overlay.State.ON, Overlay.State.ON, Overlay.State.ON,
						Overlay.State.ON));
		final RgbColor stationaryForegroundColor = RgbColor.RED;
		final RgbColor stationaryBackgroundColor = RgbColor.YELLOW;
		final Overlay overlayStationary = new OverlayStationary(new TwoDimensionalListRectangular<>(statesStationary),
				stationaryForegroundColor, stationaryBackgroundColor);

		final me.hmasrafchi.leddisplay.model.Scene sceneOverlayed = new SceneOverlayed(
				Arrays.asList(overlayRoll, overlayStationary));
		final me.hmasrafchi.leddisplay.model.Scene sceneRandom = new SceneRandomColor(
				Arrays.asList(Led.RgbColor.BLUE, Led.RgbColor.RED));
		final me.hmasrafchi.leddisplay.model.Scene sceneComposite = new SceneComposited(
				Arrays.asList(sceneRandom, sceneOverlayed));
		final Matrix matrix = new MatrixDefault();
		final CompiledFrames compiledFrames = matrix.compile(sceneComposite, matrixRowsCount, matrixColumnsCount);

		final GridPane gridPane = new GridPane();
		final Collection<TwoDimensionalListRectangular<Led>> frames = new ArrayList<>();
		final Iterator<TwoDimensionalListRectangular<Led>> iterator = compiledFrames.listIterator();
		while (iterator.hasNext()) {
			frames.add(iterator.next());
		}

		final int guiRowsCount = matrixRowsCount;
		final int guiColumnsCount = matrixColumnsCount;
		TwoDimensionalListRectangular<LedJFx> map = TwoDimensionalListRectangular.create(LedJFx::new, guiRowsCount,
				guiColumnsCount);
		for (int i = 0; i < map.getRowCount(); i++) {
			gridPane.addRow(i, map.getRowAt(i).stream().toArray(Node[]::new));
		}

		primaryStage.setScene(new Scene(gridPane));

		final Timeline timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(200), new EventHandler<ActionEvent>() {
			CyclicIterator<TwoDimensionalListRectangular<Led>> framesIterator = new CyclicIterator<>(frames);

			@Override
			public void handle(ActionEvent event) {
				final TwoDimensionalListRectangular<Led> frame = framesIterator.next();
				for (int r = 0; r < frame.getRowCount(); r++) {
					for (int c = 0; c < frame.getColumnCount(); c++) {
						final LedJFx ledJFx = map.getRowAt(r).get(c);
						final Led led = frame.getRowAt(r).get(c);

						final RgbColor rgbColor = led.getRgbColor();
						ledJFx.setFill(Color.rgb(rgbColor.getR(), rgbColor.getG(), rgbColor.getB()));
					}
				}
			}
		}));
		timeline.setCycleCount(Timeline.INDEFINITE);
		primaryStage.show();
		timeline.play();
	}

	private String toJson(final TwoDimensionalListRectangular<Led> frame) {
		Type type = new TypeToken<TwoDimensionalListRectangular<Led>>() {
			private static final long serialVersionUID = 1L;
		}.getType();

		final Gson gson = new GsonBuilder().create();
		return gson.toJson(frame, type);
	}

	private TwoDimensionalListRectangular<LedJFx> fromJson(final String json) {
		final TwoDimensionalListRectangular<Led> fromJson = gson.fromJson(json, TYPE);
		return fromJson.map((i) -> {
			RgbColor rgbColor = i.getRgbColor();
			LedJFx ledJFx = new LedJFx();
			ledJFx.setFill(Color.rgb(rgbColor.getR(), rgbColor.getG(), rgbColor.getB()));
			return ledJFx;
		});
	}

	private Pane getGrid(final TwoDimensionalListRectangular<LedJFx> leds) {
		final GridPane gridPane = new GridPane();
		for (int i = 0; i < leds.getRowCount(); i++) {
			gridPane.addRow(i, leds.getRowAt(i).stream().toArray(Node[]::new));
		}

		return gridPane;
	}
}