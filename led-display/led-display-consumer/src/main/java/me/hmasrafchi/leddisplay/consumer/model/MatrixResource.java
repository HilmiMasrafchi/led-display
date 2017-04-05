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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * @author michelin
 *
 */
@Stateless
@Path("matrices")
public class MatrixResource {
	@PersistenceContext
	private EntityManager entityManager;

	@GET
	@Path("{matrixId}/compiled_frames")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCompiledFrames(@PathParam("matrixId") final Integer matrixId) {
		return Response.status(Status.NOT_IMPLEMENTED).build();
	}
}