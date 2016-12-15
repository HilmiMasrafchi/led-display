/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import java.util.Arrays;
import java.util.List;

import lombok.RequiredArgsConstructor;
import me.hmasrafchi.leddisplay.model.api.Led;
import me.hmasrafchi.leddisplay.model.api.Led.RgbColor;

/**
 * @author michelin
 *
 */
@RequiredArgsConstructor
public final class SceneRandomColor implements Scene {
	private static final int FRAMES_COUNT = 10;

	private final static List<Double> OPACITY_VALUES = Arrays.asList(new Double(0.3), new Double(0.4), new Double(0.5),
			new Double(0.6), new Double(0.7), new Double(0.8), new Double(0.9), new Double(1));

	private final List<RgbColor> colors;

	private int counter = 1;

	@Override
	public void onLedVisited(final Led led, final int currentLedColumnIndex, final int currentLedRowIndex) {
		final int randomRainbowIndex1 = (int) (Math.random() * (colors.size()));
		final RgbColor rgbColor = colors.get(randomRainbowIndex1);
		led.setRgbColor(rgbColor);

		final int randomRainbowIndex2 = (int) (Math.random() * (colors.size() - 1));
		led.setOpacityLevels(OPACITY_VALUES.get(randomRainbowIndex2));
	}

	@Override
	public boolean isExhausted() {
		return counter >= FRAMES_COUNT;
	}

	@Override
	public void onMatrixReset() {
		counter = 1;
	}

	@Override
	public void onMatrixIterationEnded() {
		counter++;
	}
}