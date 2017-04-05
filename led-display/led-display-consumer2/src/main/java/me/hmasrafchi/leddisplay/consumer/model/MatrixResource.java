/**
 * 
 */
package me.hmasrafchi.leddisplay.consumer.model;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author michelin
 *
 */
@Stateless
@Path(PathLiterals.MATRICES)
public class MatrixResource {
	@PersistenceContext
	private EntityManager entityManager;

	@GET
	@Path("{matrixId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMatrix(@PathParam("matrixId") final Integer matrixId) {
		final Client client = ClientBuilder.newClient();
		final WebTarget target = client.target("http://localhost:8080/led-display-administration")
				.path("matrices/" + matrixId);
		final Response response = target.request(MediaType.APPLICATION_JSON).get();
		final Matrix matrix = response.readEntity(Matrix.class);

		return Response.ok(matrix).build();
	}

	@GET
	@Path("{matrixId}/compiled_frames")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCompiledFrames(@PathParam("matrixId") final Integer matrixId) {
		final Client client = ClientBuilder.newClient();
		final WebTarget target = client.target("http://localhost:8080/led-display-administration")
				.path("matrices/" + matrixId);
		final Response response = target.request(MediaType.APPLICATION_JSON).get();
		final Matrix matrix = response.readEntity(Matrix.class);
		final CompiledFrames compiledFrames = matrix.getCompiledFrames();

		return Response.ok(compiledFrames).build();
	}
}