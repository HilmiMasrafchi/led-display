/**
 * 
 */
package me.hmasrafchi.leddisplay.rest.persist;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author michelin
 *
 */
@Data
@Entity
@EqualsAndHashCode(of = { "id" })
public class MatrixEntity {
	@Id
	@GeneratedValue
	int id;
	int columnCount;
	int rowCount;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn
	List<Scene> scenes;

	// public void addOverlayStationary(final int sceneIndex, final
	// TwoDimensionalListRectangular<LedState> states,
	// final LedRgbColor onColor, final LedRgbColor offColor, final int
	// duration) {
	// final Scene scene = getScenes().get(sceneIndex);
	// final SceneOverlayed sceneOverlayed = (SceneOverlayed) scene;
	// sceneOverlayed.addOverlay(new OverlayStationary(states, onColor,
	// offColor, duration));
	// }
	//
	// public void addOverlayStationaryAt(final int sceneIndex, final int
	// overlayIndex,
	// final TwoDimensionalListRectangular<LedState> states, final LedRgbColor
	// onColor, final LedRgbColor offColor,
	// final int duration) {
	// final Scene scene = getScenes().get(sceneIndex);
	// final SceneOverlayed sceneOverlayed = (SceneOverlayed) scene;
	// sceneOverlayed.addOverlayAt(overlayIndex, new OverlayStationary(states,
	// onColor, offColor, duration));
	// }
	//
	// public void addOverlayRollHorizontally(final int sceneIndex, final
	// TwoDimensionalListRectangular<LedState> states,
	// final LedRgbColor onColor, final LedRgbColor offColor, final int
	// beginIndexMark, final int yPosition) {
	// final Scene scene = getScenes().get(sceneIndex);
	// final SceneOverlayed sceneOverlayed = (SceneOverlayed) scene;
	// sceneOverlayed.addOverlay(new OverlayRollHorizontally(states, onColor,
	// offColor, beginIndexMark, yPosition));
	// }
	//
	// public void addOverlayRollHorizontallyAt(final int sceneIndex, final int
	// overlayIndex,
	// final TwoDimensionalListRectangular<LedState> states, final LedRgbColor
	// onColor, final LedRgbColor offColor,
	// final int beginIndexMark, final int yPosition) {
	// final Scene scene = getScenes().get(sceneIndex);
	// final SceneOverlayed sceneOverlayed = (SceneOverlayed) scene;
	// sceneOverlayed.addOverlayAt(overlayIndex,
	// new OverlayRollHorizontally(states, onColor, offColor, beginIndexMark,
	// yPosition));
	// }
}