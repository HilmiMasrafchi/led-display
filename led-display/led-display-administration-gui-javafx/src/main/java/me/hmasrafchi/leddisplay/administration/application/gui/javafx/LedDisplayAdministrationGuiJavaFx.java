/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * @author michelin
 *
 */
public final class LedDisplayAdministrationGuiJavaFx extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(final Stage primaryStage) throws Exception {
		final Client jaxRsClient = ClientBuilder.newClient();
		final Response getAllMatricesResponse = jaxRsClient.target("http://localhost:8080/led-display-administration")
				.path("matrices").request(MediaType.APPLICATION_JSON).get();
		// getAllMatricesResponse.readEntity(new GenericType<List<MatrixView>>()
		// {
		// });

		primaryStage.setTitle("Hello World!");

		final Parent root = new BorderPane(null, null, null, null, getMatricesListGui());
		primaryStage.setScene(new Scene(root, 1200, 800));
		primaryStage.show();
	}

	private Node getMatricesListGui() {
		final ObservableList<String> items = FXCollections.observableArrayList("Single", "Double", "Suite",
				"Family App");
		return new ListView<>(items);
	}
}