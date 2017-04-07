/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application;

import java.net.URI;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import me.hmasrafchi.leddisplay.administration.model.Scene;
import me.hmasrafchi.leddisplay.administration.model.SceneComposite;

/**
 * @author michelin
 *
 */
public class TestResource {
	Response sendPostCreateScene(final WebTarget webTarget) {
		final SceneComposite scene = new SceneComposite();
		return webTarget.path("scenes").request(MediaType.APPLICATION_JSON).post(Entity.json(scene));
	}

	Scene getScene(final WebTarget webTarget, final String path) {
		final Scene readEntity = webTarget.path(path).request(MediaType.APPLICATION_JSON).get().readEntity(Scene.class);
		return readEntity;
	}

	String getWebTargetPath(final Response response) {
		final URI location = response.getLocation();
		final String locationPath = location.getPath();
		final String locationPathWithoutLeading = locationPath.substring(1);

		return locationPathWithoutLeading.substring(locationPathWithoutLeading.indexOf("/") + 1);
	}
}