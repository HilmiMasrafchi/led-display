/**
 * 
 */
package me.hmasrafchi.leddisplay.jfx;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Provider;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import me.hmasrafchi.leddisplay.api.Board;
import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.api.Led.RgbColor;
import me.hmasrafchi.leddisplay.model.GeneratorLed;
import me.hmasrafchi.leddisplay.model.GeneratorLedWithUniformText;
import me.hmasrafchi.leddisplay.model.GeneratorMatrix;
import me.hmasrafchi.leddisplay.model.Matrix;
import me.hmasrafchi.leddisplay.model.Scene;
import me.hmasrafchi.leddisplay.model.SceneOverlayed;
import me.hmasrafchi.leddisplay.model.SceneRandomColor;
import me.hmasrafchi.leddisplay.model.overlay.Overlay;
import me.hmasrafchi.leddisplay.model.overlay.Overlay.State;
import me.hmasrafchi.leddisplay.model.overlay.OverlayRollHorizontal;
import me.hmasrafchi.leddisplay.model.overlay.OverlayStationary;

/**
 * @author michelin
 *
 */
public final class Main extends Application {
	private final static double WINDOW_WIDTH = 1200d;
	private final static double WINDOW_HEIGHT = 600d;

	public static void main(final String[] args) {
		launch(args);
	}

	@Override
	public void start(final Stage primaryStage) throws Exception {
		final Configuration configuration = Configuration.builder().matrixColumnsCount(5).matrixRowsCount(6)
				.canvasYPosition(1).delayBetweenFrames(100).matrixLedHorizontalGap(1).matrixLedVerticalGap(1).build();

		// OverlayRollHorizontal
		final List<List<State>> statesRoll = Arrays.asList(
				Arrays.asList(Overlay.State.ON, Overlay.State.ON, Overlay.State.ON,
						Overlay.State.ON, Overlay.State.ON, Overlay.State.ON, Overlay.State.ON),
				Arrays.asList(Overlay.State.ON, Overlay.State.TRANSPARENT, Overlay.State.ON,
						Overlay.State.TRANSPARENT, Overlay.State.ON, Overlay.State.TRANSPARENT,
						Overlay.State.ON),
				Arrays.asList(Overlay.State.ON, Overlay.State.ON, Overlay.State.ON,
						Overlay.State.ON, Overlay.State.ON, Overlay.State.ON, Overlay.State.ON));
		RgbColor expectedRollColor = RgbColor.GREEN;
		final Overlay overlayRoll = new OverlayRollHorizontal(statesRoll, expectedRollColor, RgbColor.YELLOW,
				1, configuration.getMatrixColumnsCount());

		// OverlayStationary
		final List<List<State>> statesStationary = Arrays.asList(
				Arrays.asList(Overlay.State.ON, Overlay.State.ON, Overlay.State.ON,
						Overlay.State.ON, Overlay.State.ON),
				Arrays.asList(Overlay.State.TRANSPARENT, Overlay.State.TRANSPARENT,
						Overlay.State.TRANSPARENT, Overlay.State.TRANSPARENT, Overlay.State.TRANSPARENT),
				Arrays.asList(Overlay.State.ON, Overlay.State.TRANSPARENT, Overlay.State.TRANSPARENT,
						Overlay.State.TRANSPARENT, Overlay.State.ON),
				Arrays.asList(Overlay.State.OFF, Overlay.State.TRANSPARENT, Overlay.State.TRANSPARENT,
						Overlay.State.TRANSPARENT, Overlay.State.OFF),
				Arrays.asList(Overlay.State.ON, Overlay.State.TRANSPARENT, Overlay.State.TRANSPARENT,
						Overlay.State.TRANSPARENT, Overlay.State.ON),
				Arrays.asList(Overlay.State.ON, Overlay.State.ON, Overlay.State.ON,
						Overlay.State.ON, Overlay.State.ON));
		final RgbColor stationaryForegroundColor = RgbColor.RED;
		final RgbColor stationaryBackgroundColor = RgbColor.YELLOW;
		final Overlay overlayStationary = new OverlayStationary(statesStationary, stationaryForegroundColor,
				stationaryBackgroundColor);

		final Scene firstScene = new SceneOverlayed(Arrays.asList(overlayRoll, overlayStationary));

		final List<RgbColor> colors = Arrays.asList(RgbColor.RED, RgbColor.GREEN, RgbColor.BLUE);
		final Scene secondScene = new SceneRandomColor(colors);

		final Provider<Led> provider = new ProviderLEDJFx();
		final GeneratorLed generatorLed = new GeneratorLedWithUniformText(provider, "●", 140d);
		final GeneratorMatrix generatorMatrix = new GeneratorMatrix(generatorLed,
				configuration.getMatrixLedHorizontalGap(), configuration.getMatrixLedVerticalGap());
		final Matrix matrix = generatorMatrix.next(Arrays.asList(overlayRoll, overlayStationary, secondScene),
				configuration.getMatrixColumnsCount(), configuration.getMatrixRowsCount());

		final Board board = new BoardJFX(matrix, Duration.ofMillis(configuration.getDelayBetweenFrames()));

		final Pane pane = new Pane();
		final Collection<? extends Node> textNodes = matrix.stream().map(led -> (Text) led)
				.collect(Collectors.toList());
		pane.getChildren().addAll(textNodes);
		final javafx.scene.Scene scene = new javafx.scene.Scene(pane, WINDOW_WIDTH, WINDOW_HEIGHT, true);
		primaryStage.setScene(scene);

		primaryStage.show();
		board.startAnimation();

		// // 1
		// matrix.nextFrame();
		// // 2
		// matrix.nextFrame();
		// // 3
		// matrix.nextFrame();
		// // 4
		// matrix.nextFrame();
		// // 5
		// matrix.nextFrame();
		// // 6
		// matrix.nextFrame();
		// // 7
		// matrix.nextFrame();
		// // 8
		// matrix.nextFrame();
		// // 9
		// matrix.nextFrame();
		// // 10
		// matrix.nextFrame();
		// // 11
		// matrix.nextFrame();
		// // 12
		// matrix.nextFrame();
		// // exhausted
		// // 13
		// matrix.nextFrame();
		// matrix.nextFrame();
		// matrix.nextFrame();
		//
		// // exhausedj
		// matrix.nextFrame();
	}
}