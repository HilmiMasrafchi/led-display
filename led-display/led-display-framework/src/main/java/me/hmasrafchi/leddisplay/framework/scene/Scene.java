/**
 * 
 */
package me.hmasrafchi.leddisplay.framework.scene;

import java.util.List;

import me.hmasrafchi.leddisplay.api.Led;

/**
 * @author michelin
 *
 */
public abstract class Scene {
	public void nextFrame(final List<List<Led>> leds) {
		for (int currentLedRowIndex = 0; currentLedRowIndex < leds.size(); currentLedRowIndex++) {
			for (int currentLedColumnIndex = 0; currentLedColumnIndex < leds.get(0).size(); currentLedColumnIndex++) {
				changeLed(leds.get(currentLedRowIndex).get(currentLedColumnIndex), currentLedColumnIndex,
						currentLedRowIndex);
			}
		}

		matrixIterationEnded();
	}

	public void reset(final List<List<Led>> leds) {
		leds.stream().flatMap(row -> row.stream()).forEach(led -> led.reset());
		resetInternalState();
	}

	public abstract boolean hasNextFrame();

	protected abstract void resetInternalState();

	protected abstract void matrixIterationEnded();

	protected abstract void changeLed(final Led led, int ledColumnIndex, int ledRowIndex);
}