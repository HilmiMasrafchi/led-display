/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.api.LedRgbColor;
import me.hmasrafchi.leddisplay.api.Scene;

/**
 * @author michelin
 *
 */
@RequiredArgsConstructor
public final class SceneRandomColor implements Scene {
	private static final int FRAMES_COUNT = 3;

	@Getter
	private final List<LedRgbColor> colors;

	private int counter = 1;

	@Override
	public Led onLedVisited(final Led led, final int ledRowIndex, final int ledColumnIndex) {
		final int randomRainbowIndex1 = (int) (Math.random() * (colors.size()));
		final LedRgbColor rgbColor = colors.get(randomRainbowIndex1);

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