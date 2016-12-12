/**
 * 
 */
package me.hmasrafchi.leddisplay.jfx;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import javax.inject.Provider;

import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import me.hmasrafchi.leddisplay.infrastructure.GeneratorLed;
import me.hmasrafchi.leddisplay.infrastructure.GeneratorLedWithUniformText;
import me.hmasrafchi.leddisplay.infrastructure.GeneratorMatrix;
import me.hmasrafchi.leddisplay.model.CompositeScene;
import me.hmasrafchi.leddisplay.model.Matrix;
import me.hmasrafchi.leddisplay.model.OverlayedScene;
import me.hmasrafchi.leddisplay.model.RandomColorScene;
import me.hmasrafchi.leddisplay.model.Scene;
import me.hmasrafchi.leddisplay.model.api.Board;
import me.hmasrafchi.leddisplay.model.api.Led;
import me.hmasrafchi.leddisplay.model.api.Led.RgbColor;
import me.hmasrafchi.leddisplay.model.scene.overlay.Overlay;
import me.hmasrafchi.leddisplay.model.scene.overlay.OverlayRollHorizontal;
import me.hmasrafchi.leddisplay.model.scene.overlay.OverlayStationary;

/**
 * @author michelin
 *
 */
public final class Main extends Application {
	private final static double WINDOW_WIDTH = 1200d;
	private final static double WINDOW_HEIGHT = 600d;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(final Stage primaryStage) throws Exception {
		final Configuration configuration = Configuration.builder().matrixColumnsCount(5).matrixRowsCount(6)
				.canvasYPosition(1).delayBetweenFrames(100).matrixLedHorizontalGap(1).matrixLedVerticalGap(1).build();

		final Provider<Led> provider = new ProviderLEDJFx();
		final GeneratorLed generatorLed = new GeneratorLedWithUniformText(provider, "●", 140d);
		final GeneratorMatrix generatorMatrix = new GeneratorMatrix(generatorLed,
				configuration.getMatrixLedHorizontalGap(), configuration.getMatrixLedVerticalGap());
		final Matrix matrix = generatorMatrix.next(configuration.getMatrixColumnsCount(),
				configuration.getMatrixRowsCount());

		final List<List<Overlay.State>> statesOverlay1 = Arrays.asList(
				Arrays.asList(Overlay.State.ON, Overlay.State.ON, Overlay.State.ON, Overlay.State.ON, Overlay.State.ON),
				Arrays.asList(Overlay.State.OFF, Overlay.State.TRANSPARENT, Overlay.State.TRANSPARENT,
						Overlay.State.TRANSPARENT, Overlay.State.OFF),
				Arrays.asList(Overlay.State.ON, Overlay.State.TRANSPARENT, Overlay.State.TRANSPARENT,
						Overlay.State.TRANSPARENT, Overlay.State.ON),
				Arrays.asList(Overlay.State.OFF, Overlay.State.TRANSPARENT, Overlay.State.TRANSPARENT,
						Overlay.State.TRANSPARENT, Overlay.State.OFF),
				Arrays.asList(Overlay.State.ON, Overlay.State.TRANSPARENT, Overlay.State.TRANSPARENT,
						Overlay.State.TRANSPARENT, Overlay.State.ON),
				Arrays.asList(Overlay.State.ON, Overlay.State.ON, Overlay.State.ON, Overlay.State.ON,
						Overlay.State.ON));
		final Overlay overlay1 = new OverlayStationary(statesOverlay1, RgbColor.RED, RgbColor.ORANGE);

		final List<List<Overlay.State>> statesOverlay2 = Arrays.asList(
				Arrays.asList(Overlay.State.ON, Overlay.State.ON, Overlay.State.ON, Overlay.State.ON, Overlay.State.ON,
						Overlay.State.ON, Overlay.State.ON),
				Arrays.asList(Overlay.State.ON, Overlay.State.TRANSPARENT, Overlay.State.ON, Overlay.State.TRANSPARENT,
						Overlay.State.ON, Overlay.State.TRANSPARENT, Overlay.State.ON),
				Arrays.asList(Overlay.State.ON, Overlay.State.ON, Overlay.State.ON, Overlay.State.ON, Overlay.State.ON,
						Overlay.State.ON, Overlay.State.ON));
		final Overlay overlay2 = new OverlayRollHorizontal(statesOverlay2, RgbColor.GREEN,
				configuration.getCanvasYPosition(), configuration.getMatrixColumnsCount());

		final Scene firstScene = new OverlayedScene(Arrays.asList(overlay2, overlay1));

		final List<RgbColor> colors = Arrays.asList(RgbColor.RED, RgbColor.GREEN, RgbColor.BLUE);
		final Scene secondScene = new RandomColorScene(colors);

		final CompositeScene compositeScene = new CompositeScene(Arrays.asList(firstScene, secondScene));

		final Board board = new BoardJFX(compositeScene, matrix,
				Duration.ofMillis(configuration.getDelayBetweenFrames()));

		final javafx.scene.Scene scene = new javafx.scene.Scene((Pane) board, WINDOW_WIDTH, WINDOW_HEIGHT, true);
		primaryStage.setScene(scene);

		primaryStage.show();
		board.startAnimation();
	}
}