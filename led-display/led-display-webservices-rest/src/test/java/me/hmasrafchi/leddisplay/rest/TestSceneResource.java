/**
 * 
 */
package me.hmasrafchi.leddisplay.rest;

import static java.util.Arrays.asList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static me.hmasrafchi.leddisplay.rest.persist.LedState.OFF;
import static me.hmasrafchi.leddisplay.rest.persist.LedState.ON;
import static me.hmasrafchi.leddisplay.rest.persist.LedState.TRANSPARENT;
import static me.hmasrafchi.leddisplay.rest.persist.LedState.UNRECOGNIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

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

import me.hmasrafchi.leddisplay.rest.persist.LedRgbColor;
import me.hmasrafchi.leddisplay.rest.persist.LedState;
import me.hmasrafchi.leddisplay.rest.persist.LedStateRow;
import me.hmasrafchi.leddisplay.rest.persist.MatrixEntity;
import me.hmasrafchi.leddisplay.rest.persist.Overlay;
import me.hmasrafchi.leddisplay.rest.persist.OverlayRollHorizontally;
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
	public void post_matrices_scenes_shouldCreateNewScene(@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2,
			@ArquillianResteasyResource("") final WebTarget webTarget3) {
		final Response createMatrixResponse = sendPostCreatingMatrix(webTarget1, 6, 4);
		final String webTargetPathMatrices = getWebTargetPath(createMatrixResponse);
		final String webTargetPathScenes = String.format("%s/%s", webTargetPathMatrices, "scenes");

		final Response postScenesResponse = webTarget2.path(webTargetPathScenes).request().post(Entity.text(""));

		final int actualResponseStatusCode = postScenesResponse.getStatus();
		assertThat(actualResponseStatusCode, is(equalTo(Response.Status.CREATED.getStatusCode())));

		final String actualResponseHeaderLocationPath = getWebTargetPath(postScenesResponse);
		assertThat(actualResponseHeaderLocationPath, is(notNullValue()));
		final String expectedRegExMatch = String.format("%s/.*/%s/.*", PathLiterals.MATRICES, PathLiterals.SCENES);
		assertTrue(actualResponseHeaderLocationPath.matches(expectedRegExMatch));

		final Response getMatrixResponse = webTarget3.path(webTargetPathMatrices).request(APPLICATION_JSON).get();
		final MatrixEntity actualMatrixEntity = getMatrixResponse.readEntity(MatrixEntity.class);
		final List<Scene> scenes = actualMatrixEntity.getScenes();
		assertThat(scenes.size(), is(1));
	}

	@Test
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void post_matrices_scenes_shouldCreateTwoNewScenes(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2,
			@ArquillianResteasyResource("") final WebTarget webTarget3,
			@ArquillianResteasyResource("") final WebTarget webTarget4) {
		final Response createMatrixResponse = sendPostCreatingMatrix(webTarget1, 6, 4);
		final String webTargetPathMatrices = getWebTargetPath(createMatrixResponse);
		final String webTargetPathScenes = String.format("%s/%s", webTargetPathMatrices, "scenes");

		webTarget2.path(webTargetPathScenes).request().post(Entity.text(""));
		webTarget3.path(webTargetPathScenes).request().post(Entity.text(""));

		final Response getMatrixResponse = webTarget4.path(webTargetPathMatrices).request(APPLICATION_JSON).get();
		final MatrixEntity actualMatrixEntity = getMatrixResponse.readEntity(MatrixEntity.class);
		final List<Scene> scenes = actualMatrixEntity.getScenes();
		assertThat(scenes.size(), is(2));
	}

	@Test
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void post_matrices_scenes_shouldAddOverlayStationaryToScene(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2,
			@ArquillianResteasyResource("") final WebTarget webTarget3,
			@ArquillianResteasyResource("") final WebTarget webTarget4) {
		final Response createMatrixResponse = sendPostCreatingMatrix(webTarget1, 6, 4);
		final String webTargetPathMatrices = getWebTargetPath(createMatrixResponse);
		final String webTargetPathScenes = String.format("%s/%s", webTargetPathMatrices, "scenes");

		final Response postScenesResponse = webTarget2.path(webTargetPathScenes).request().post(Entity.text(""));
		final String newlyCreatedSceneLocationPath = getWebTargetPath(postScenesResponse);

		final List<LedStateRow> overlayStationaryStates = asList( //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(ON, LedState.OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON)), //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED, UNRECOGNIZED)));
		final OverlayStationary overlayStationaryToPost = new OverlayStationary(overlayStationaryStates,
				new LedRgbColor(255, 0, 0), new LedRgbColor(0, 255, 0), 10);

		final Response postOverlayStationaryResponse = webTarget3
				.path(String.format("%s/%s", newlyCreatedSceneLocationPath, PathLiterals.OVERLAY_STATIONARY))
				.request(APPLICATION_JSON).post(Entity.json(overlayStationaryToPost));
		assertThat(postOverlayStationaryResponse.getStatus(), is(Response.Status.CREATED.getStatusCode()));

		final String actualResponseHeaderLocationPath = getWebTargetPath(postOverlayStationaryResponse);
		assertThat(actualResponseHeaderLocationPath, is(notNullValue()));
		final String expectedRegExMatch = String.format("%s/.*/%s/.*/%s/.*", PathLiterals.MATRICES, PathLiterals.SCENES,
				PathLiterals.OVERLAYS);
		assertTrue(actualResponseHeaderLocationPath.matches(expectedRegExMatch));

		final Response getMatrixResponse = webTarget4.path(webTargetPathMatrices).request(APPLICATION_JSON).get();
		final MatrixEntity actualMatrixEntity = getMatrixResponse.readEntity(MatrixEntity.class);
		final List<Overlay> overlays = actualMatrixEntity.getScenes().get(0).getOverlays();
		assertThat(overlays.size(), is(1));
		assertThat(overlays.get(0), equalTo(overlayStationaryToPost));
	}

	@Test
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void post_matrices_scenes_shouldAddTwoOverlaysStationaryToScene(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2,
			@ArquillianResteasyResource("") final WebTarget webTarget3,
			@ArquillianResteasyResource("") final WebTarget webTarget4,
			@ArquillianResteasyResource("") final WebTarget webTarget5) {
		final Response createMatrixResponse = sendPostCreatingMatrix(webTarget1, 6, 4);
		final String webTargetPathMatrices = getWebTargetPath(createMatrixResponse);
		final String webTargetPathScenes = String.format("%s/%s", webTargetPathMatrices, "scenes");

		final Response postScenesResponse = webTarget2.path(webTargetPathScenes).request().post(Entity.text(""));
		final String newlyCreatedSceneLocationPath = getWebTargetPath(postScenesResponse);

		final List<LedStateRow> overlayStationaryStates1 = asList( //
				new LedStateRow(asList(OFF, OFF, OFF, OFF, OFF, OFF, OFF)), //
				new LedStateRow(asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON)), //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED, UNRECOGNIZED)));
		final OverlayStationary overlayStationaryToPost1 = new OverlayStationary(overlayStationaryStates1,
				new LedRgbColor(255, 0, 0), new LedRgbColor(0, 255, 0), 10);
		webTarget3.path(String.format("%s/%s", newlyCreatedSceneLocationPath, PathLiterals.OVERLAY_STATIONARY))
				.request(APPLICATION_JSON).post(Entity.json(overlayStationaryToPost1));

		final List<LedStateRow> overlayStationaryStates2 = asList( //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON)), //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED, UNRECOGNIZED)));
		final OverlayStationary overlayStationaryToPost2 = new OverlayStationary(overlayStationaryStates2,
				new LedRgbColor(255, 0, 0), new LedRgbColor(0, 255, 0), 10);
		webTarget4.path(String.format("%s/%s", newlyCreatedSceneLocationPath, PathLiterals.OVERLAY_STATIONARY))
				.request(APPLICATION_JSON).post(Entity.json(overlayStationaryToPost2));

		final Response getMatrixResponse = webTarget5.path(webTargetPathMatrices).request(APPLICATION_JSON).get();
		final MatrixEntity actualMatrixEntity = getMatrixResponse.readEntity(MatrixEntity.class);
		final List<Overlay> overlays = actualMatrixEntity.getScenes().get(0).getOverlays();
		assertThat(overlays.size(), is(2));
		assertThat(overlays.get(0), equalTo(overlayStationaryToPost1));
		assertThat(overlays.get(1), equalTo(overlayStationaryToPost2));
	}

	@Test
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void post_matrices_scenes_shouldAddOverlayRollHorizontallyToScene(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2,
			@ArquillianResteasyResource("") final WebTarget webTarget3,
			@ArquillianResteasyResource("") final WebTarget webTarget4) {
		final Response createMatrixResponse = sendPostCreatingMatrix(webTarget1, 6, 4);
		final String webTargetPathMatrices = getWebTargetPath(createMatrixResponse);
		final String webTargetPathScenes = String.format("%s/%s", webTargetPathMatrices, "scenes");

		final Response postScenesResponse = webTarget2.path(webTargetPathScenes).request().post(Entity.text(""));
		final String newlyCreatedSceneLocationPath = getWebTargetPath(postScenesResponse);

		final List<LedStateRow> overlayRollHorizontallyStates = asList( //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON)), //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED, UNRECOGNIZED)));
		final OverlayRollHorizontally overlayRollHorizontallyToPost = new OverlayRollHorizontally(
				overlayRollHorizontallyStates, new LedRgbColor(255, 0, 0), new LedRgbColor(0, 255, 0), 10, 1);

		final Response postOverlayRollHorizontallyResponse = webTarget3
				.path(String.format("%s/%s", newlyCreatedSceneLocationPath, PathLiterals.OVERLAY_ROLL_HORIZONTALLY))
				.request(APPLICATION_JSON).post(Entity.json(overlayRollHorizontallyToPost));
		assertThat(postOverlayRollHorizontallyResponse.getStatus(), is(Response.Status.CREATED.getStatusCode()));

		final String actualResponseHeaderLocationPath = getWebTargetPath(postOverlayRollHorizontallyResponse);
		assertThat(actualResponseHeaderLocationPath, is(notNullValue()));
		final String expectedRegExMatch = String.format("%s/.*/%s/.*/%s/.*", PathLiterals.MATRICES, PathLiterals.SCENES,
				PathLiterals.OVERLAYS);
		assertTrue(actualResponseHeaderLocationPath.matches(expectedRegExMatch));

		final Response getMatrixResponse = webTarget4.path(webTargetPathMatrices).request(APPLICATION_JSON).get();
		final MatrixEntity actualMatrixEntity = getMatrixResponse.readEntity(MatrixEntity.class);
		final List<Overlay> overlays = actualMatrixEntity.getScenes().get(0).getOverlays();
		assertThat(overlays.size(), is(1));
		assertThat(overlays.get(0), equalTo(overlayRollHorizontallyToPost));
	}

	@Test
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void post_matrices_scenes_shouldAddTwoOverlaysRollHorizontallyToScene(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2,
			@ArquillianResteasyResource("") final WebTarget webTarget3,
			@ArquillianResteasyResource("") final WebTarget webTarget4,
			@ArquillianResteasyResource("") final WebTarget webTarget5) {
		final Response createMatrixResponse = sendPostCreatingMatrix(webTarget1, 6, 4);
		final String webTargetPathMatrices = getWebTargetPath(createMatrixResponse);
		final String webTargetPathScenes = String.format("%s/%s", webTargetPathMatrices, "scenes");

		final Response postScenesResponse = webTarget2.path(webTargetPathScenes).request().post(Entity.text(""));
		final String newlyCreatedSceneLocationPath = getWebTargetPath(postScenesResponse);

		final List<LedStateRow> overlayRollHorizontallyStates1 = asList( //
				new LedStateRow(asList(OFF, OFF, OFF, OFF, OFF, OFF, OFF)), //
				new LedStateRow(asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON)), //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED, UNRECOGNIZED)));
		final OverlayRollHorizontally overlayRollHorizontallyToPost1 = new OverlayRollHorizontally(
				overlayRollHorizontallyStates1, new LedRgbColor(255, 0, 0), new LedRgbColor(0, 255, 0), 10, 1);
		webTarget3.path(String.format("%s/%s", newlyCreatedSceneLocationPath, PathLiterals.OVERLAY_ROLL_HORIZONTALLY))
				.request(APPLICATION_JSON).post(Entity.json(overlayRollHorizontallyToPost1));

		final List<LedStateRow> overlayRollHorizontallyStates2 = asList( //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON)), //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED, UNRECOGNIZED)));
		final OverlayRollHorizontally overlayRollHorizontallyToPost2 = new OverlayRollHorizontally(
				overlayRollHorizontallyStates2, new LedRgbColor(255, 0, 0), new LedRgbColor(0, 255, 0), 10, 1);
		webTarget4.path(String.format("%s/%s", newlyCreatedSceneLocationPath, PathLiterals.OVERLAY_ROLL_HORIZONTALLY))
				.request(APPLICATION_JSON).post(Entity.json(overlayRollHorizontallyToPost2));

		final Response getMatrixResponse = webTarget5.path(webTargetPathMatrices).request(APPLICATION_JSON).get();
		final MatrixEntity actualMatrixEntity = getMatrixResponse.readEntity(MatrixEntity.class);
		final List<Overlay> overlays = actualMatrixEntity.getScenes().get(0).getOverlays();
		assertThat(overlays.size(), is(2));
		assertThat(overlays.get(0), equalTo(overlayRollHorizontallyToPost1));
		assertThat(overlays.get(1), equalTo(overlayRollHorizontallyToPost2));
	}

	@Test
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void post_matrices_scenes_shouldAddOverlayStationaryAndOverlayRollHorizontallyToScene(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2,
			@ArquillianResteasyResource("") final WebTarget webTarget3,
			@ArquillianResteasyResource("") final WebTarget webTarget4,
			@ArquillianResteasyResource("") final WebTarget webTarget5) {
		final Response createMatrixResponse = sendPostCreatingMatrix(webTarget1, 6, 4);
		final String webTargetPathMatrices = getWebTargetPath(createMatrixResponse);
		final String webTargetPathScenes = String.format("%s/%s", webTargetPathMatrices, "scenes");

		final Response postScenesResponse = webTarget2.path(webTargetPathScenes).request().post(Entity.text(""));
		final String newlyCreatedSceneLocationPath = getWebTargetPath(postScenesResponse);

		final List<LedStateRow> overlayStationaryStates = asList( //
				new LedStateRow(asList(OFF, OFF, OFF, OFF, OFF, OFF, OFF)), //
				new LedStateRow(asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON)), //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED, UNRECOGNIZED)));
		final OverlayStationary overlayStationaryToPost = new OverlayStationary(overlayStationaryStates,
				new LedRgbColor(255, 0, 0), new LedRgbColor(0, 255, 0), 10);
		webTarget3.path(String.format("%s/%s", newlyCreatedSceneLocationPath, PathLiterals.OVERLAY_STATIONARY))
				.request(APPLICATION_JSON).post(Entity.json(overlayStationaryToPost));

		final List<LedStateRow> overlayRollHorizontallyStates = asList( //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON)), //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED, UNRECOGNIZED)));
		final OverlayRollHorizontally overlayRollHorizontallyToPost = new OverlayRollHorizontally(
				overlayRollHorizontallyStates, new LedRgbColor(255, 0, 0), new LedRgbColor(0, 255, 0), 10, 1);
		webTarget4.path(String.format("%s/%s", newlyCreatedSceneLocationPath, PathLiterals.OVERLAY_ROLL_HORIZONTALLY))
				.request(APPLICATION_JSON).post(Entity.json(overlayRollHorizontallyToPost));

		final Response getMatrixResponse = webTarget5.path(webTargetPathMatrices).request(APPLICATION_JSON).get();
		final MatrixEntity actualMatrixEntity = getMatrixResponse.readEntity(MatrixEntity.class);
		final List<Overlay> overlays = actualMatrixEntity.getScenes().get(0).getOverlays();
		assertThat(overlays.size(), is(2));
		assertThat(overlays.get(0), equalTo(overlayStationaryToPost));
		assertThat(overlays.get(1), equalTo(overlayRollHorizontallyToPost));
	}

	@Test
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void post_matrices_scenes_shouldAddOverlayStationaryAndOverlayRollHorizontallyToSeparateScenes(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2,
			@ArquillianResteasyResource("") final WebTarget webTarget3,
			@ArquillianResteasyResource("") final WebTarget webTarget4,
			@ArquillianResteasyResource("") final WebTarget webTarget5,
			@ArquillianResteasyResource("") final WebTarget webTarget6) {
		final Response createMatrixResponse = sendPostCreatingMatrix(webTarget1, 6, 4);
		final String webTargetPathMatrices = getWebTargetPath(createMatrixResponse);
		final String webTargetPathScenes = String.format("%s/%s", webTargetPathMatrices, "scenes");

		final Response postScenesResponse1 = webTarget2.path(webTargetPathScenes).request().post(Entity.text(""));
		final String newlyCreatedSceneLocationPath1 = getWebTargetPath(postScenesResponse1);

		final Response postScenesResponse2 = webTarget3.path(webTargetPathScenes).request().post(Entity.text(""));
		final String newlyCreatedSceneLocationPath2 = getWebTargetPath(postScenesResponse2);

		final List<LedStateRow> overlayStationaryStates = asList( //
				new LedStateRow(asList(OFF, OFF, OFF, OFF, OFF, OFF, OFF)), //
				new LedStateRow(asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON)), //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED, UNRECOGNIZED)));
		final OverlayStationary overlayStationaryToPost = new OverlayStationary(overlayStationaryStates,
				new LedRgbColor(255, 0, 0), new LedRgbColor(0, 255, 0), 10);
		webTarget4.path(String.format("%s/%s", newlyCreatedSceneLocationPath1, PathLiterals.OVERLAY_STATIONARY))
				.request(APPLICATION_JSON).post(Entity.json(overlayStationaryToPost));

		final List<LedStateRow> overlayRollHorizontallyStates = asList( //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON)), //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED, UNRECOGNIZED)));
		final OverlayRollHorizontally overlayRollHorizontallyToPost = new OverlayRollHorizontally(
				overlayRollHorizontallyStates, new LedRgbColor(255, 0, 0), new LedRgbColor(0, 255, 0), 10, 1);
		webTarget5.path(String.format("%s/%s", newlyCreatedSceneLocationPath2, PathLiterals.OVERLAY_ROLL_HORIZONTALLY))
				.request(APPLICATION_JSON).post(Entity.json(overlayRollHorizontallyToPost));

		final Response getMatrixResponse = webTarget6.path(webTargetPathMatrices).request(APPLICATION_JSON).get();
		final MatrixEntity actualMatrixEntity = getMatrixResponse.readEntity(MatrixEntity.class);
		assertThat(actualMatrixEntity.getScenes().get(0).getOverlays().get(0), equalTo(overlayStationaryToPost));
		assertThat(actualMatrixEntity.getScenes().get(1).getOverlays().get(0), equalTo(overlayRollHorizontallyToPost));
	}

	@Test
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void put_matrices_scenes_overlays_shouldUpdateOverlayStationaryToScene(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2,
			@ArquillianResteasyResource("") final WebTarget webTarget3,
			@ArquillianResteasyResource("") final WebTarget webTarget4,
			@ArquillianResteasyResource("") final WebTarget webTarget5) {
		final Response createMatrixResponse = sendPostCreatingMatrix(webTarget1, 6, 4);
		final String webTargetPathMatrices = getWebTargetPath(createMatrixResponse);
		final String webTargetPathScenes = String.format("%s/%s", webTargetPathMatrices, "scenes");

		final Response postScenesResponse = webTarget2.path(webTargetPathScenes).request().post(Entity.text(""));
		final String newlyCreatedSceneLocationPath = getWebTargetPath(postScenesResponse);

		final List<LedStateRow> overlayStationaryStates1 = asList( //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(ON, LedState.OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON)), //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED, UNRECOGNIZED)));
		final OverlayStationary overlayStationaryToPost1 = new OverlayStationary(overlayStationaryStates1,
				new LedRgbColor(255, 0, 0), new LedRgbColor(0, 255, 0), 10);

		final Response postOverlayStationaryResponse = webTarget3
				.path(String.format("%s/%s", newlyCreatedSceneLocationPath, PathLiterals.OVERLAY_STATIONARY))
				.request(APPLICATION_JSON).post(Entity.json(overlayStationaryToPost1));

		final String actualResponseHeaderLocationPath = getWebTargetPath(postOverlayStationaryResponse);
		final List<LedStateRow> overlayStationaryStates2 = asList( //
				new LedStateRow(asList(OFF, OFF, OFF, OFF, OFF, OFF, OFF)), //
				new LedStateRow(asList(ON, LedState.OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON)), //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED, UNRECOGNIZED)));
		final OverlayStationary overlayStationaryToPut = new OverlayStationary(overlayStationaryStates2,
				new LedRgbColor(255, 0, 0), new LedRgbColor(0, 255, 0), 10);
		final Response putResponse = webTarget4.path(actualResponseHeaderLocationPath).request(APPLICATION_JSON)
				.put(Entity.json(overlayStationaryToPut));
		final int actualResponseStatusCode = putResponse.getStatus();
		assertThat(actualResponseStatusCode, is(equalTo(Response.Status.OK.getStatusCode())));

		final Response getMatrixResponse = webTarget5.path(webTargetPathMatrices).request(APPLICATION_JSON).get();
		final MatrixEntity actualMatrixEntity = getMatrixResponse.readEntity(MatrixEntity.class);
		final List<Overlay> overlays = actualMatrixEntity.getScenes().get(0).getOverlays();
		assertThat(overlays.size(), is(1));
		assertThat(overlays.get(0), equalTo(overlayStationaryToPut));
	}

	@Test
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void delete_matrices_scenes_shouldDeleteScene(@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2,
			@ArquillianResteasyResource("") final WebTarget webTarget3) {
		final Response createMatrixResponse = sendPostCreatingMatrix(webTarget1, 6, 4);
		final String webTargetPathMatrices = getWebTargetPath(createMatrixResponse);
		final String webTargetPathScenes = String.format("%s/%s", webTargetPathMatrices, "scenes");

		final Response postScenesResponse = webTarget2.path(webTargetPathScenes).request().post(Entity.text(""));
		final String actualResponseHeaderLocationPath = getWebTargetPath(postScenesResponse);

		final Response deleteResponse = webTarget3.path(actualResponseHeaderLocationPath).request().delete();

		assertThat(deleteResponse.getStatus(), equalTo(Response.Status.NO_CONTENT.getStatusCode()));

		final Response getMatrixResponse = webTarget3.path(webTargetPathMatrices).request(APPLICATION_JSON).get();
		final MatrixEntity actualMatrixEntity = getMatrixResponse.readEntity(MatrixEntity.class);
		final List<Scene> scenes = actualMatrixEntity.getScenes();
		assertThat(scenes.size(), equalTo(0));
	}

	@Test
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void delete_matrices_scenes_overlays_shouldDeleteOverlayFromScene(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2,
			@ArquillianResteasyResource("") final WebTarget webTarget3,
			@ArquillianResteasyResource("") final WebTarget webTarget4) {
		final Response createMatrixResponse = sendPostCreatingMatrix(webTarget1, 6, 4);
		final String webTargetPathMatrices = getWebTargetPath(createMatrixResponse);
		final String webTargetPathScenes = String.format("%s/%s", webTargetPathMatrices, "scenes");

		final Response postScenesResponse = webTarget2.path(webTargetPathScenes).request().post(Entity.text(""));
		final String newlyCreatedSceneLocationPath = getWebTargetPath(postScenesResponse);

		final List<LedStateRow> overlayStationaryStates = asList( //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(ON, LedState.OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON)), //
				new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
				new LedStateRow(asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED, UNRECOGNIZED)));
		final OverlayStationary overlayStationaryToPost = new OverlayStationary(overlayStationaryStates,
				new LedRgbColor(255, 0, 0), new LedRgbColor(0, 255, 0), 10);

		final Response postOverlayStationaryResponse = webTarget3
				.path(String.format("%s/%s", newlyCreatedSceneLocationPath, PathLiterals.OVERLAY_STATIONARY))
				.request(APPLICATION_JSON).post(Entity.json(overlayStationaryToPost));
		final String actualResponseHeaderLocationPath = getWebTargetPath(postOverlayStationaryResponse);

		final Response deleteResponse = webTarget4.path(actualResponseHeaderLocationPath).request().delete();
		assertThat(deleteResponse.getStatus(), equalTo(Response.Status.NO_CONTENT.getStatusCode()));

		final Response getMatrixResponse = webTarget4.path(webTargetPathMatrices).request(APPLICATION_JSON).get();
		final MatrixEntity actualMatrixEntity = getMatrixResponse.readEntity(MatrixEntity.class);
		final List<Overlay> overlays = actualMatrixEntity.getScenes().get(0).getOverlays();
		assertThat(overlays.size(), equalTo(0));
	}

	////////////////////////////////////////////////////////////////////////////////////

	@Test
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Ignore
	public void post_matrices_scenes_stationary_shoulCreateNewSceneAndAddStationaryOverlayAsFirstElement(
			@ArquillianResteasyResource("") final WebTarget webTarget1,
			@ArquillianResteasyResource("") final WebTarget webTarget2,
			@ArquillianResteasyResource("") final WebTarget webTarget3) {
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

		final Response postResponse = webTarget2.path(webTargetPathScenesStationary).request(APPLICATION_JSON)
				.post(Entity.json(entity));

		final int actualResponseStatusCode = postResponse.getStatus();
		final String actualResponseHeaderLocationPath = getWebTargetPath(postResponse);
		assertThat(actualResponseStatusCode, is(equalTo(Response.Status.CREATED.getStatusCode())));
		assertThat(actualResponseHeaderLocationPath, is(notNullValue()));

		final String[] split = actualResponseHeaderLocationPath.split("/");
		assertThat(split.length, is(6));

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