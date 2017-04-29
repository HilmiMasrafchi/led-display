/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.hmasrafchi.leddisplay.administration.model.view.MatrixView;

/**
 * @author michelin
 *
 */
public final class AdministrationApp extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(final Stage primaryStage) throws Exception {
		final Client jaxRsClient = ClientBuilder.newClient();
		final Response getAllMatricesResponse = jaxRsClient.target("http://localhost:8080/led-display-administration")
				.path("matrices").request(APPLICATION_JSON).get();
		if (getAllMatricesResponse.getStatus() == Response.Status.OK.getStatusCode()) {
			final List<MatrixView> matrices = getAllMatricesResponse.readEntity(new GenericType<List<MatrixView>>() {
			});

			final AdministrationGui root = new AdministrationGui(matrices);
			primaryStage.setScene(new Scene(root, 1200, 800));
		} else {
			primaryStage.setScene(new Scene(new Group()));
		}

		primaryStage.setTitle("Administration Panel");
		primaryStage.show();
	}
}