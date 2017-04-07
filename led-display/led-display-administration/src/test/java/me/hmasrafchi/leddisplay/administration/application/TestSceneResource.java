/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application;

import static java.util.Arrays.asList;
import static me.hmasrafchi.leddisplay.administration.model.Led.State.OFF;
import static me.hmasrafchi.leddisplay.administration.model.Led.State.ON;
import static me.hmasrafchi.leddisplay.administration.model.Led.State.TRANSPARENT;
import static me.hmasrafchi.leddisplay.administration.model.Led.State.UNRECOGNIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

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
import org.junit.Test;
import org.junit.runner.RunWith;

import me.hmasrafchi.leddisplay.administration.model.LedStateRow;
import me.hmasrafchi.leddisplay.administration.model.OverlayRollHorizontally;
import me.hmasrafchi.leddisplay.administration.model.OverlayStationary;
import me.hmasrafchi.leddisplay.administration.model.RgbColor;
import me.hmasrafchi.leddisplay.administration.model.Scene;
import me.hmasrafchi.leddisplay.administration.model.SceneComposite;
import me.hmasrafchi.leddisplay.administration.model.SceneOverlayed;

/**
 * @author michelin
 *
 */
@RunWith(Arquillian.class)
public class TestSceneResource extends TestResource {
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
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void post_scenes_shouldCreateNewScene(@ArquillianResteasyResource("") final WebTarget webTarget) {
		final Response createSceneResponse = sendPostCreateScene(webTarget);

		final int actualResponseStatusCode = createSceneResponse.getStatus();
		final URI actualResponseHeaderLocation = createSceneResponse.getLocation();
		assertThat(actualResponseStatusCode, equalTo(Response.Status.CREATED.getStatusCode()));
		assertThat(actualResponseHeaderLocation, notNullValue());
	}

	@Test
	@Consumes(MediaType.APPLICATION_JSON)
	public void get_scenes_shoulReturnOKIfSceneFound(@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2) {
		final Response createSceneResponse = sendPostCreateScene(webTarget1);
		final String path = getWebTargetPath(createSceneResponse);

		final Response getResponse = webTarget2.path(path).request(MediaType.APPLICATION_JSON).get();
		final Scene actualScene = getResponse.readEntity(Scene.class);

		final int actualResponseStatusCode = getResponse.getStatus();
		assertThat(actualResponseStatusCode, equalTo(Response.Status.OK.getStatusCode()));
		assertThat(actualScene, notNullValue());
	}

	@Test
	@Consumes(MediaType.APPLICATION_JSON)
	public void get_scenes_shoulReturnNotFoundIfSceneNotFound(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2) {
		final Response createSceneResponse = sendPostCreateScene(webTarget1);
		final String path = getWebTargetPath(createSceneResponse);
		final String fooPath = path + "FOO";

		final Response getResponse = webTarget2.path(fooPath).request(MediaType.APPLICATION_JSON).get();

		final int actualResponseStatusCode = getResponse.getStatus();
		assertThat(actualResponseStatusCode, equalTo(Response.Status.NOT_FOUND.getStatusCode()));
	}

	@Test
	@Consumes(MediaType.APPLICATION_JSON)
	public void post_scenes_sceneId_stationary_shouldCreateSceneOverlayedAndThenAddOneOverlayStationaryToItAndAddTheSceneOverlayedToExistingCompositeScene(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2,
			@ArquillianResteasyResource("") final WebTarget webTarget3) {
		final Response createSceneResponse = sendPostCreateScene(webTarget1);
		final String scenePath = getWebTargetPath(createSceneResponse);
		final String path = String.format("%s/%s", scenePath, PathLiterals.OVERLAY_STATIONARY);

		final List<LedStateRow> overlayStationaryStates = asList( //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON)), //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED, UNRECOGNIZED)));
		final OverlayStationary overlayStationaryToPost = new OverlayStationary(overlayStationaryStates,
				new RgbColor(255, 0, 0), new RgbColor(0, 255, 0), 10);

		final Response postResponse = webTarget2.path(path).request(MediaType.APPLICATION_JSON)
				.post(Entity.json(overlayStationaryToPost));

		final int actualResponseStatusCode = postResponse.getStatus();
		assertThat(actualResponseStatusCode, equalTo(Response.Status.NO_CONTENT.getStatusCode()));

		final Scene actualScene = getScene(webTarget3, scenePath);
		final SceneComposite expectedScene = new SceneComposite(
				asList(new SceneOverlayed(asList(overlayStationaryToPost))));
		assertThat(actualScene, equalTo(expectedScene));
	}

	@Test
	@Consumes(MediaType.APPLICATION_JSON)
	public void post_scenes_sceneId_roll_shouldCreateSceneOverlayedAndThenAddOneOverlayRollHorizontallyToItAndAddTheSceneOverlayedToExistingCompositeScene(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2,
			@ArquillianResteasyResource("") final WebTarget webTarget3) {
		final Response createSceneResponse = sendPostCreateScene(webTarget1);
		final String scenePath = getWebTargetPath(createSceneResponse);
		final String path = String.format("%s/%s", scenePath, PathLiterals.OVERLAY_ROLL_HORIZONTALLY);

		final List<LedStateRow> overlayRollHorizontallyStates = asList( //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON)), //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED, UNRECOGNIZED)));
		final OverlayRollHorizontally overlayRollHorizontallyToPost = new OverlayRollHorizontally(
				overlayRollHorizontallyStates, new RgbColor(255, 0, 0), new RgbColor(0, 255, 0), 10, 1);

		final Response postResponse = webTarget2.path(path).request(MediaType.APPLICATION_JSON)
				.post(Entity.json(overlayRollHorizontallyToPost));

		final int actualResponseStatusCode = postResponse.getStatus();
		assertThat(actualResponseStatusCode, equalTo(Response.Status.NO_CONTENT.getStatusCode()));

		final Scene actualScene = getScene(webTarget3, scenePath);
		assertThat(actualScene, notNullValue());

		final SceneComposite expectedScene = new SceneComposite(
				asList(new SceneOverlayed(asList(overlayRollHorizontallyToPost))));
		assertThat(actualScene, equalTo(expectedScene));
	}

	@Test
	@Consumes(MediaType.APPLICATION_JSON)
	public void post_scenes_sceneId_roll_shouldCreateSceneOverlayedAndThenAddOneOverlayStationaryToItAndThenCreateANewSceneOverlayedAndAddOneOverlayRollHorizontallyToItAndAddBothSceneOverlayedToExistingCompositeScene(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2,
			@ArquillianResteasyResource("") final WebTarget webTarget3,
			@ArquillianResteasyResource("") final WebTarget webTarget4) {
		final Response createSceneResponse = sendPostCreateScene(webTarget1);
		final String scenePath = getWebTargetPath(createSceneResponse);

		final String pathStationary = String.format("%s/%s", scenePath, PathLiterals.OVERLAY_STATIONARY);
		final List<LedStateRow> overlayStationaryStates = asList( //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON)), //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED, UNRECOGNIZED)));
		final OverlayStationary overlayStationaryToPost = new OverlayStationary(overlayStationaryStates,
				new RgbColor(255, 0, 0), new RgbColor(0, 255, 0), 10);
		final Response postResponse1 = webTarget2.path(pathStationary).request(MediaType.APPLICATION_JSON)
				.post(Entity.json(overlayStationaryToPost));

		final String pathRoll = String.format("%s/%s", scenePath, PathLiterals.OVERLAY_ROLL_HORIZONTALLY);
		final List<LedStateRow> overlayRollHorizontallyStates = asList( //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON)), //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED, UNRECOGNIZED)));
		final OverlayRollHorizontally overlayRollHorizontallyToPost = new OverlayRollHorizontally(
				overlayRollHorizontallyStates, new RgbColor(255, 0, 0), new RgbColor(0, 255, 0), 10, 1);
		final Response postResponse2 = webTarget3.path(pathRoll).request(MediaType.APPLICATION_JSON)
				.post(Entity.json(overlayRollHorizontallyToPost));

		final int actualResponseStatusCode1 = postResponse1.getStatus();
		assertThat(actualResponseStatusCode1, equalTo(Response.Status.NO_CONTENT.getStatusCode()));
		final int actualResponseStatusCode2 = postResponse2.getStatus();
		assertThat(actualResponseStatusCode2, equalTo(Response.Status.NO_CONTENT.getStatusCode()));

		final Scene actualScene = getScene(webTarget4, scenePath);
		assertThat(actualScene, notNullValue());

		final SceneComposite expectedScene = new SceneComposite(
				asList(new SceneOverlayed(asList(overlayStationaryToPost)),
						new SceneOverlayed(asList(overlayRollHorizontallyToPost))));
		assertThat(actualScene, equalTo(expectedScene));
	}
}