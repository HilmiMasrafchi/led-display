/**
 * 
 */
package me.hmasrafchi.leddisplay.rest;

import java.net.URI;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import me.hmasrafchi.leddisplay.rest.persist.MatrixEntity;

/**
 * @author michelin
 *
 */
public class TestResource {
	Response sendPostCreatingMatrix(final WebTarget webTarget, final int rowCount, final int columnCount) {
		final MatrixEntity entity = new MatrixEntity();
		entity.setRowCount(rowCount);
		entity.setColumnCount(columnCount);

		return webTarget.path("matrices").request(MediaType.APPLICATION_JSON).post(Entity.json(entity));
	}

	String getWebTargetPath(final Response response) {
		final URI location = response.getLocation();
		final String locationPath = location.getPath();
		final String locationPathWithoutLeading = locationPath.substring(1);

		return locationPathWithoutLeading.substring(locationPathWithoutLeading.indexOf("/") + 1);
	}
}