/**
 * 
 */
package me.hmasrafchi.leddisplay.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.net.URI;
import java.net.URL;

import javax.ws.rs.Consumes;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import me.hmasrafchi.leddisplay.rest.persist.MatrixEntity;

/**
 * @author michelin
 *
 */
@RunWith(Arquillian.class)
public final class TestMatrixResource {
	@ArquillianResource
	private URL deploymentURL;

	@Deployment(testable = false)
	public static WebArchive createDeployment() {
		return ShrinkWrap.create(WebArchive.class) //
				.addPackages(true, "me.hmasrafchi.leddisplay.api") //
				.addPackages(true, "me.hmasrafchi.leddisplay.model") //
				.addPackages(true, "me.hmasrafchi.leddisplay.rest") //
				.addPackages(true, "me.hmasrafchi.leddisplay.rest.persist") //
				.addPackages(true, "me.hmasrafchi.leddisplay.rest.persist.inmem") //
				.addAsResource("persistence.xml", "META-INF/persistence.xml") //
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Test
	@Consumes(MediaType.APPLICATION_JSON)
	public void post_matrix_shoulCreateNewMatrix(@ArquillianResteasyResource("") final WebTarget webTarget) {
		final Response response = sendPostCreatingMatrix(webTarget, 6, 4);

		final int actualResponseStatusCode = response.getStatus();
		final URI actualResponseHeaderLocation = response.getLocation();
		assertThat(actualResponseStatusCode, is(equalTo(Response.Status.CREATED.getStatusCode())));
		assertThat(actualResponseHeaderLocation, is(notNullValue()));
	}

	private Response sendPostCreatingMatrix(final WebTarget webTarget, final int rowCount, final int columnCount) {
		final MatrixEntity entity = new MatrixEntity();
		entity.setRowCount(rowCount);
		entity.setColumnCount(columnCount);

		return webTarget.path("matrix").request(MediaType.APPLICATION_JSON).post(Entity.json(entity));
	}

	@Test
	@Consumes(MediaType.APPLICATION_JSON)
	public void get_matrix_shoulReturnOK(@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2) {
		final int expectedRowCount = 6;
		final int expectedColumnCount = 4;
		final Response postResponse = sendPostCreatingMatrix(webTarget1, expectedRowCount, expectedColumnCount);
		final String path = getWebTargetPath(postResponse);

		final Response getResponse = webTarget2.path(path).request(MediaType.APPLICATION_JSON).get();
		final MatrixEntity actualEntity = getResponse.readEntity(MatrixEntity.class);

		final int actualResponseStatusCode = getResponse.getStatus();
		assertThat(actualResponseStatusCode, is(equalTo(Response.Status.OK.getStatusCode())));
		assertThat(actualEntity.getRowCount(), is(equalTo(expectedRowCount)));
		assertThat(actualEntity.getColumnCount(), is(equalTo(expectedColumnCount)));
	}

	private String getWebTargetPath(final Response response) {
		final URI location = response.getLocation();
		final String locationPath = location.getPath();
		final String locationPathWithoutLeading = locationPath.substring(1);

		return locationPathWithoutLeading.substring(locationPathWithoutLeading.indexOf("/"));
	}

	@Test
	@Consumes(MediaType.APPLICATION_JSON)
	public void get_matrix_shoulNotFoundIfMatrixWithSuchIdDoesntExists(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2) {
		final Response postResponse = sendPostCreatingMatrix(webTarget1, 4, 6);
		final String path = getWebTargetPath(postResponse);
		final String newPath = path + "FOO";

		final Response getResponse = webTarget2.path(newPath).request(MediaType.APPLICATION_JSON).get();

		final int actualResponseStatusCode = getResponse.getStatus();
		assertThat(actualResponseStatusCode, is(equalTo(Response.Status.NOT_FOUND.getStatusCode())));
	}

	@Test
	@Consumes(MediaType.APPLICATION_JSON)
	public void delete_matrix_shoulReturnNoContentIfDeletingExistingMatrix(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2) {
		final Response postResponse = sendPostCreatingMatrix(webTarget1, 4, 6);
		final String path = getWebTargetPath(postResponse);

		final Response getResponse = webTarget2.path(path).request(MediaType.APPLICATION_JSON).delete();
		final int actualResponseStatusCode = getResponse.getStatus();
		assertThat(actualResponseStatusCode, is(equalTo(Response.Status.NO_CONTENT.getStatusCode())));
	}

	@Test
	@Consumes(MediaType.APPLICATION_JSON)
	public void delete_matrix_shoulReturnNotFoundIfDeletingNonExistingMatrix(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2) {
		final Response postResponse = sendPostCreatingMatrix(webTarget1, 4, 6);
		final String path = getWebTargetPath(postResponse);
		final String newPath = path + "FOO";

		final Response getResponse = webTarget2.path(newPath).request(MediaType.APPLICATION_JSON).delete();
		final int actualResponseStatusCode = getResponse.getStatus();
		assertThat(actualResponseStatusCode, is(equalTo(Response.Status.NOT_FOUND.getStatusCode())));
	}
}