/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application;

import static java.util.Arrays.asList;
import static javax.ws.rs.client.Entity.json;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static me.hmasrafchi.leddisplay.administration.model.view.LedState.OFF;
import static me.hmasrafchi.leddisplay.administration.model.view.LedState.ON;
import static me.hmasrafchi.leddisplay.administration.model.view.LedState.TRANSPARENT;
import static me.hmasrafchi.leddisplay.administration.model.view.LedState.UNRECOGNIZED;
import static me.hmasrafchi.leddisplay.administration.model.view.RgbColor.BLACK;
import static me.hmasrafchi.leddisplay.administration.model.view.RgbColor.BLUE;
import static me.hmasrafchi.leddisplay.administration.model.view.RgbColor.GREEN;
import static me.hmasrafchi.leddisplay.administration.model.view.RgbColor.RED;
import static me.hmasrafchi.leddisplay.administration.model.view.RgbColor.YELLOW;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import me.hmasrafchi.leddisplay.administration.model.view.CompiledFrames;
import me.hmasrafchi.leddisplay.administration.model.view.CreateMatrixCommand;
import me.hmasrafchi.leddisplay.administration.model.view.Led;
import me.hmasrafchi.leddisplay.administration.model.view.LedState;
import me.hmasrafchi.leddisplay.administration.model.view.Matrix;
import me.hmasrafchi.leddisplay.administration.model.view.OverlayRollHorizontally;
import me.hmasrafchi.leddisplay.administration.model.view.OverlayStationary;
import me.hmasrafchi.leddisplay.administration.model.view.RgbColor;

/**
 * @author michelin
 *
 */
@RunWith(Arquillian.class)
public final class TestMatrixResource {
	@Deployment(testable = false)
	public static WebArchive createDeployment() {
		return ShrinkWrap.create(WebArchive.class) //
				.addPackages(true, "me.hmasrafchi.leddisplay.administration") //
				.addPackages(true, "me.hmasrafchi.leddisplay.administration.application") //
				.addPackages(true, "me.hmasrafchi.leddisplay.administration.infrastructure") //
				.addPackages(true, "me.hmasrafchi.leddisplay.administration.model") //
				.addPackages(true, "me.hmasrafchi.leddisplay.util") //
				.addAsResource("persistence.xml", "META-INF/persistence.xml") //
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Test
	public void post_matrices_shouldReturnBadRequestIfRowCountIsNegative(
			@ArquillianResteasyResource("") final WebTarget webTarget) {
		final CreateMatrixCommand createMatrixCommand = new CreateMatrixCommand(-1, 6);
		final Response postMatrixResponse = webTarget.path("matrices").request(APPLICATION_JSON)
				.post(json(createMatrixCommand));

		final int actualResponseStatusCode = postMatrixResponse.getStatus();
		assertThat(actualResponseStatusCode, equalTo(Response.Status.BAD_REQUEST.getStatusCode()));
	}

	@Test
	public void post_matrices_shouldReturnBadRequestIfRowCountIsZero(
			@ArquillianResteasyResource("") final WebTarget webTarget) {
		final CreateMatrixCommand createMatrixCommand = new CreateMatrixCommand(0, 6);
		final Response postMatrixResponse = webTarget.path("matrices").request(APPLICATION_JSON)
				.post(json(createMatrixCommand));

		final int actualResponseStatusCode = postMatrixResponse.getStatus();
		assertThat(actualResponseStatusCode, equalTo(Response.Status.BAD_REQUEST.getStatusCode()));
	}

	@Test
	public void post_matrices_shouldReturnBadRequestIfColumnCountIsNegative(
			@ArquillianResteasyResource("") final WebTarget webTarget) {
		final CreateMatrixCommand createMatrixCommand = new CreateMatrixCommand(5, -1);
		final Response postMatrixResponse = webTarget.path("matrices").request(APPLICATION_JSON)
				.post(json(createMatrixCommand));

		final int actualResponseStatusCode = postMatrixResponse.getStatus();
		assertThat(actualResponseStatusCode, equalTo(Response.Status.BAD_REQUEST.getStatusCode()));
	}

	@Test
	public void post_matrices_shouldReturnBadRequestIfColumnCountIsZero(
			@ArquillianResteasyResource("") final WebTarget webTarget) {
		final CreateMatrixCommand createMatrixCommand = new CreateMatrixCommand(5, 0);
		final Response postMatrixResponse = webTarget.path("matrices").request(APPLICATION_JSON)
				.post(json(createMatrixCommand));

		final int actualResponseStatusCode = postMatrixResponse.getStatus();
		assertThat(actualResponseStatusCode, equalTo(Response.Status.BAD_REQUEST.getStatusCode()));
	}

	@Test
	public void post_matrices_shouldCreateNewMatrix(@ArquillianResteasyResource("") final WebTarget webTarget) {
		final Response postMatrixResponse = postCreateMatrixCommand(webTarget);

		final int actualResponseStatusCode = postMatrixResponse.getStatus();
		final URI actualResponseHeaderLocation = postMatrixResponse.getLocation();
		assertThat(actualResponseStatusCode, equalTo(Response.Status.CREATED.getStatusCode()));
		assertThat(actualResponseHeaderLocation, notNullValue());
	}

	@Test
	public void get_matrices_shouldReturnMatrixAndReturnOKStatus(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2) {
		final Response postMatrixResponse = postCreateMatrixCommand(webTarget1);
		final String newlyCreatedMatrixPath = getWebTargetPath(postMatrixResponse);

		final Response getMatrixResponse = webTarget2.path(newlyCreatedMatrixPath).request(APPLICATION_JSON).get();

		final Matrix actualMatrix = getMatrixResponse.readEntity(Matrix.class);
		final int actualResponseStatusCode = getMatrixResponse.getStatus();
		assertThat(actualResponseStatusCode, equalTo(Response.Status.OK.getStatusCode()));
		assertThat(actualMatrix, equalTo(actualMatrix));
	}

	@Test
	public void get_matrices_shouldReturnNotFoundStatus(@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2) {
		final Response postMatrixResponse = postCreateMatrixCommand(webTarget1);
		final String newlyCreatedMatrixPath = getWebTargetPath(postMatrixResponse);

		final Response getMatrixResponse = webTarget2.path(newlyCreatedMatrixPath + "FOO").request(APPLICATION_JSON)
				.get();

		final int actualResponseStatusCode = getMatrixResponse.getStatus();
		assertThat(actualResponseStatusCode, equalTo(Response.Status.NOT_FOUND.getStatusCode()));
	}

	@Test
	public void put_matrices_shouldCreateNewSceneWithOverlayAndReturnNoContentStatus(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2,
			@ArquillianResteasyResource("") final WebTarget webTarget3,
			@ArquillianResteasyResource("") final WebTarget webTarget4) {
		final Response postMatrixResponse = postCreateMatrixCommand(webTarget1);
		final String newlyCreatedMatrixPath = getWebTargetPath(postMatrixResponse);
		final Matrix expectedMatrix = webTarget2.path(newlyCreatedMatrixPath).request(APPLICATION_JSON).get()
				.readEntity(Matrix.class);

		final List<List<LedState>> overlayStationaryStates = asList( //
				asList(LedState.ON, LedState.ON, LedState.ON, LedState.ON, LedState.ON, LedState.ON, LedState.ON), //
				asList(LedState.ON, LedState.OFF, LedState.ON, LedState.TRANSPARENT, LedState.ON, LedState.TRANSPARENT,
						LedState.ON), //
				asList(LedState.ON, LedState.ON, LedState.ON, LedState.ON, LedState.ON, LedState.ON, LedState.ON), //
				asList(LedState.UNRECOGNIZED, LedState.UNRECOGNIZED, LedState.UNRECOGNIZED, LedState.UNRECOGNIZED,
						LedState.UNRECOGNIZED, LedState.UNRECOGNIZED, LedState.UNRECOGNIZED));
		final OverlayStationary overlay = new OverlayStationary(overlayStationaryStates, new RgbColor(255, 0, 0),
				new RgbColor(0, 255, 0), 10);
		expectedMatrix.appendNewSceneAndAppendOverlayToIt(overlay);

		final Response putMatrixResponse = webTarget3.path("matrices").request(APPLICATION_JSON)
				.put(json(expectedMatrix));

		final int actualStatus = putMatrixResponse.getStatus();
		assertThat(actualStatus, equalTo(Response.Status.NO_CONTENT.getStatusCode()));

		final Response getMatrixResponse2 = webTarget4.path(newlyCreatedMatrixPath).request(APPLICATION_JSON).get();
		final Matrix actualMatrix = getMatrixResponse2.readEntity(Matrix.class);

		assertThat(actualMatrix.getScenes().size(), equalTo(1));
		assertThat(actualMatrix, equalTo(expectedMatrix));
	}

	@Test
	public void put_matrices_shouldAppendNewSceneToAlreadyExistingScenesWithOverlayFromTheSameTypeAndReturnNoContentStatus(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2,
			@ArquillianResteasyResource("") final WebTarget webTarget3,
			@ArquillianResteasyResource("") final WebTarget webTarget4) {
		final List<List<LedState>> overlayStationaryStates = asList( //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON), //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED));
		final OverlayStationary overlayStationary1 = new OverlayStationary(overlayStationaryStates,
				new RgbColor(255, 0, 0), new RgbColor(0, 255, 0), 10);
		final CreateMatrixCommand createMatrixCommand = new CreateMatrixCommand(5, 6,
				asList(asList(overlayStationary1)));
		final Response postMatrixResponse = webTarget1.path("matrices").request(APPLICATION_JSON)
				.post(json(createMatrixCommand));
		final String newlyCreatedMatrixPath = getWebTargetPath(postMatrixResponse);
		final Matrix expectedMatrix = webTarget2.path(newlyCreatedMatrixPath).request(APPLICATION_JSON).get()
				.readEntity(Matrix.class);

		final List<List<LedState>> overlayStationaryStates2 = asList( //
				asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON), //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED));
		final OverlayStationary overlayStationary2 = new OverlayStationary(overlayStationaryStates2,
				new RgbColor(255, 0, 0), new RgbColor(0, 255, 0), 10);
		expectedMatrix.appendNewSceneAndAppendOverlayToIt(overlayStationary2);

		final Response putMatrixResponse = webTarget3.path("matrices").request(APPLICATION_JSON)
				.put(json(expectedMatrix));

		final int actualStatus = putMatrixResponse.getStatus();
		assertThat(actualStatus, equalTo(Response.Status.NO_CONTENT.getStatusCode()));

		final Response getMatrixResponse2 = webTarget4.path(newlyCreatedMatrixPath).request(APPLICATION_JSON).get();
		final Matrix actualMatrix = getMatrixResponse2.readEntity(Matrix.class);

		assertThat(actualMatrix.getScenes().size(), equalTo(2));
		assertThat(actualMatrix, equalTo(expectedMatrix));
	}

	@Test
	public void put_matrices_shouldAppendNewSceneToAlreadyExistingScenesWithOverlayFromDifferentTypeAndReturnNoContentStatus(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2,
			@ArquillianResteasyResource("") final WebTarget webTarget3,
			@ArquillianResteasyResource("") final WebTarget webTarget4) {
		final List<List<LedState>> overlayStationaryStates = asList( //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON), //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED));
		final OverlayStationary overlayStationary1 = new OverlayStationary(overlayStationaryStates,
				new RgbColor(255, 0, 0), new RgbColor(0, 255, 0), 10);
		final CreateMatrixCommand createMatrixCommand = new CreateMatrixCommand(5, 6,
				asList(asList(overlayStationary1)));
		final Response postMatrixResponse = webTarget1.path("matrices").request(APPLICATION_JSON)
				.post(json(createMatrixCommand));
		final String newlyCreatedMatrixPath = getWebTargetPath(postMatrixResponse);
		final Matrix expectedMatrix = webTarget2.path(newlyCreatedMatrixPath).request(APPLICATION_JSON).get()
				.readEntity(Matrix.class);

		final List<List<LedState>> overlayRollHorizontallyStates = asList( //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON), //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED));
		final OverlayRollHorizontally overlayRollHorizontally = new OverlayRollHorizontally(
				overlayRollHorizontallyStates, new RgbColor(255, 0, 0), new RgbColor(0, 255, 0), 10, 1);
		expectedMatrix.appendNewSceneAndAppendOverlayToIt(overlayRollHorizontally);

		final Response putMatrixResponse = webTarget3.path("matrices").request(APPLICATION_JSON)
				.put(json(expectedMatrix));

		final int actualStatus = putMatrixResponse.getStatus();
		assertThat(actualStatus, equalTo(Response.Status.NO_CONTENT.getStatusCode()));

		final Response getMatrixResponse2 = webTarget4.path(newlyCreatedMatrixPath).request(APPLICATION_JSON).get();
		final Matrix actualMatrix = getMatrixResponse2.readEntity(Matrix.class);

		assertThat(actualMatrix.getScenes().size(), equalTo(2));
		assertThat(actualMatrix, equalTo(expectedMatrix));
	}

	@Test
	public void put_matrices_shouldAppendOverlayWithTheSameTypeToSceneWithOneOverlayAndReturnNoContentStatus(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2,
			@ArquillianResteasyResource("") final WebTarget webTarget3,
			@ArquillianResteasyResource("") final WebTarget webTarget4) {
		final List<List<LedState>> overlayStationaryStates = asList( //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON), //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED));
		final OverlayStationary overlayStationary1 = new OverlayStationary(overlayStationaryStates,
				new RgbColor(255, 0, 0), new RgbColor(0, 255, 0), 10);
		final CreateMatrixCommand createMatrixCommand = new CreateMatrixCommand(5, 6,
				asList(asList(overlayStationary1)));
		final Response postMatrixResponse = webTarget1.path("matrices").request(APPLICATION_JSON)
				.post(json(createMatrixCommand));
		final String newlyCreatedMatrixPath = getWebTargetPath(postMatrixResponse);
		final Matrix expectedMatrix = webTarget2.path(newlyCreatedMatrixPath).request(APPLICATION_JSON).get()
				.readEntity(Matrix.class);

		final List<List<LedState>> overlayStationaryStates2 = asList( //
				asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON), //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED));
		final OverlayStationary overlayStationary2 = new OverlayStationary(overlayStationaryStates2,
				new RgbColor(255, 0, 0), new RgbColor(0, 255, 0), 10);
		expectedMatrix.appendNewOverlayToScene(0, overlayStationary2);

		final Response putMatrixResponse = webTarget3.path("matrices").request(APPLICATION_JSON)
				.put(json(expectedMatrix));
		final int actualStatus = putMatrixResponse.getStatus();
		assertThat(actualStatus, equalTo(Response.Status.NO_CONTENT.getStatusCode()));

		final Response getMatrixResponse2 = webTarget4.path(newlyCreatedMatrixPath).request(APPLICATION_JSON).get();
		final Matrix actualMatrix = getMatrixResponse2.readEntity(Matrix.class);

		assertThat(actualMatrix.getScenes().size(), equalTo(1));
		assertThat(actualMatrix.getScenes().get(0).size(), equalTo(2));
		assertThat(actualMatrix, equalTo(expectedMatrix));
	}

	@Test
	public void put_matrices_shouldAppendOverlayWithDifferentTypeToSceneWithOneOverlayAndReturnNoContentStatus(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2,
			@ArquillianResteasyResource("") final WebTarget webTarget3,
			@ArquillianResteasyResource("") final WebTarget webTarget4) {
		final List<List<LedState>> overlayStationaryStates = asList( //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON), //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED));
		final OverlayStationary overlayStationary1 = new OverlayStationary(overlayStationaryStates,
				new RgbColor(255, 0, 0), new RgbColor(0, 255, 0), 10);
		final CreateMatrixCommand createMatrixCommand = new CreateMatrixCommand(5, 6,
				asList(asList(overlayStationary1)));
		final Response postMatrixResponse = webTarget1.path("matrices").request(APPLICATION_JSON)
				.post(json(createMatrixCommand));
		final String newlyCreatedMatrixPath = getWebTargetPath(postMatrixResponse);
		final Matrix expectedMatrix = webTarget2.path(newlyCreatedMatrixPath).request(APPLICATION_JSON).get()
				.readEntity(Matrix.class);

		final List<List<LedState>> overlayRollHorizontallyStates = asList( //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON), //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED));
		final OverlayRollHorizontally overlayRollHorizontally = new OverlayRollHorizontally(
				overlayRollHorizontallyStates, new RgbColor(255, 0, 0), new RgbColor(0, 255, 0), 10, 1);
		expectedMatrix.appendNewOverlayToScene(0, overlayRollHorizontally);

		final Response putMatrixResponse = webTarget3.path("matrices").request(APPLICATION_JSON)
				.put(json(expectedMatrix));
		final int actualStatus = putMatrixResponse.getStatus();
		assertThat(actualStatus, equalTo(Response.Status.NO_CONTENT.getStatusCode()));

		final Response getMatrixResponse2 = webTarget4.path(newlyCreatedMatrixPath).request(APPLICATION_JSON).get();
		final Matrix actualMatrix = getMatrixResponse2.readEntity(Matrix.class);

		assertThat(actualMatrix.getScenes().size(), equalTo(1));
		assertThat(actualMatrix.getScenes().get(0).size(), equalTo(2));
		assertThat(actualMatrix, equalTo(expectedMatrix));
	}

	private Response postCreateMatrixCommand(final WebTarget webTarget) {
		final CreateMatrixCommand createMatrixCommand = new CreateMatrixCommand(5, 6);
		return webTarget.path("matrices").request(APPLICATION_JSON).post(json(createMatrixCommand));
	}

	private String getWebTargetPath(final Response response) {
		final URI location = response.getLocation();
		final String locationPath = location.getPath();
		final String locationPathWithoutLeading = locationPath.substring(1);

		return locationPathWithoutLeading.substring(locationPathWithoutLeading.indexOf("/") + 1);
	}

	@Test
	public void get_compiledFrames_shoulReturnNotFoundIfNoScenesArePresent(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2) {
		final Response postResponse = postCreateMatrixCommand(webTarget1);

		final String path = getWebTargetPath(postResponse) + "/compiled_frames";
		final Response getResponse = webTarget2.path(path).request(MediaType.APPLICATION_JSON).get();

		final int actualResponseStatusCode = getResponse.getStatus();
		assertThat(actualResponseStatusCode, equalTo(Response.Status.NOT_FOUND.getStatusCode()));
	}

	@Test
	public void get_compiledFrames_shoulReturnCompiledFramesForTwoDifferentTypeOfScenes(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2,
			@ArquillianResteasyResource("") final WebTarget webTarget3,
			@ArquillianResteasyResource("") final WebTarget webTarget4) {
		final List<List<LedState>> overlayRollHorizontallyStates1 = asList( //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON), //
				asList(ON, ON, ON, ON, ON, ON, ON));
		final OverlayRollHorizontally overlayRollHorizontallyToPost1 = new OverlayRollHorizontally(
				overlayRollHorizontallyStates1, RgbColor.GREEN, RgbColor.BLUE, 5, 0);

		final List<List<LedState>> overlayRollHorizontallyStates2 = asList( //
				asList(ON, ON, ON), //
				asList(ON, OFF, ON), //
				asList(ON, ON, ON));
		final OverlayRollHorizontally overlayRollHorizontallyToPost2 = new OverlayRollHorizontally(
				overlayRollHorizontallyStates2, RgbColor.GREEN, RgbColor.BLUE, 5, 1);

		final CreateMatrixCommand createMatrixCommand = new CreateMatrixCommand(6, 5,
				asList(asList(overlayRollHorizontallyToPost1), asList(overlayRollHorizontallyToPost2)));
		final Response postMatrixResponse = webTarget1.path("matrices").request(APPLICATION_JSON)
				.post(json(createMatrixCommand));
		final String newlyCreatedMatrixPath = getWebTargetPath(postMatrixResponse);

		final String path = newlyCreatedMatrixPath + "/compiled_frames";
		final Response getResponse = webTarget2.path(path).request(MediaType.APPLICATION_JSON).get();

		final int actualResponseStatusCode = getResponse.getStatus();
		assertThat(actualResponseStatusCode, equalTo(Response.Status.OK.getStatusCode()));

		final CompiledFrames actualCompiledFrames = getResponse.readEntity(CompiledFrames.class);

		// scene 1
		final List<List<Led>> frame1 = new ArrayList<>(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final List<List<Led>> frame2 = new ArrayList<>(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final List<List<Led>> frame3 = new ArrayList<>(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(BLUE)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final List<List<Led>> frame4 = new ArrayList<>(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(BLUE), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final List<List<Led>> frame5 = new ArrayList<>(asList( //
				asList(new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(GREEN), new Led(BLUE), new Led(GREEN), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final List<List<Led>> frame6 = new ArrayList<>(asList( //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(GREEN), new Led(BLUE), new Led(GREEN), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final List<List<Led>> frame7 = new ArrayList<>(asList( //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLUE), new Led(GREEN), new Led(BLACK), new Led(GREEN), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final List<List<Led>> frame8 = new ArrayList<>(asList( //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(GREEN), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final List<List<Led>> frame9 = new ArrayList<>(asList( //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(GREEN), new Led(BLACK), new Led(GREEN), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final List<List<Led>> frame10 = new ArrayList<>(asList( //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(GREEN), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final List<List<Led>> frame11 = new ArrayList<>(asList( //
				asList(new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final List<List<Led>> frame12 = new ArrayList<>(asList( //
				asList(new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final List<List<Led>> frame13 = new ArrayList<>(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		// scene 2
		final List<List<Led>> frame14 = new ArrayList<>(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final List<List<Led>> frame15 = new ArrayList<>(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final List<List<Led>> frame16 = new ArrayList<>(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(BLUE)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final List<List<Led>> frame17 = new ArrayList<>(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(BLUE), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final List<List<Led>> frame18 = new ArrayList<>(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(GREEN), new Led(BLUE), new Led(GREEN), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final List<List<Led>> frame19 = new ArrayList<>(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(BLUE), new Led(GREEN), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final List<List<Led>> frame20 = new ArrayList<>(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLUE), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final List<List<Led>> frame21 = new ArrayList<>(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final List<List<Led>> frame22 = new ArrayList<>(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final CompiledFrames expectedCompiledFrames = new CompiledFrames(asList(frame1, frame2, frame3, frame4, frame5,
				frame6, frame7, frame8, frame9, frame10, frame11, frame12, frame13, frame14, frame15, frame16, frame17,
				frame18, frame19, frame20, frame21, frame22));

		assertThat(actualCompiledFrames, equalTo(expectedCompiledFrames));
	}

	@Test
	public void get_compiledFrames_shoulReturnCompiledFramesForTwoDifferentTypeScenesFromSceneOverlayed(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2,
			@ArquillianResteasyResource("") final WebTarget webTarget3,
			@ArquillianResteasyResource("") final WebTarget webTarget4,
			@ArquillianResteasyResource("") final WebTarget webTarget5) {
		final List<List<LedState>> overlayRollHorizontallyStates = asList( //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON), //
				asList(ON, ON, ON, ON, ON, ON, ON));
		final OverlayRollHorizontally overlayRollHorizontallyToPost = new OverlayRollHorizontally(
				overlayRollHorizontallyStates, RgbColor.GREEN, RgbColor.BLUE, 5, 1);

		final List<List<LedState>> overlayStationaryStates = asList( //
				asList(ON, ON, ON, ON, ON), //
				asList(TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT), //
				asList(ON, TRANSPARENT, TRANSPARENT, TRANSPARENT, ON), //
				asList(OFF, TRANSPARENT, TRANSPARENT, TRANSPARENT, OFF), //
				asList(ON, TRANSPARENT, TRANSPARENT, TRANSPARENT, ON), //
				asList(ON, ON, ON, ON, ON));
		final OverlayStationary overlayStationaryToPost = new OverlayStationary(overlayStationaryStates, RgbColor.RED,
				RgbColor.YELLOW, 1);

		final CreateMatrixCommand createMatrixCommand = new CreateMatrixCommand(6, 5,
				asList(asList(overlayRollHorizontallyToPost, overlayStationaryToPost)));
		final Response postMatrixResponse = webTarget1.path("matrices").request(APPLICATION_JSON)
				.post(json(createMatrixCommand));
		final String newlyCreatedMatrixPath = getWebTargetPath(postMatrixResponse);

		final String path = newlyCreatedMatrixPath + "/compiled_frames";
		final Response getResponse = webTarget2.path(path).request(MediaType.APPLICATION_JSON).get();

		final int actualResponseStatusCode = getResponse.getStatus();
		assertThat(actualResponseStatusCode, equalTo(Response.Status.OK.getStatusCode()));

		final CompiledFrames actualCompiledFrames = getResponse.readEntity(CompiledFrames.class);

		// 0nd frame
		final List<List<Led>> frame0 = new ArrayList<>(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(YELLOW), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 1nd frame
		final List<List<Led>> frame1 = new ArrayList<>(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(YELLOW), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 2rd frame
		final List<List<Led>> frame2 = new ArrayList<>(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(RED)), //
				asList(new Led(YELLOW), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 3th frame
		final List<List<Led>> frame3 = new ArrayList<>(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(BLACK), new Led(GREEN), new Led(BLUE), new Led(RED)), //
				asList(new Led(YELLOW), new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 4th frame
		final List<List<Led>> frame4 = new ArrayList<>(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(GREEN), new Led(BLUE), new Led(GREEN), new Led(RED)), //
				asList(new Led(YELLOW), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 5th frame
		final List<List<Led>> frame5 = new ArrayList<>(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(BLUE), new Led(GREEN), new Led(BLACK), new Led(RED)), //
				asList(new Led(YELLOW), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 6th frame
		final List<List<Led>> frame6 = new ArrayList<>(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(GREEN), new Led(BLACK), new Led(GREEN), new Led(RED)), //
				asList(new Led(YELLOW), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 7th frame
		final List<List<Led>> frame7 = new ArrayList<>(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(BLACK), new Led(GREEN), new Led(BLACK), new Led(RED)), //
				asList(new Led(YELLOW), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 8th frame
		final List<List<Led>> frame8 = new ArrayList<>(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK)), //
				asList(new Led(RED), new Led(GREEN), new Led(BLACK), new Led(GREEN), new Led(RED)), //
				asList(new Led(YELLOW), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 9th frame
		final List<List<Led>> frame9 = new ArrayList<>(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(RED), new Led(BLACK), new Led(GREEN), new Led(BLACK), new Led(RED)), //
				asList(new Led(YELLOW), new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 10th frame
		final List<List<Led>> frame10 = new ArrayList<>(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(RED), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(YELLOW), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 11th frame
		final List<List<Led>> frame11 = new ArrayList<>(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(YELLOW), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 12th frame
		final List<List<Led>> frame12 = new ArrayList<>(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(YELLOW), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));
		final CompiledFrames expectedCompiledFrames = new CompiledFrames(asList(frame0, frame1, frame2, frame3, frame4,
				frame5, frame6, frame7, frame8, frame9, frame10, frame11, frame12));

		assertThat(actualCompiledFrames, equalTo(expectedCompiledFrames));
	}
}