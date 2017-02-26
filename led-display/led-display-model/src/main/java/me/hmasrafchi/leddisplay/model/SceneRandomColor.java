/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.api.LedRgbColor;

/**
 * @author michelin
 *
 */
@RequiredArgsConstructor
public final class SceneRandomColor extends Scene {
	private static final int FRAMES_COUNT = 3;

	@Getter
	private final List<LedRgbColor> colors;

	private int counter = 1;

	@Override
	Led onLedVisited(final int ledRowIndex, final int ledColumnIndex) {
		final int randomRainbowIndex1 = (int) (Math.random() * (colors.size()));
		final LedRgbColor rgbColor = colors.get(randomRainbowIndex1);

		return new Led(rgbColor);
	}

	@Override
	void onMatrixIterationEnded() {
		counter++;
	}

	@Override
	boolean isExhausted() {
		return counter > FRAMES_COUNT;
	}
}