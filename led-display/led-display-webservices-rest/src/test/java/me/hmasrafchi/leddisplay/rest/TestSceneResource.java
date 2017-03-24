/**
 * 
 */
package me.hmasrafchi.leddisplay.rest;

import static java.util.Arrays.asList;
import static me.hmasrafchi.leddisplay.rest.persist.LedState.OFF;
import static me.hmasrafchi.leddisplay.rest.persist.LedState.ON;
import static me.hmasrafchi.leddisplay.rest.persist.LedState.TRANSPARENT;
import static me.hmasrafchi.leddisplay.rest.persist.LedState.UNRECOGNIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.databind.JsonMappingException;

import me.hmasrafchi.leddisplay.rest.persist.LedRgbColor;
import me.hmasrafchi.leddisplay.rest.persist.LedStateRow;
import me.hmasrafchi.leddisplay.rest.persist.MatrixEntity;
import me.hmasrafchi.leddisplay.rest.persist.OverlayStationary;
import me.hmasrafchi.leddisplay.rest.persist.Scene;

/**
 * @author michelin
 *
 */
@RunWith(Arquillian.class)
public final class TestSceneResource extends TestResource {
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
	@Produces(MediaType.APPLICATION_JSON)
	public void post_matrices_scenes_stationary_shoulCreateNewSceneAndAddStationaryOverlayAsFirstElement(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2,
			@ArquillianResteasyResource("") final WebTarget webTarget3) throws JsonMappingException, IOException {
		final Response response = sendPostCreatingMatrix(webTarget1, 6, 4);
		final String webTargetPathMatrices = getWebTargetPath(response);
		final String webTargetPathScenesStationary = String.format("%s/%s/%s", webTargetPathMatrices, "scenes",
				"stationary");

		final List<LedStateRow> overlayStationaryStates = asList( //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON)), //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED, UNRECOGNIZED)));

		final OverlayStationary entity = new OverlayStationary(overlayStationaryStates, new LedRgbColor(255, 0, 0),
				new LedRgbColor(0, 255, 0), 10);

		final Response postResponse = webTarget2.path(webTargetPathScenesStationary).request(MediaType.APPLICATION_JSON)
				.post(Entity.json(entity));

		final int actualResponseStatusCode = postResponse.getStatus();
		final URI actualResponseHeaderLocation = postResponse.getLocation();
		assertThat(actualResponseStatusCode, is(equalTo(Response.Status.CREATED.getStatusCode())));
		assertThat(actualResponseHeaderLocation, is(notNullValue()));

		final Response getMatrixResponse = webTarget3.path(webTargetPathMatrices).request(MediaType.APPLICATION_JSON)
				.get();
		final MatrixEntity matrix = getMatrixResponse.readEntity(MatrixEntity.class);
		final List<Scene> scenes = matrix.getScenes();
		assertThat(scenes.size(), is(1));
		assertThat(scenes.get(0).getOverlays().size(), is(1));
	}

	@Test
	@Ignore
	@Consumes(MediaType.APPLICATION_JSON)
	public void post_matrices_scenes_stationary_ifCalledTwiceShoulCreateTwoScenesAndAddStationaryOverlayAsFirstElement(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2) {
		final Response response = sendPostCreatingMatrix(webTarget1, 6, 4);
		final String webTargetPathMatrices = getWebTargetPath(response);
		final String webTargetPathScenesStationary = String.format("%s/%s/%s", webTargetPathMatrices, "scenes",
				"stationary");

		final List<LedStateRow> overlayStationaryStates = asList( //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON)), //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED, UNRECOGNIZED)));

		final OverlayStationary entity = new OverlayStationary(overlayStationaryStates, new LedRgbColor(255, 0, 0),
				new LedRgbColor(0, 255, 0), 10);
		final Response postResponse = webTarget2.path(webTargetPathScenesStationary).request(MediaType.APPLICATION_JSON)
				.post(Entity.json(entity));

		final int actualResponseStatusCode = postResponse.getStatus();
		final URI actualResponseHeaderLocation = postResponse.getLocation();
		assertThat(actualResponseStatusCode, is(equalTo(Response.Status.CREATED.getStatusCode())));
		assertThat(actualResponseHeaderLocation, is(notNullValue()));

	}
}