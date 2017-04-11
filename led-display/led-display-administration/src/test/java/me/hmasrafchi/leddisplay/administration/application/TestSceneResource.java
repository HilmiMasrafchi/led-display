/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application;

import static java.util.Arrays.asList;
import static me.hmasrafchi.leddisplay.administration.model.Led.State.OFF;
import static me.hmasrafchi.leddisplay.administration.model.Led.State.ON;
import static me.hmasrafchi.leddisplay.administration.model.Led.State.TRANSPARENT;
import static me.hmasrafchi.leddisplay.administration.model.Led.State.UNRECOGNIZED;
import static me.hmasrafchi.leddisplay.administration.model.RgbColor.BLACK;
import static me.hmasrafchi.leddisplay.administration.model.RgbColor.BLUE;
import static me.hmasrafchi.leddisplay.administration.model.RgbColor.GREEN;
import static me.hmasrafchi.leddisplay.administration.model.RgbColor.RED;
import static me.hmasrafchi.leddisplay.administration.model.RgbColor.YELLOW;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
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

import me.hmasrafchi.leddisplay.administration.CompiledFrames;
import me.hmasrafchi.leddisplay.administration.Frame;
import me.hmasrafchi.leddisplay.administration.model.Led;
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
	public void post_scenes_sceneId_shouldAddOverlayToCompositeScene(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2,
			@ArquillianResteasyResource("") final WebTarget webTarget3) {
		final Response createSceneResponse = sendPostCreateScene(webTarget1);
		final String scenePath = getWebTargetPath(createSceneResponse);
		final List<LedStateRow> overlayStationaryStates = asList( //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON)), //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED, UNRECOGNIZED)));
		final OverlayStationary overlayStationaryToPost = new OverlayStationary(overlayStationaryStates,
				new RgbColor(255, 0, 0), new RgbColor(0, 255, 0), 10);

		final Response postResponse = webTarget2.path(scenePath).request(MediaType.APPLICATION_JSON)
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
	public void post_scenes_sceneId_shouldAddTwoDifferentTypesOverlaysToCompositeScene(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2,
			@ArquillianResteasyResource("") final WebTarget webTarget3,
			@ArquillianResteasyResource("") final WebTarget webTarget4) {
		final Response createSceneResponse = sendPostCreateScene(webTarget1);
		final String scenePath = getWebTargetPath(createSceneResponse);

		final List<LedStateRow> overlayStationaryStates = asList( //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON)), //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED, UNRECOGNIZED)));
		final OverlayStationary overlayStationaryToPost = new OverlayStationary(overlayStationaryStates,
				new RgbColor(255, 0, 0), new RgbColor(0, 255, 0), 10);
		final Response postResponse1 = webTarget2.path(scenePath).request(MediaType.APPLICATION_JSON)
				.post(Entity.json(overlayStationaryToPost));

		final List<LedStateRow> overlayRollHorizontallyStates = asList( //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON)), //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED, UNRECOGNIZED)));
		final OverlayRollHorizontally overlayRollHorizontallyToPost = new OverlayRollHorizontally(
				overlayRollHorizontallyStates, new RgbColor(255, 0, 0), new RgbColor(0, 255, 0), 10, 1);
		final Response postResponse2 = webTarget3.path(scenePath).request(MediaType.APPLICATION_JSON)
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

	@Test
	@Consumes(MediaType.APPLICATION_JSON)
	public void post_scenes_sceneId_shouldAddTwoSameTypeOverlaysFromToCompositeScene(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2,
			@ArquillianResteasyResource("") final WebTarget webTarget3,
			@ArquillianResteasyResource("") final WebTarget webTarget4) {
		final Response createSceneResponse = sendPostCreateScene(webTarget1);
		final String scenePath = getWebTargetPath(createSceneResponse);

		final List<LedStateRow> overlayStationaryStates1 = asList( //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON)), //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED, UNRECOGNIZED)));
		final OverlayStationary overlayStationaryToPost1 = new OverlayStationary(overlayStationaryStates1,
				new RgbColor(255, 0, 0), new RgbColor(0, 255, 0), 10);
		final Response postResponse1 = webTarget2.path(scenePath).request(MediaType.APPLICATION_JSON)
				.post(Entity.json(overlayStationaryToPost1));

		final List<LedStateRow> overlayStationaryStates2 = asList( //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON)), //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED, UNRECOGNIZED)));
		final OverlayStationary overlayStationaryToPost2 = new OverlayStationary(overlayStationaryStates2,
				new RgbColor(255, 0, 0), new RgbColor(0, 255, 0), 10);
		final Response postResponse2 = webTarget2.path(scenePath).request(MediaType.APPLICATION_JSON)
				.post(Entity.json(overlayStationaryToPost2));

		final int actualResponseStatusCode1 = postResponse1.getStatus();
		assertThat(actualResponseStatusCode1, equalTo(Response.Status.NO_CONTENT.getStatusCode()));
		final int actualResponseStatusCode2 = postResponse2.getStatus();
		assertThat(actualResponseStatusCode2, equalTo(Response.Status.NO_CONTENT.getStatusCode()));

		final Scene actualScene = getScene(webTarget4, scenePath);
		assertThat(actualScene, notNullValue());

		final SceneComposite expectedScene = new SceneComposite(
				asList(new SceneOverlayed(asList(overlayStationaryToPost1)),
						new SceneOverlayed(asList(overlayStationaryToPost2))));
		assertThat(actualScene, equalTo(expectedScene));
	}

	@Test
	@Consumes(MediaType.APPLICATION_JSON)
	public void post_scenes_sceneId_overlays_overlayId_shouldAddTwoSameTypesOverlaysToSceneOverlayed(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2,
			@ArquillianResteasyResource("") final WebTarget webTarget3,
			@ArquillianResteasyResource("") final WebTarget webTarget4,
			@ArquillianResteasyResource("") final WebTarget webTarget5) {
		final Response createSceneResponse = sendPostCreateScene(webTarget1);

		final String scenePath = getWebTargetPath(createSceneResponse);
		final List<LedStateRow> overlayStationaryStates1 = asList( //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON)), //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED, UNRECOGNIZED)));
		final OverlayStationary overlayStationaryToPost1 = new OverlayStationary(overlayStationaryStates1,
				new RgbColor(255, 0, 0), new RgbColor(0, 255, 0), 10);
		final Response postResponse1 = webTarget2.path(scenePath).request(MediaType.APPLICATION_JSON)
				.post(Entity.json(overlayStationaryToPost1));

		final SceneComposite scene = getScene(webTarget3, scenePath);
		final int overlayId = ((SceneOverlayed) scene.getScenes().iterator().next()).getId();
		final String sceneOverlayPath = String.format("%s/overlays/%s", scenePath, overlayId);

		final List<LedStateRow> overlayStationaryStates2 = asList( //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON)), //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED, UNRECOGNIZED)));
		final OverlayStationary overlayStationaryToPost2 = new OverlayStationary(overlayStationaryStates2,
				new RgbColor(255, 0, 0), new RgbColor(0, 255, 0), 10);
		final Response postResponse2 = webTarget2.path(sceneOverlayPath).request(MediaType.APPLICATION_JSON)
				.post(Entity.json(overlayStationaryToPost2));

		final int actualResponseStatusCode1 = postResponse1.getStatus();
		assertThat(actualResponseStatusCode1, equalTo(Response.Status.NO_CONTENT.getStatusCode()));
		final int actualResponseStatusCode2 = postResponse2.getStatus();
		assertThat(actualResponseStatusCode2, equalTo(Response.Status.NO_CONTENT.getStatusCode()));

		final Scene actualScene = getScene(webTarget5, scenePath);
		assertThat(actualScene, notNullValue());

		final SceneComposite expectedScene = new SceneComposite(
				asList(new SceneOverlayed(asList(overlayStationaryToPost1, overlayStationaryToPost2))));
		assertThat(actualScene, equalTo(expectedScene));
	}

	@Test
	@Consumes(MediaType.APPLICATION_JSON)
	public void post_scenes_sceneId_overlays_overlayId_shouldAddTwoDifferentTypesOverlaysToSceneOverlayed(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2,
			@ArquillianResteasyResource("") final WebTarget webTarget3,
			@ArquillianResteasyResource("") final WebTarget webTarget4,
			@ArquillianResteasyResource("") final WebTarget webTarget5) {
		final Response createSceneResponse = sendPostCreateScene(webTarget1);

		final String scenePath = getWebTargetPath(createSceneResponse);
		final List<LedStateRow> overlayStationaryStates = asList( //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON)), //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED, UNRECOGNIZED)));
		final OverlayStationary overlayStationaryToPost = new OverlayStationary(overlayStationaryStates,
				new RgbColor(255, 0, 0), new RgbColor(0, 255, 0), 10);
		final Response postResponse1 = webTarget2.path(scenePath).request(MediaType.APPLICATION_JSON)
				.post(Entity.json(overlayStationaryToPost));

		final SceneComposite scene = getScene(webTarget3, scenePath);
		final int overlayId = ((SceneOverlayed) scene.getScenes().iterator().next()).getId();
		final String sceneOverlayPath = String.format("%s/overlays/%s", scenePath, overlayId);

		final List<LedStateRow> overlayRollHorizontallyStates = asList( //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON)), //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED, UNRECOGNIZED)));
		final OverlayRollHorizontally overlayRollHorizontallyToPost = new OverlayRollHorizontally(
				overlayRollHorizontallyStates, new RgbColor(255, 0, 0), new RgbColor(0, 255, 0), 10, 1);
		final Response postResponse2 = webTarget4.path(sceneOverlayPath).request(MediaType.APPLICATION_JSON)
				.post(Entity.json(overlayRollHorizontallyToPost));

		final int actualResponseStatusCode1 = postResponse1.getStatus();
		assertThat(actualResponseStatusCode1, equalTo(Response.Status.NO_CONTENT.getStatusCode()));
		final int actualResponseStatusCode2 = postResponse2.getStatus();
		assertThat(actualResponseStatusCode2, equalTo(Response.Status.NO_CONTENT.getStatusCode()));

		final Scene actualScene = getScene(webTarget5, scenePath);
		assertThat(actualScene, notNullValue());

		final SceneComposite expectedScene = new SceneComposite(
				asList(new SceneOverlayed(asList(overlayStationaryToPost, overlayRollHorizontallyToPost))));
		assertThat(actualScene, equalTo(expectedScene));
	}

	@Test
	@Consumes(MediaType.APPLICATION_JSON)
	public void get_compiledFrames_shoulReturnNotFoundIfNoScenesArePresent(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2) {
		final Response postResponse = sendPostCreateScene(webTarget1);

		final String path = getWebTargetPath(postResponse) + "/compiled_frames";
		final Response getResponse = webTarget2.path(path).request(MediaType.APPLICATION_JSON).get();

		final int actualResponseStatusCode = getResponse.getStatus();
		assertThat(actualResponseStatusCode, is(equalTo(Response.Status.NOT_FOUND.getStatusCode())));
	}

	@Test
	@Consumes(MediaType.APPLICATION_JSON)
	public void get_compiledFrames_shoulReturnCompiledFramesForTwoSameTypeScenesFromSceneComposited(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2,
			@ArquillianResteasyResource("") final WebTarget webTarget3,
			@ArquillianResteasyResource("") final WebTarget webTarget4) {
		final Response postResponse = sendPostCreateScene(webTarget1);
		final String scenePath = getWebTargetPath(postResponse);

		final List<LedStateRow> overlayRollHorizontallyStates1 = asList( //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON)), //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)));
		final OverlayRollHorizontally overlayRollHorizontallyToPost1 = new OverlayRollHorizontally(
				overlayRollHorizontallyStates1, RgbColor.GREEN, RgbColor.BLUE, 5, 0);
		webTarget2.path(scenePath).request(MediaType.APPLICATION_JSON)
				.post(Entity.json(overlayRollHorizontallyToPost1));

		final List<LedStateRow> overlayRollHorizontallyStates2 = asList( //
				new LedStateRow(asList(ON, ON, ON)), //
				new LedStateRow(asList(ON, OFF, ON)), //
				new LedStateRow(asList(ON, ON, ON)));
		final OverlayRollHorizontally overlayRollHorizontallyToPost2 = new OverlayRollHorizontally(
				overlayRollHorizontallyStates2, RgbColor.GREEN, RgbColor.BLUE, 5, 1);
		webTarget3.path(scenePath).request(MediaType.APPLICATION_JSON)
				.post(Entity.json(overlayRollHorizontallyToPost2));

		final String path = scenePath + "/compiled_frames";
		final Response getResponse = webTarget4.path(path).queryParam("rowCount", 6).queryParam("columnCount", 5)
				.request(MediaType.APPLICATION_JSON).get();

		final int actualResponseStatusCode = getResponse.getStatus();
		assertThat(actualResponseStatusCode, is(equalTo(Response.Status.OK.getStatusCode())));

		final CompiledFrames actualCompiledFrames = getResponse.readEntity(CompiledFrames.class);

		// scene 1
		final Frame frame1 = new Frame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame2 = new Frame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame3 = new Frame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(BLUE)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame4 = new Frame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(BLUE), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame5 = new Frame(asList( //
				asList(new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(GREEN), new Led(BLUE), new Led(GREEN), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame6 = new Frame(asList( //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(GREEN), new Led(BLUE), new Led(GREEN), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame7 = new Frame(asList( //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLUE), new Led(GREEN), new Led(BLACK), new Led(GREEN), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame8 = new Frame(asList( //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(GREEN), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame9 = new Frame(asList( //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(GREEN), new Led(BLACK), new Led(GREEN), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame10 = new Frame(asList( //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(GREEN), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame11 = new Frame(asList( //
				asList(new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame12 = new Frame(asList( //
				asList(new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame13 = new Frame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		// scene 2
		final Frame frame14 = new Frame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame15 = new Frame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame16 = new Frame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(BLUE)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame17 = new Frame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(BLUE), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame18 = new Frame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(GREEN), new Led(BLUE), new Led(GREEN), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame19 = new Frame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(BLUE), new Led(GREEN), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame20 = new Frame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLUE), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame21 = new Frame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame22 = new Frame(asList( //
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
	@Consumes(MediaType.APPLICATION_JSON)
	public void get_compiledFrames_shoulReturnCompiledFramesForTwoDifferentTypeScenesFromSceneOverlayed(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2,
			@ArquillianResteasyResource("") final WebTarget webTarget3,
			@ArquillianResteasyResource("") final WebTarget webTarget4,
			@ArquillianResteasyResource("") final WebTarget webTarget5) {
		final Response postResponse = sendPostCreateScene(webTarget1);
		final String scenePath = getWebTargetPath(postResponse);

		final List<LedStateRow> overlayRollHorizontallyStates = asList( //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON)), //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)));
		final OverlayRollHorizontally overlayRollHorizontallyToPost = new OverlayRollHorizontally(
				overlayRollHorizontallyStates, RgbColor.GREEN, RgbColor.BLUE, 5, 1);
		webTarget2.path(scenePath).request(MediaType.APPLICATION_JSON).post(Entity.json(overlayRollHorizontallyToPost));

		final SceneComposite scene = getScene(webTarget3, scenePath);
		final int overlayId = ((SceneOverlayed) scene.getScenes().iterator().next()).getId();
		final String sceneOverlayPath = String.format("%s/overlays/%s", scenePath, overlayId);

		final List<LedStateRow> overlayStationaryStates = asList( //
				new LedStateRow(asList(ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT)), //
				new LedStateRow(asList(ON, TRANSPARENT, TRANSPARENT, TRANSPARENT, ON)), //
				new LedStateRow(asList(OFF, TRANSPARENT, TRANSPARENT, TRANSPARENT, OFF)), //
				new LedStateRow(asList(ON, TRANSPARENT, TRANSPARENT, TRANSPARENT, ON)), //
				new LedStateRow(asList(ON, ON, ON, ON, ON)));
		final OverlayStationary overlayStationaryToPost = new OverlayStationary(overlayStationaryStates, RgbColor.RED,
				RgbColor.YELLOW, 1);
		webTarget4.path(sceneOverlayPath).request(MediaType.APPLICATION_JSON)
				.post(Entity.json(overlayStationaryToPost));

		final String path = scenePath + "/compiled_frames";
		final Response getResponse = webTarget5.path(path).queryParam("rowCount", 6).queryParam("columnCount", 5)
				.request(MediaType.APPLICATION_JSON).get();

		final int actualResponseStatusCode = getResponse.getStatus();
		assertThat(actualResponseStatusCode, is(equalTo(Response.Status.OK.getStatusCode())));

		// 0nd frame
		final Frame frame0 = new Frame(
				asList(asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
						asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
						asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
						asList(new Led(YELLOW), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(YELLOW)), //
						asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
						asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 1nd frame
		final Frame frame1 = new Frame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(YELLOW), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 2rd frame
		final Frame frame2 = new Frame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(RED)), //
				asList(new Led(YELLOW), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 3th frame
		final Frame frame3 = new Frame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(BLACK), new Led(GREEN), new Led(BLUE), new Led(RED)), //
				asList(new Led(YELLOW), new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 4th frame
		final Frame frame4 = new Frame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(GREEN), new Led(BLUE), new Led(GREEN), new Led(RED)), //
				asList(new Led(YELLOW), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 5th frame
		final Frame frame5 = new Frame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(BLUE), new Led(GREEN), new Led(BLACK), new Led(RED)), //
				asList(new Led(YELLOW), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 6th frame
		final Frame frame6 = new Frame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(GREEN), new Led(BLACK), new Led(GREEN), new Led(RED)), //
				asList(new Led(YELLOW), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 7th frame
		final Frame frame7 = new Frame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(BLACK), new Led(GREEN), new Led(BLACK), new Led(RED)), //
				asList(new Led(YELLOW), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 8th frame
		final Frame frame8 = new Frame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK)), //
				asList(new Led(RED), new Led(GREEN), new Led(BLACK), new Led(GREEN), new Led(RED)), //
				asList(new Led(YELLOW), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 9th frame
		final Frame frame9 = new Frame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(RED), new Led(BLACK), new Led(GREEN), new Led(BLACK), new Led(RED)), //
				asList(new Led(YELLOW), new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 10th frame
		final Frame frame10 = new Frame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(RED), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(YELLOW), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 11th frame
		final Frame frame11 = new Frame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(YELLOW), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 12th frame
		final Frame frame12 = new Frame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(YELLOW), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));
		final CompiledFrames expectedCompiledFrames = new CompiledFrames(asList(frame0, frame1, frame2, frame3, frame4,
				frame5, frame6, frame7, frame8, frame9, frame10, frame11, frame12));

		final CompiledFrames actualCompiledFrames = getResponse.readEntity(CompiledFrames.class);

		assertThat(actualCompiledFrames, equalTo(expectedCompiledFrames));
	}
}