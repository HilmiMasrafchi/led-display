/**
 * 
 */
package me.hmasrafchi.leddisplay.ws;

import static java.util.Arrays.asList;
import static me.hmasrafchi.leddisplay.api.LedRgbColor.BLUE;
import static me.hmasrafchi.leddisplay.api.LedRgbColor.GREEN;
import static me.hmasrafchi.leddisplay.api.LedRgbColor.RED;
import static me.hmasrafchi.leddisplay.api.LedRgbColor.YELLOW;
import static me.hmasrafchi.leddisplay.api.LedState.OFF;
import static me.hmasrafchi.leddisplay.api.LedState.ON;
import static me.hmasrafchi.leddisplay.api.LedState.TRANSPARENT;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.api.LedRgbColor;
import me.hmasrafchi.leddisplay.api.LedState;
import me.hmasrafchi.leddisplay.model.CompiledFrames;
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
public final class TestDisplayJavaFx extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		final int matrixRowsCount = 6;
		final int matrixColumnsCount = 5;

		// OverlayRollHorizontal
		final List<List<LedState>> statesRoll = asList( //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON), //
				asList(ON, ON, ON, ON, ON, ON, ON));

		final int yPosition = 1;
		final Overlay overlayRoll = new OverlayRollHorizontally(new TwoDimensionalListRectangular<>(statesRoll), GREEN,
				BLUE, matrixColumnsCount, yPosition);

		// OverlayStationary
		final List<List<LedState>> statesStationary = asList(asList(ON, ON, ON, ON, ON), //
				asList(TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT), //
				asList(ON, TRANSPARENT, TRANSPARENT, TRANSPARENT, ON), //
				asList(OFF, TRANSPARENT, TRANSPARENT, TRANSPARENT, OFF), //
				asList(ON, TRANSPARENT, TRANSPARENT, TRANSPARENT, ON), //
				asList(ON, ON, ON, ON, ON));
		final Overlay overlayStationary = new OverlayStationary(new TwoDimensionalListRectangular<>(statesStationary),
				RED, YELLOW, 1);

		final me.hmasrafchi.leddisplay.model.Scene sceneOverlayed = new SceneOverlayed(
				asList(overlayRoll, overlayStationary));
		final me.hmasrafchi.leddisplay.model.Scene sceneRandom = new SceneRandomColor(asList(BLUE, RED));
		final me.hmasrafchi.leddisplay.model.Scene sceneComposite = new SceneComposited(
				asList(sceneRandom, sceneOverlayed));
		final Matrix matrix = new MatrixDefault();
		final CompiledFrames compiledFrames = matrix.compile(sceneComposite, matrixRowsCount, matrixColumnsCount);

		final GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(0, 0, 0, 0));
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

						final LedRgbColor rgbColor = led.getRgbColor();
						ledJFx.setFill(Color.rgb(rgbColor.getR(), rgbColor.getG(), rgbColor.getB()));
					}
				}
			}
		}));
		timeline.setCycleCount(Timeline.INDEFINITE);
		primaryStage.show();
		timeline.play();
	}

	private Pane getGrid(final TwoDimensionalListRectangular<LedJFx> leds) {
		final GridPane gridPane = new GridPane();
		for (int i = 0; i < leds.getRowCount(); i++) {
			gridPane.addRow(i, leds.getRowAt(i).stream().toArray(Node[]::new));
		}

		return gridPane;
	}
}