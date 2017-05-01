/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx;

import static javax.ws.rs.client.Entity.json;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import me.hmasrafchi.leddisplay.administration.model.view.CreateMatrixCommand;

/**
 * @author michelin
 *
 */
public final class RestClient {
	private static final String API_DOMAIN = "http://localhost:8080/led-display-administration";
	private static final Client JAXRS_CLIENT = ClientBuilder.newClient();

	public static Response createMatrix(final CreateMatrixCommand createMatrixCommand) {
		return JAXRS_CLIENT.target(API_DOMAIN).path("matrices").request(APPLICATION_JSON)
				.post(json(createMatrixCommand));
	}

	public static Response getAllMatrices() {
		return JAXRS_CLIENT.target(API_DOMAIN).path("matrices").request(APPLICATION_JSON).get();
	}
}