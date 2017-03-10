/**
 * 
 */
package me.hmasrafchi.leddisplay.ws;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Data;
import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.api.RgbColor;
import me.hmasrafchi.leddisplay.util.CyclicIterator;
import me.hmasrafchi.leddisplay.util.TwoDimensionalListRectangular;

/**
 * @author michelin
 *
 */
public final class LedDisplayJavaFx extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		final Client client = ClientBuilder.newClient();
		final Response response = client.target("http://localhost:8080/led-display-webservices-rest")
				.path("matrix/1/frames").request(MediaType.APPLICATION_JSON).get();

		final MyClass myClass = response.readEntity(MyClass.class);
		final List<List<List<Led>>> allFrames = myClass.getFrames();

		final GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(0, 0, 0, 0));
		final int guiRowsCount = myClass.getRowCount();
		final int guiColumnsCount = myClass.getColumnCount();

		TwoDimensionalListRectangular<LedJFx> map = new TwoDimensionalListRectangular<>(LedJFx::new, guiRowsCount,
				guiColumnsCount);
		for (int i = 0; i < map.getRowCount(); i++) {
			gridPane.addRow(i, map.getRowAt(i).stream().toArray(Node[]::new));
		}

		primaryStage.setScene(new Scene(gridPane));

		final Timeline timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>() {
			final CyclicIterator<List<List<Led>>> framesIterator = new CyclicIterator<>(allFrames);

			@Override
			public void handle(ActionEvent event) {
				List<List<Led>> next = framesIterator.next();
				TwoDimensionalListRectangular<Led> frame = new TwoDimensionalListRectangular<>(next);

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

	@Data
	static class MyClass {
		Integer id;
		Integer rowCount;
		Integer columnCount;
		List<List<List<Led>>> frames;
	}
}