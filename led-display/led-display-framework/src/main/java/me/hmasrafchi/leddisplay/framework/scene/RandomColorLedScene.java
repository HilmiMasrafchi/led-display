/**
 * 
 */
package me.hmasrafchi.leddisplay.framework.scene;

import java.util.Arrays;
import java.util.List;

import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.api.Led.RgbColor;

/**
 * @author michelin
 *
 */
public final class RandomColorLedScene implements Scene {
	private final static List<Led.RgbColor> RAINBOW_COLORS = Arrays.asList(Led.RgbColor.INDIGO, Led.RgbColor.BLUE,
			Led.RgbColor.GREEN, Led.RgbColor.YELLOW, Led.RgbColor.ORANGE, Led.RgbColor.RED);
	private final static List<Double> OPACITY_VALUES = Arrays.asList(new Double(0.3), new Double(0.4), new Double(0.5),
			new Double(0.6), new Double(0.7), new Double(0.8), new Double(0.9), new Double(1));

	@Override
	public boolean hasNext() {
		return true;
	}

	@Override
	public void nextFrame(final Led led, int x, int y) {
		final int randomRainbowIndex1 = (int) (Math.random() * (RAINBOW_COLORS.size() - 1));
		final RgbColor rgbColor = RAINBOW_COLORS.get(randomRainbowIndex1);
		led.setRgbColor(rgbColor);

		final int randomRainbowIndex2 = (int) (Math.random() * (RAINBOW_COLORS.size() - 1));
		led.setOpacityLevels(OPACITY_VALUES.get(randomRainbowIndex2));
	}
}