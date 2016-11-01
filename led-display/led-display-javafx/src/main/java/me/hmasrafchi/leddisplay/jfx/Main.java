/**
 * 
 */
package me.hmasrafchi.leddisplay.jfx;

import com.google.inject.Guice;
import com.google.inject.Injector;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import me.hmasrafchi.leddisplay.api.Board;

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
				.canvasYPosition(1).delayBetweenFrames(50).build();

		final Injector injector = Guice.createInjector(new DefaultModule(configuration));
		final Board board = injector.getInstance(Board.class);

		final Scene scene = new Scene((Pane) board, WINDOW_WIDTH, WINDOW_HEIGHT, true);
		primaryStage.setScene(scene);

		primaryStage.show();

		board.startAnimation();
	}
}