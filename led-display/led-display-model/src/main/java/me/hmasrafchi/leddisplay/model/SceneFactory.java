/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import java.util.List;

import me.hmasrafchi.leddisplay.api.RgbColor;
import me.hmasrafchi.leddisplay.api.LedState;
import me.hmasrafchi.leddisplay.api.Scene;
import me.hmasrafchi.leddisplay.util.TwoDimensionalListRectangular;

/**
 * @author michelin
 *
 */
public final class SceneFactory {
	public Overlay getOverlayStationary(final List<? extends List<? extends LedState>> states,
			final RgbColor onColor, final RgbColor offColor, final int duration) {
		return new OverlayStationary(new TwoDimensionalListRectangular<>(states), onColor, offColor, duration);
	}

	public Overlay getOverlayRollHorizontally(final List<? extends List<? extends LedState>> states,
			final RgbColor onColor, final RgbColor offColor, final int beginIndexMark, final int yPosition) {
		return new OverlayRollHorizontally(new TwoDimensionalListRectangular<>(states), onColor, offColor,
				beginIndexMark, yPosition);
	}

	public Scene getSceneOverlayed(final List<? extends Overlay> overlays) {
		return new SceneOverlayed(overlays);
	}

	public Scene getCompositeScene(final List<? extends Scene> scenes) {
		return new SceneComposited(scenes);
	}
}