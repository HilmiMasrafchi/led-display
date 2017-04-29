/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application;

import static java.util.Arrays.asList;
import static javax.ws.rs.client.Entity.json;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static me.hmasrafchi.leddisplay.administration.model.view.LedStateView.OFF;
import static me.hmasrafchi.leddisplay.administration.model.view.LedStateView.ON;
import static me.hmasrafchi.leddisplay.administration.model.view.LedStateView.TRANSPARENT;
import static me.hmasrafchi.leddisplay.administration.model.view.LedStateView.UNRECOGNIZED;
import static me.hmasrafchi.leddisplay.administration.model.view.RgbColorView.BLACK;
import static me.hmasrafchi.leddisplay.administration.model.view.RgbColorView.BLUE;
import static me.hmasrafchi.leddisplay.administration.model.view.RgbColorView.GREEN;
import static me.hmasrafchi.leddisplay.administration.model.view.RgbColorView.RED;
import static me.hmasrafchi.leddisplay.administration.model.view.RgbColorView.YELLOW;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
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

import me.hmasrafchi.leddisplay.administration.model.view.CreateMatrixCommand;
import me.hmasrafchi.leddisplay.administration.model.view.LedStateView;
import me.hmasrafchi.leddisplay.administration.model.view.LedView;
import me.hmasrafchi.leddisplay.administration.model.view.MatrixView;
import me.hmasrafchi.leddisplay.administration.model.view.OverlayRollHorizontallyView;
import me.hmasrafchi.leddisplay.administration.model.view.OverlayStationaryView;
import me.hmasrafchi.leddisplay.administration.model.view.RgbColorView;

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
				.addPackages(true, "me.hmasrafchi.leddisplay.domain") //
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

		final MatrixView actualMatrix = getMatrixResponse.readEntity(MatrixView.class);
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
		// TODO: the test is not as described in the title
		final Response postMatrixResponse = postCreateMatrixCommand(webTarget1);
		final String newlyCreatedMatrixPath = getWebTargetPath(postMatrixResponse);
		final MatrixView expectedMatrix = webTarget2.path(newlyCreatedMatrixPath).request(APPLICATION_JSON).get()
				.readEntity(MatrixView.class);

		final List<List<LedStateView>> overlayStationaryStates = asList( //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON), //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED));
		final OverlayStationaryView overlay = new OverlayStationaryView(overlayStationaryStates,
				new RgbColorView(255, 0, 0), new RgbColorView(0, 255, 0), 10);
		expectedMatrix.appendNewSceneAndAppendOverlayToIt(overlay);

		final Response putMatrixResponse = webTarget3.path("matrices").request(APPLICATION_JSON)
				.put(json(expectedMatrix));

		final int actualStatus = putMatrixResponse.getStatus();
		assertThat(actualStatus, equalTo(Response.Status.NO_CONTENT.getStatusCode()));

		final Response getMatrixResponse2 = webTarget4.path(newlyCreatedMatrixPath).request(APPLICATION_JSON).get();
		final MatrixView actualMatrix = getMatrixResponse2.readEntity(MatrixView.class);

		assertThat(actualMatrix.getScenes().size(), equalTo(1));
		assertThat(actualMatrix, equalTo(expectedMatrix));
	}

	@Test
	public void put_matrices_shouldAppendNewSceneToAlreadyExistingScenesWithOverlayFromTheSameTypeAndReturnNoContentStatus(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2,
			@ArquillianResteasyResource("") final WebTarget webTarget3,
			@ArquillianResteasyResource("") final WebTarget webTarget4) {
		final List<List<LedStateView>> overlayStationaryStates = asList( //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON), //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED));
		final OverlayStationaryView overlayStationary1 = new OverlayStationaryView(overlayStationaryStates,
				new RgbColorView(255, 0, 0), new RgbColorView(0, 255, 0), 10);
		final CreateMatrixCommand createMatrixCommand = new CreateMatrixCommand("", 5, 6,
				asList(asList(overlayStationary1)));
		final Response postMatrixResponse = webTarget1.path("matrices").request(APPLICATION_JSON)
				.post(json(createMatrixCommand));
		final String newlyCreatedMatrixPath = getWebTargetPath(postMatrixResponse);
		final MatrixView expectedMatrix = webTarget2.path(newlyCreatedMatrixPath).request(APPLICATION_JSON).get()
				.readEntity(MatrixView.class);

		final List<List<LedStateView>> overlayStationaryStates2 = asList( //
				asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON), //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED));
		final OverlayStationaryView overlayStationary2 = new OverlayStationaryView(overlayStationaryStates2,
				new RgbColorView(255, 0, 0), new RgbColorView(0, 255, 0), 10);
		expectedMatrix.appendNewSceneAndAppendOverlayToIt(overlayStationary2);

		final Response putMatrixResponse = webTarget3.path("matrices").request(APPLICATION_JSON)
				.put(json(expectedMatrix));

		final int actualStatus = putMatrixResponse.getStatus();
		assertThat(actualStatus, equalTo(Response.Status.NO_CONTENT.getStatusCode()));

		final Response getMatrixResponse2 = webTarget4.path(newlyCreatedMatrixPath).request(APPLICATION_JSON).get();
		final MatrixView actualMatrix = getMatrixResponse2.readEntity(MatrixView.class);

		assertThat(actualMatrix.getScenes().size(), equalTo(2));
		assertThat(actualMatrix, equalTo(expectedMatrix));
	}

	@Test
	public void put_matrices_shouldAppendNewSceneToAlreadyExistingScenesWithOverlayFromDifferentTypeAndReturnNoContentStatus(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2,
			@ArquillianResteasyResource("") final WebTarget webTarget3,
			@ArquillianResteasyResource("") final WebTarget webTarget4) {
		final List<List<LedStateView>> overlayStationaryStates = asList( //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON), //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED));
		final OverlayStationaryView overlayStationary1 = new OverlayStationaryView(overlayStationaryStates,
				new RgbColorView(255, 0, 0), new RgbColorView(0, 255, 0), 10);
		final CreateMatrixCommand createMatrixCommand = new CreateMatrixCommand("", 5, 6,
				asList(asList(overlayStationary1)));
		final Response postMatrixResponse = webTarget1.path("matrices").request(APPLICATION_JSON)
				.post(json(createMatrixCommand));
		final String newlyCreatedMatrixPath = getWebTargetPath(postMatrixResponse);
		final MatrixView expectedMatrix = webTarget2.path(newlyCreatedMatrixPath).request(APPLICATION_JSON).get()
				.readEntity(MatrixView.class);

		final List<List<LedStateView>> overlayRollHorizontallyStates = asList( //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON), //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED));
		final OverlayRollHorizontallyView overlayRollHorizontally = new OverlayRollHorizontallyView(
				overlayRollHorizontallyStates, new RgbColorView(255, 0, 0), new RgbColorView(0, 255, 0), 10, 1);
		expectedMatrix.appendNewSceneAndAppendOverlayToIt(overlayRollHorizontally);

		final Response putMatrixResponse = webTarget3.path("matrices").request(APPLICATION_JSON)
				.put(json(expectedMatrix));

		final int actualStatus = putMatrixResponse.getStatus();
		assertThat(actualStatus, equalTo(Response.Status.NO_CONTENT.getStatusCode()));

		final Response getMatrixResponse2 = webTarget4.path(newlyCreatedMatrixPath).request(APPLICATION_JSON).get();
		final MatrixView actualMatrix = getMatrixResponse2.readEntity(MatrixView.class);

		assertThat(actualMatrix.getScenes().size(), equalTo(2));
		assertThat(actualMatrix, equalTo(expectedMatrix));
	}

	@Test
	public void put_matrices_shouldAppendOverlayWithTheSameTypeToSceneWithOneOverlayAndReturnNoContentStatus(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2,
			@ArquillianResteasyResource("") final WebTarget webTarget3,
			@ArquillianResteasyResource("") final WebTarget webTarget4) {
		final List<List<LedStateView>> overlayStationaryStates = asList( //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON), //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED));
		final OverlayStationaryView overlayStationary1 = new OverlayStationaryView(overlayStationaryStates,
				new RgbColorView(255, 0, 0), new RgbColorView(0, 255, 0), 10);
		final CreateMatrixCommand createMatrixCommand = new CreateMatrixCommand("", 5, 6,
				asList(asList(overlayStationary1)));
		final Response postMatrixResponse = webTarget1.path("matrices").request(APPLICATION_JSON)
				.post(json(createMatrixCommand));
		final String newlyCreatedMatrixPath = getWebTargetPath(postMatrixResponse);
		final MatrixView expectedMatrix = webTarget2.path(newlyCreatedMatrixPath).request(APPLICATION_JSON).get()
				.readEntity(MatrixView.class);

		final List<List<LedStateView>> overlayStationaryStates2 = asList( //
				asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON), //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED));
		final OverlayStationaryView overlayStationary2 = new OverlayStationaryView(overlayStationaryStates2,
				new RgbColorView(255, 0, 0), new RgbColorView(0, 255, 0), 10);
		expectedMatrix.appendNewOverlayToScene(0, overlayStationary2);

		final Response putMatrixResponse = webTarget3.path("matrices").request(APPLICATION_JSON)
				.put(json(expectedMatrix));
		final int actualStatus = putMatrixResponse.getStatus();
		assertThat(actualStatus, equalTo(Response.Status.NO_CONTENT.getStatusCode()));

		final Response getMatrixResponse2 = webTarget4.path(newlyCreatedMatrixPath).request(APPLICATION_JSON).get();
		final MatrixView actualMatrix = getMatrixResponse2.readEntity(MatrixView.class);

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
		final List<List<LedStateView>> overlayStationaryStates = asList( //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON), //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED));
		final OverlayStationaryView overlayStationary1 = new OverlayStationaryView(overlayStationaryStates,
				new RgbColorView(255, 0, 0), new RgbColorView(0, 255, 0), 10);
		final CreateMatrixCommand createMatrixCommand = new CreateMatrixCommand("", 5, 6,
				asList(asList(overlayStationary1)));
		final Response postMatrixResponse = webTarget1.path("matrices").request(APPLICATION_JSON)
				.post(json(createMatrixCommand));
		final String newlyCreatedMatrixPath = getWebTargetPath(postMatrixResponse);
		final MatrixView expectedMatrix = webTarget2.path(newlyCreatedMatrixPath).request(APPLICATION_JSON).get()
				.readEntity(MatrixView.class);

		final List<List<LedStateView>> overlayRollHorizontallyStates = asList( //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON), //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED));
		final OverlayRollHorizontallyView overlayRollHorizontally = new OverlayRollHorizontallyView(
				overlayRollHorizontallyStates, new RgbColorView(255, 0, 0), new RgbColorView(0, 255, 0), 10, 1);
		expectedMatrix.appendNewOverlayToScene(0, overlayRollHorizontally);

		final Response putMatrixResponse = webTarget3.path("matrices").request(APPLICATION_JSON)
				.put(json(expectedMatrix));
		final int actualStatus = putMatrixResponse.getStatus();
		assertThat(actualStatus, equalTo(Response.Status.NO_CONTENT.getStatusCode()));

		final Response getMatrixResponse2 = webTarget4.path(newlyCreatedMatrixPath).request(APPLICATION_JSON).get();
		final MatrixView actualMatrix = getMatrixResponse2.readEntity(MatrixView.class);

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
		final List<List<LedStateView>> overlayRollHorizontallyStates1 = asList( //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON), //
				asList(ON, ON, ON, ON, ON, ON, ON));
		final OverlayRollHorizontallyView overlayRollHorizontallyToPost1 = new OverlayRollHorizontallyView(
				overlayRollHorizontallyStates1, RgbColorView.GREEN, RgbColorView.BLUE, 5, 0);

		final List<List<LedStateView>> overlayRollHorizontallyStates2 = asList( //
				asList(ON, ON, ON), //
				asList(ON, OFF, ON), //
				asList(ON, ON, ON));
		final OverlayRollHorizontallyView overlayRollHorizontallyToPost2 = new OverlayRollHorizontallyView(
				overlayRollHorizontallyStates2, RgbColorView.GREEN, RgbColorView.BLUE, 5, 1);

		final CreateMatrixCommand createMatrixCommand = new CreateMatrixCommand("", 6, 5,
				asList(asList(overlayRollHorizontallyToPost1), asList(overlayRollHorizontallyToPost2)));
		final Response postMatrixResponse = webTarget1.path("matrices").request(APPLICATION_JSON)
				.post(json(createMatrixCommand));
		final String newlyCreatedMatrixPath = getWebTargetPath(postMatrixResponse);

		final Response getResponse = webTarget2.path(newlyCreatedMatrixPath).request(MediaType.APPLICATION_JSON).get();

		final int actualResponseStatusCode = getResponse.getStatus();
		assertThat(actualResponseStatusCode, equalTo(Response.Status.OK.getStatusCode()));

		final List<List<List<LedView>>> actualCompiledFrames = getResponse.readEntity(MatrixView.class)
				.getCompiledFrames();

		// scene 1
		final List<List<LedView>> frame1 = new ArrayList<>(asList( //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK))));

		final List<List<LedView>> frame2 = new ArrayList<>(asList( //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(GREEN)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(GREEN)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(GREEN)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK))));

		final List<List<LedView>> frame3 = new ArrayList<>(asList( //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(GREEN),
						new LedView(GREEN)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(GREEN),
						new LedView(BLUE)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(GREEN),
						new LedView(GREEN)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK))));

		final List<List<LedView>> frame4 = new ArrayList<>(asList( //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(GREEN), new LedView(GREEN),
						new LedView(GREEN)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(GREEN), new LedView(BLUE),
						new LedView(GREEN)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(GREEN), new LedView(GREEN),
						new LedView(GREEN)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK))));

		final List<List<LedView>> frame5 = new ArrayList<>(asList( //
				asList(new LedView(BLACK), new LedView(GREEN), new LedView(GREEN), new LedView(GREEN),
						new LedView(GREEN)), //
				asList(new LedView(BLACK), new LedView(GREEN), new LedView(BLUE), new LedView(GREEN),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(GREEN), new LedView(GREEN), new LedView(GREEN),
						new LedView(GREEN)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK))));

		final List<List<LedView>> frame6 = new ArrayList<>(asList( //
				asList(new LedView(GREEN), new LedView(GREEN), new LedView(GREEN), new LedView(GREEN),
						new LedView(GREEN)), //
				asList(new LedView(GREEN), new LedView(BLUE), new LedView(GREEN), new LedView(BLACK),
						new LedView(GREEN)), //
				asList(new LedView(GREEN), new LedView(GREEN), new LedView(GREEN), new LedView(GREEN),
						new LedView(GREEN)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK))));

		final List<List<LedView>> frame7 = new ArrayList<>(asList( //
				asList(new LedView(GREEN), new LedView(GREEN), new LedView(GREEN), new LedView(GREEN),
						new LedView(GREEN)), //
				asList(new LedView(BLUE), new LedView(GREEN), new LedView(BLACK), new LedView(GREEN),
						new LedView(BLACK)), //
				asList(new LedView(GREEN), new LedView(GREEN), new LedView(GREEN), new LedView(GREEN),
						new LedView(GREEN)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK))));

		final List<List<LedView>> frame8 = new ArrayList<>(asList( //
				asList(new LedView(GREEN), new LedView(GREEN), new LedView(GREEN), new LedView(GREEN),
						new LedView(GREEN)), //
				asList(new LedView(GREEN), new LedView(BLACK), new LedView(GREEN), new LedView(BLACK),
						new LedView(GREEN)), //
				asList(new LedView(GREEN), new LedView(GREEN), new LedView(GREEN), new LedView(GREEN),
						new LedView(GREEN)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK))));

		final List<List<LedView>> frame9 = new ArrayList<>(asList( //
				asList(new LedView(GREEN), new LedView(GREEN), new LedView(GREEN), new LedView(GREEN),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(GREEN), new LedView(BLACK), new LedView(GREEN),
						new LedView(BLACK)), //
				asList(new LedView(GREEN), new LedView(GREEN), new LedView(GREEN), new LedView(GREEN),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK))));

		final List<List<LedView>> frame10 = new ArrayList<>(asList( //
				asList(new LedView(GREEN), new LedView(GREEN), new LedView(GREEN), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(GREEN), new LedView(BLACK), new LedView(GREEN), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(GREEN), new LedView(GREEN), new LedView(GREEN), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK))));

		final List<List<LedView>> frame11 = new ArrayList<>(asList( //
				asList(new LedView(GREEN), new LedView(GREEN), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(GREEN), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(GREEN), new LedView(GREEN), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK))));

		final List<List<LedView>> frame12 = new ArrayList<>(asList( //
				asList(new LedView(GREEN), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(GREEN), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(GREEN), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK))));

		final List<List<LedView>> frame13 = new ArrayList<>(asList( //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK))));

		// scene 2
		final List<List<LedView>> frame14 = new ArrayList<>(asList( //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK))));

		final List<List<LedView>> frame15 = new ArrayList<>(asList( //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(GREEN)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(GREEN)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(GREEN)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK))));

		final List<List<LedView>> frame16 = new ArrayList<>(asList( //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(GREEN),
						new LedView(GREEN)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(GREEN),
						new LedView(BLUE)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(GREEN),
						new LedView(GREEN)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK))));

		final List<List<LedView>> frame17 = new ArrayList<>(asList( //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(GREEN), new LedView(GREEN),
						new LedView(GREEN)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(GREEN), new LedView(BLUE),
						new LedView(GREEN)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(GREEN), new LedView(GREEN),
						new LedView(GREEN)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK))));

		final List<List<LedView>> frame18 = new ArrayList<>(asList( //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(GREEN), new LedView(GREEN), new LedView(GREEN),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(GREEN), new LedView(BLUE), new LedView(GREEN),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(GREEN), new LedView(GREEN), new LedView(GREEN),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK))));

		final List<List<LedView>> frame19 = new ArrayList<>(asList( //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(GREEN), new LedView(GREEN), new LedView(GREEN), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(GREEN), new LedView(BLUE), new LedView(GREEN), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(GREEN), new LedView(GREEN), new LedView(GREEN), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK))));

		final List<List<LedView>> frame20 = new ArrayList<>(asList( //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(GREEN), new LedView(GREEN), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLUE), new LedView(GREEN), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(GREEN), new LedView(GREEN), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK))));

		final List<List<LedView>> frame21 = new ArrayList<>(asList( //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(GREEN), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(GREEN), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(GREEN), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK))));

		final List<List<LedView>> frame22 = new ArrayList<>(asList( //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK))));

		final List<List<List<LedView>>> expectedCompiledFrames = asList(frame1, frame2, frame3, frame4, frame5, frame6,
				frame7, frame8, frame9, frame10, frame11, frame12, frame13, frame14, frame15, frame16, frame17, frame18,
				frame19, frame20, frame21, frame22);

		assertThat(actualCompiledFrames, equalTo(expectedCompiledFrames));
	}

	@Test
	public void get_compiledFrames_shoulReturnCompiledFramesForTwoDifferentTypeScenesFromSceneOverlayed(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2,
			@ArquillianResteasyResource("") final WebTarget webTarget3,
			@ArquillianResteasyResource("") final WebTarget webTarget4,
			@ArquillianResteasyResource("") final WebTarget webTarget5) {
		final List<List<LedStateView>> overlayRollHorizontallyStates = asList( //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON), //
				asList(ON, ON, ON, ON, ON, ON, ON));
		final OverlayRollHorizontallyView overlayRollHorizontallyToPost = new OverlayRollHorizontallyView(
				overlayRollHorizontallyStates, RgbColorView.GREEN, RgbColorView.BLUE, 5, 1);

		final List<List<LedStateView>> overlayStationaryStates = asList( //
				asList(ON, ON, ON, ON, ON), //
				asList(TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT), //
				asList(ON, TRANSPARENT, TRANSPARENT, TRANSPARENT, ON), //
				asList(OFF, TRANSPARENT, TRANSPARENT, TRANSPARENT, OFF), //
				asList(ON, TRANSPARENT, TRANSPARENT, TRANSPARENT, ON), //
				asList(ON, ON, ON, ON, ON));
		final OverlayStationaryView overlayStationaryToPost = new OverlayStationaryView(overlayStationaryStates,
				RgbColorView.RED, RgbColorView.YELLOW, 1);

		final CreateMatrixCommand createMatrixCommand = new CreateMatrixCommand("", 6, 5,
				asList(asList(overlayRollHorizontallyToPost, overlayStationaryToPost)));
		final Response postMatrixResponse = webTarget1.path("matrices").request(APPLICATION_JSON)
				.post(json(createMatrixCommand));
		final String newlyCreatedMatrixPath = getWebTargetPath(postMatrixResponse);

		final Response getResponse = webTarget2.path(newlyCreatedMatrixPath).request(MediaType.APPLICATION_JSON).get();

		final int actualResponseStatusCode = getResponse.getStatus();
		assertThat(actualResponseStatusCode, equalTo(Response.Status.OK.getStatusCode()));

		final List<List<List<LedView>>> actualCompiledFrames = getResponse.readEntity(MatrixView.class)
				.getCompiledFrames();

		// 0nd frame
		final List<List<LedView>> frame0 = new ArrayList<>(asList( //
				asList(new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(RED), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(RED)), //
				asList(new LedView(YELLOW), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(YELLOW)), //
				asList(new LedView(RED), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(RED)), //
				asList(new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED))));

		// 1nd frame
		final List<List<LedView>> frame1 = new ArrayList<>(asList( //
				asList(new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(GREEN)), //
				asList(new LedView(RED), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(RED)), //
				asList(new LedView(YELLOW), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(YELLOW)), //
				asList(new LedView(RED), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(RED)), //
				asList(new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED))));

		// 2rd frame
		final List<List<LedView>> frame2 = new ArrayList<>(asList( //
				asList(new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(GREEN),
						new LedView(GREEN)), //
				asList(new LedView(RED), new LedView(BLACK), new LedView(BLACK), new LedView(GREEN), new LedView(RED)), //
				asList(new LedView(YELLOW), new LedView(BLACK), new LedView(BLACK), new LedView(GREEN),
						new LedView(YELLOW)), //
				asList(new LedView(RED), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(RED)), //
				asList(new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED))));

		// 3th frame
		final List<List<LedView>> frame3 = new ArrayList<>(asList( //
				asList(new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(GREEN), new LedView(GREEN),
						new LedView(GREEN)), //
				asList(new LedView(RED), new LedView(BLACK), new LedView(GREEN), new LedView(BLUE), new LedView(RED)), //
				asList(new LedView(YELLOW), new LedView(BLACK), new LedView(GREEN), new LedView(GREEN),
						new LedView(YELLOW)), //
				asList(new LedView(RED), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(RED)), //
				asList(new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED))));

		// 4th frame
		final List<List<LedView>> frame4 = new ArrayList<>(asList( //
				asList(new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED)), //
				asList(new LedView(BLACK), new LedView(GREEN), new LedView(GREEN), new LedView(GREEN),
						new LedView(GREEN)), //
				asList(new LedView(RED), new LedView(GREEN), new LedView(BLUE), new LedView(GREEN), new LedView(RED)), //
				asList(new LedView(YELLOW), new LedView(GREEN), new LedView(GREEN), new LedView(GREEN),
						new LedView(YELLOW)), //
				asList(new LedView(RED), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(RED)), //
				asList(new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED))));

		// 5th frame
		final List<List<LedView>> frame5 = new ArrayList<>(asList( //
				asList(new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED)), //
				asList(new LedView(GREEN), new LedView(GREEN), new LedView(GREEN), new LedView(GREEN),
						new LedView(GREEN)), //
				asList(new LedView(RED), new LedView(BLUE), new LedView(GREEN), new LedView(BLACK), new LedView(RED)), //
				asList(new LedView(YELLOW), new LedView(GREEN), new LedView(GREEN), new LedView(GREEN),
						new LedView(YELLOW)), //
				asList(new LedView(RED), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(RED)), //
				asList(new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED))));

		// 6th frame
		final List<List<LedView>> frame6 = new ArrayList<>(asList( //
				asList(new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED)), //
				asList(new LedView(GREEN), new LedView(GREEN), new LedView(GREEN), new LedView(GREEN),
						new LedView(GREEN)), //
				asList(new LedView(RED), new LedView(GREEN), new LedView(BLACK), new LedView(GREEN), new LedView(RED)), //
				asList(new LedView(YELLOW), new LedView(GREEN), new LedView(GREEN), new LedView(GREEN),
						new LedView(YELLOW)), //
				asList(new LedView(RED), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(RED)), //
				asList(new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED))));

		// 7th frame
		final List<List<LedView>> frame7 = new ArrayList<>(asList( //
				asList(new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED)), //
				asList(new LedView(GREEN), new LedView(GREEN), new LedView(GREEN), new LedView(GREEN),
						new LedView(GREEN)), //
				asList(new LedView(RED), new LedView(BLACK), new LedView(GREEN), new LedView(BLACK), new LedView(RED)), //
				asList(new LedView(YELLOW), new LedView(GREEN), new LedView(GREEN), new LedView(GREEN),
						new LedView(YELLOW)), //
				asList(new LedView(RED), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(RED)), //
				asList(new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED))));

		// 8th frame
		final List<List<LedView>> frame8 = new ArrayList<>(asList( //
				asList(new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED)), //
				asList(new LedView(GREEN), new LedView(GREEN), new LedView(GREEN), new LedView(GREEN),
						new LedView(BLACK)), //
				asList(new LedView(RED), new LedView(GREEN), new LedView(BLACK), new LedView(GREEN), new LedView(RED)), //
				asList(new LedView(YELLOW), new LedView(GREEN), new LedView(GREEN), new LedView(GREEN),
						new LedView(YELLOW)), //
				asList(new LedView(RED), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(RED)), //
				asList(new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED))));

		// 9th frame
		final List<List<LedView>> frame9 = new ArrayList<>(asList( //
				asList(new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED)), //
				asList(new LedView(GREEN), new LedView(GREEN), new LedView(GREEN), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(RED), new LedView(BLACK), new LedView(GREEN), new LedView(BLACK), new LedView(RED)), //
				asList(new LedView(YELLOW), new LedView(GREEN), new LedView(GREEN), new LedView(BLACK),
						new LedView(YELLOW)), //
				asList(new LedView(RED), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(RED)), //
				asList(new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED))));

		// 10th frame
		final List<List<LedView>> frame10 = new ArrayList<>(asList( //
				asList(new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED)), //
				asList(new LedView(GREEN), new LedView(GREEN), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(RED), new LedView(GREEN), new LedView(BLACK), new LedView(BLACK), new LedView(RED)), //
				asList(new LedView(YELLOW), new LedView(GREEN), new LedView(BLACK), new LedView(BLACK),
						new LedView(YELLOW)), //
				asList(new LedView(RED), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(RED)), //
				asList(new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED))));

		// 11th frame
		final List<List<LedView>> frame11 = new ArrayList<>(asList( //
				asList(new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED)), //
				asList(new LedView(GREEN), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(RED), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(RED)), //
				asList(new LedView(YELLOW), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(YELLOW)), //
				asList(new LedView(RED), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(RED)), //
				asList(new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED))));

		// 12th frame
		final List<List<LedView>> frame12 = new ArrayList<>(asList( //
				asList(new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED)), //
				asList(new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(BLACK)), //
				asList(new LedView(RED), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(RED)), //
				asList(new LedView(YELLOW), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK),
						new LedView(YELLOW)), //
				asList(new LedView(RED), new LedView(BLACK), new LedView(BLACK), new LedView(BLACK), new LedView(RED)), //
				asList(new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED), new LedView(RED))));
		final List<List<List<LedView>>> expectedCompiledFrames = asList(frame0, frame1, frame2, frame3, frame4, frame5,
				frame6, frame7, frame8, frame9, frame10, frame11, frame12);

		assertThat(actualCompiledFrames, equalTo(expectedCompiledFrames));
	}

	@Test
	public void get_allmatrices_shoulReturnNotFoundStatus(@ArquillianResteasyResource("") final WebTarget webTarget1) {
		final Response response = webTarget1.path("matrices").request(APPLICATION_JSON).get();
		assertThat(response.getStatus(), equalTo(Response.Status.NOT_FOUND.getStatusCode()));
	}

	@Test
	@Ignore
	public void get_allmatrices_shoulReturnAllMatricesWithOkStatus(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2,
			@ArquillianResteasyResource("") final WebTarget webTarget3,
			@ArquillianResteasyResource("") final WebTarget webTarget4) {
		final Response postCreateMatrixCommand1 = postCreateMatrixCommand(webTarget1);
		final Response postCreateMatrixCommand2 = postCreateMatrixCommand(webTarget2);
		final Response postCreateMatrixCommand3 = postCreateMatrixCommand(webTarget3);
		final Response response = webTarget4.path("matrices").request(APPLICATION_JSON).get();
		assertThat(response.getStatus(), equalTo(Response.Status.OK.getStatusCode()));

		final List<MatrixView> readEntity = response.readEntity(new GenericType<List<MatrixView>>() {
		});
		assertThat(readEntity.size(), equalTo(2));
	}
}