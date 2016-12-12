/**
 * 
 */
package me.hmasrafchi.leddisplay.jfx;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Provider;

import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import me.hmasrafchi.leddisplay.api.Board;
import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.api.Led.RgbColor;
import me.hmasrafchi.leddisplay.framework.Matrix;
import me.hmasrafchi.leddisplay.framework.generator.GeneratorLed;
import me.hmasrafchi.leddisplay.framework.generator.GeneratorLedUniformText;
import me.hmasrafchi.leddisplay.framework.generator.GeneratorMatrix;
import me.hmasrafchi.leddisplay.framework.scene.CompositeScene;
import me.hmasrafchi.leddisplay.framework.scene.OverlayedScene;
import me.hmasrafchi.leddisplay.framework.scene.RandomColorScene;
import me.hmasrafchi.leddisplay.framework.scene.Scene;
import me.hmasrafchi.leddisplay.framework.scene.overlay.Overlay;
import me.hmasrafchi.leddisplay.framework.scene.overlay.OverlayRollHorizontal;
import me.hmasrafchi.leddisplay.framework.scene.overlay.OverlayStationary;

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
				.canvasYPosition(1).delayBetweenFrames(100).build();

		final Provider<Led> provider = new ProviderLEDJFx();
		final GeneratorLed generatorLed = new GeneratorLedUniformText(provider, "●", 140d);
		final GeneratorMatrix generatorMatrix = new GeneratorMatrix(generatorLed);
		final Matrix matrix = generatorMatrix.next(configuration.getMatrixColumnsCount(),
				configuration.getMatrixRowsCount());

		primaryStage.show();

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

		final CompositeScene compositeScene = new CompositeScene(Arrays.asList(firstScene));

		final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.schedule(() -> {
			final List<RgbColor> colors = Arrays.asList(RgbColor.RED, RgbColor.GREEN, RgbColor.BLUE);
			final Scene randomColorScene = new RandomColorScene(colors);
			compositeScene.addScene(randomColorScene);
		}, 1, TimeUnit.SECONDS);

		final Board board = new BoardJFX(compositeScene, matrix,
				Duration.ofMillis(configuration.getDelayBetweenFrames()));

		final javafx.scene.Scene scene = new javafx.scene.Scene((Pane) board, WINDOW_WIDTH, WINDOW_HEIGHT, true);
		primaryStage.setScene(scene);

		board.startAnimation();
	}
}