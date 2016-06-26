/**
 * 
 */
package me.hmasrafchi.leddisplay.jfx;

import com.google.inject.Guice;
import com.google.inject.Injector;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import me.hmasrafchi.leddisplay.api.LED;
import me.hmasrafchi.leddisplay.framework.Board;
import me.hmasrafchi.leddisplay.framework.BoardGenerator;

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
	public void start(Stage primaryStage) throws Exception {
		final Injector injector = Guice.createInjector(new DefaultModule());
		final BoardGenerator boardGenerator = injector.getInstance(BoardGenerator.class);
		final Board board = boardGenerator.getByRowAndColumnCount(50, 100);

		final Pane pane = new Pane();
		final LED[][] leDs = board.getLEDs();
		for (int i = 0; i < leDs.length; i++) {
			for (int j = 0; j < leDs[0].length; j++) {
				Text text = (Text) leDs[i][j];
				pane.getChildren().add(text);
			}
		}

		final Scene scene = new Scene(pane, WINDOW_WIDTH, WINDOW_HEIGHT, true);
		primaryStage.setScene(scene);

		primaryStage.show();
	}
}