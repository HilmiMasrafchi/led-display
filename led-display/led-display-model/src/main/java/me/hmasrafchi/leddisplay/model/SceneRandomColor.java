/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import static java.util.Collections.unmodifiableList;

import java.util.List;

import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.api.RgbColor;
import me.hmasrafchi.leddisplay.api.Scene;

/**
 * @author michelin
 *
 */
final class SceneRandomColor implements Scene {
	private static final int FRAMES_COUNT = 3;

	private final List<RgbColor> colors;

	private int counter = 1;

	SceneRandomColor(final List<? extends RgbColor> colors) {
		this.colors = unmodifiableList(colors);
	}

	@Override
	public Led onLedVisited(final Led led, final int ledRowIndex, final int ledColumnIndex) {
		final int randomRainbowIndex1 = (int) (Math.random() * (colors.size()));
		final RgbColor rgbColor = colors.get(randomRainbowIndex1);

		return new Led(rgbColor);
	}

	@Override
	public void onMatrixIterationEnded() {
		counter++;
	}

	@Override
	public boolean isExhausted() {
		return counter > FRAMES_COUNT;
	}
}