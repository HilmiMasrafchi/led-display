/**
 * 
 */
package me.hmasrafchi.leddisplay.administration;

import static java.lang.String.valueOf;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

/**
 * @author michelin
 *
 */
@Stateless
@Path(PathLiterals.MATRICES)
public class MatrixResource {
	@PersistenceContext
	private EntityManager entityManager;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createMatrix(final Matrix matrix, @Context UriInfo uriInfo) {
		matrix.setScene(new SceneComposite());
		entityManager.persist(matrix);

		final UriBuilder builder = uriInfo.getAbsolutePathBuilder();
		builder.path(valueOf(matrix.getId()));
		return Response.created(builder.build()).build();
	}

	@GET
	@Path("{matrixId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMatrix(@PathParam("matrixId") final Integer matrixId) {
		final Matrix matrix = entityManager.find(Matrix.class, matrixId);
		if (matrix == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		return Response.ok(matrix).build();
	}

	@POST
	@Path("{matrixId}/scenes/roll")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response appendSceneRollHorizontally(@PathParam("matrixId") final Integer matrixId,
			final SceneRollHorizontally scene) {
		final Matrix matrix = entityManager.find(Matrix.class, matrixId);
		matrix.getScene().getScenes().add(scene);

		return Response.ok().build();
	}
}