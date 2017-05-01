/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx;

import static javax.ws.rs.client.Entity.json;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import me.hmasrafchi.leddisplay.administration.model.view.CreateMatrixCommand;
import me.hmasrafchi.leddisplay.administration.model.view.MatrixView;

/**
 * @author michelin
 *
 */
final class RestClient {
	private static final String API_DOMAIN = "http://localhost:8080/led-display-administration";
	private static final Client JAXRS_CLIENT = ClientBuilder.newClient();

	static Response createMatrix(final CreateMatrixCommand createMatrixCommand) {
		return JAXRS_CLIENT.target(API_DOMAIN).path("matrices").request(APPLICATION_JSON)
				.post(json(createMatrixCommand));
	}

	static Response updateMatrix(final MatrixView matrixView) {
		return JAXRS_CLIENT.target(API_DOMAIN).path("matrices").request(APPLICATION_JSON).put(Entity.json(matrixView));
	}

	static Response getAllMatrices() {
		return JAXRS_CLIENT.target(API_DOMAIN).path("matrices").request(APPLICATION_JSON).get();
	}
}