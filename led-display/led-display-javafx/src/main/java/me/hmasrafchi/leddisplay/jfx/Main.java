/**
 * 
 */
package me.hmasrafchi.leddisplay.jfx;

import java.util.List;

import com.google.inject.Guice;
import com.google.inject.Injector;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import me.hmasrafchi.leddisplay.framework.Board;
import me.hmasrafchi.leddisplay.framework.Led;

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
		final Injector injector = Guice.createInjector(new DefaultModule());
		final Board board = injector.getInstance(Board.class);

		final Pane pane = new Pane();
		final List<List<Led>> leds = board.getLeds();
		for (final List<Led> currentRow : leds) {
			for (final Led currentLed : currentRow) {
				final Text text = (Text) currentLed;
				pane.getChildren().add(text);
			}
		}

		final Scene scene = new Scene(pane, WINDOW_WIDTH, WINDOW_HEIGHT, true);
		primaryStage.setScene(scene);

		primaryStage.show();

		final AnimationJavaFX animation = new AnimationJavaFX(board);
		animation.start();
	}
}