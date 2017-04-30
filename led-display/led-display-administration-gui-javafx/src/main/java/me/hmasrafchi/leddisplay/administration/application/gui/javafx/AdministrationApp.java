/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.hmasrafchi.leddisplay.administration.model.view.MatrixView;

/**
 * @author michelin
 *
 */
public final class AdministrationApp extends Application {
	public static final String API_DOMAIN = "http://localhost:8080/led-display-administration";

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(final Stage primaryStage) throws Exception {
		final Client jaxRsClient = ClientBuilder.newClient();
		final Response getAllMatricesResponse = jaxRsClient.target(API_DOMAIN).path("matrices")
				.request(APPLICATION_JSON).get();
		final int responseStatusCode = getAllMatricesResponse.getStatus();
		if (responseStatusCode == Response.Status.OK.getStatusCode()
				|| responseStatusCode == Response.Status.NOT_FOUND.getStatusCode()) {
			final List<MatrixView> matrices = getAllMatricesResponse.readEntity(new GenericType<List<MatrixView>>() {
			});

			final AdministrationGui2 root = new AdministrationGui2(matrices == null ? new ArrayList<>() : matrices);
			final Scene scene = new Scene(root, 1200, 800);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Administration Panel");
			primaryStage.show();
		} else {
			throw new RuntimeException("can not get list of matrices: " + responseStatusCode);
		}
	}
}