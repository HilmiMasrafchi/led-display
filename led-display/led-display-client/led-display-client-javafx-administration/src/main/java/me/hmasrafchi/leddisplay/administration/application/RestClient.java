/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application;

import static javax.ws.rs.client.Entity.json;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.math.BigInteger;
import java.util.concurrent.Future;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.Response;

import me.hmasrafchi.leddisplay.model.view.CreateMatrixCommand;
import me.hmasrafchi.leddisplay.model.view.MatrixView;

/**
 * @author michelin
 *
 */
public final class RestClient {
	private static final String API_DOMAIN = "http://localhost:8080/1.0";
	private static final Client JAXRS_CLIENT = ClientBuilder.newClient();

	public static Future<Response> createMatrix(final CreateMatrixCommand createMatrixCommand,
			final InvocationCallback<Response> invocationCallback) {
		return JAXRS_CLIENT.target(API_DOMAIN).path("matrices").request(APPLICATION_JSON).async()
				.post(json(createMatrixCommand), invocationCallback);
	}

	public static Future<Response> updateMatrix(final MatrixView matrixView,
			final InvocationCallback<Response> invocationCallback) {
		return JAXRS_CLIENT.target(API_DOMAIN).path("matrices").request(APPLICATION_JSON).async()
				.put(Entity.json(matrixView), invocationCallback);
	}

	public static Future<Response> getAllMatrices(final InvocationCallback<Response> invocationCallback) {
		return JAXRS_CLIENT.target(API_DOMAIN).path("matrices").request(APPLICATION_JSON).async()
				.get(invocationCallback);
	}

	public static Future<Response> deleteMatrix(final BigInteger matrixId,
			final InvocationCallback<Response> invocationCallback) {
		return JAXRS_CLIENT.target(API_DOMAIN).path(String.format("%s/%d", "matrices", matrixId)).request().async()
				.delete(invocationCallback);
	}

	public static void close() {
		JAXRS_CLIENT.close();
	}
}