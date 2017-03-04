/**
 * 
 */
package me.hmasrafchi.leddisplay.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import me.hmasrafchi.leddisplay.rest.persist.MatrixRepository;
import me.hmasrafchi.leddisplay.rest.persist.Overlay;
import me.hmasrafchi.leddisplay.rest.persist.OverlayRollHorizontally;
import me.hmasrafchi.leddisplay.rest.persist.OverlayStationary;
import me.hmasrafchi.leddisplay.rest.persist.Scene;

/**
 * @author michelin
 *
 */
@Stateless
public class SceneResource {
	@Inject
	MatrixRepository matrixRepository;

	public SceneResource() {
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{sceneIndex}/overlays/stationary")
	public void addOverlayStationary(@PathParam("matrixId") final int matrixId,
			@PathParam("sceneIndex") final int sceneIndex, final OverlayStationary addOverlayStationary) {
		add(matrixId, sceneIndex, addOverlayStationary);
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{sceneIndex}/overlays/{overlayIndex}/stationary")
	public void addOverlayStationaryAt(@PathParam("matrixId") final int matrixId,
			@PathParam("sceneIndex") final int sceneIndex, @PathParam("overlayIndex") final int overlayIndex,
			final OverlayStationary addOverlayStationary) {
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{sceneIndex}/overlays/roll")
	public void addOverlayRollHorizontally(@PathParam("matrixId") final int matrixId,
			@PathParam("sceneIndex") final int sceneIndex, final OverlayRollHorizontally addOverlayRollHorizontally) {
		add(matrixId, sceneIndex, addOverlayRollHorizontally);
	}

	private void add(Integer matrixId, Integer sceneIndex, Overlay overlay) {
		matrixRepository.get(matrixId).ifPresent(matrixEntity -> {
			final List<Scene> scenes = matrixEntity.getScenes();
			if (sceneIndex == scenes.size()) {
				scenes.add(new Scene());
			}

			scenes.get(sceneIndex).getOverlays().add(overlay);
			matrixRepository.update(matrixEntity);
		});
	}
}