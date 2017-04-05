/**
 * 
 */
package me.hmasrafchi.leddisplay.api;

import java.util.List;

/**
 * @author michelin
 *
 */
public interface Matrix {
	void appendStationaryOverlay(List<List<LedState>> states, RgbColor onColor, RgbColor offColor, int duration);

	void appendStationaryOverlayToSpecificScene(int sceneIndex, List<List<LedState>> states, RgbColor onColor,
			RgbColor offColor, int duration);

	void addStationaryOverlayToSpecificSceneAndPosition(int sceneIndex, int overlayIndex, List<List<LedState>> states,
			RgbColor onColor, RgbColor offColor, int duration);

	void replaceWithStationaryOverlayAtSpecificSceneAndPosition(int sceneIndex, int overlayIndex,
			List<List<LedState>> states, RgbColor onColor, RgbColor offColor, int duration);

	void appendRollHorizontallyOverlay(List<List<LedState>> states, RgbColor onColor, RgbColor offColor, int yPosition);

	void appendRollHorizontallyOverlayToSpecificScene(int sceneIndex, List<List<LedState>> states, RgbColor onColor,
			RgbColor offColor, int yPosition);

	void addRollHorizontallyOverlayToSpecificSceneAndPosition(int sceneIndex, int overlayIndex,
			List<List<LedState>> states, RgbColor onColor, RgbColor offColor, int yPosition);

	void replaceWithRollHorizontallyOverlayAtSpecificSceneAndPosition(int sceneIndex, int overlayIndex,
			List<List<LedState>> states, RgbColor onColor, RgbColor offColor, int yPosition);

	CompiledFrames getCompiledFrames();
}