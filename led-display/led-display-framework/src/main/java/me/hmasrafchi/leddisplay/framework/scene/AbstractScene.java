/**
 * 
 */
package me.hmasrafchi.leddisplay.framework.scene;

import lombok.RequiredArgsConstructor;
import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.framework.Matrix;

/**
 * @author michelin
 *
 */
@RequiredArgsConstructor
public abstract class AbstractScene implements Scene {
	@Override
	public void nextFrame(final Matrix matrix) {
		for (int currentLedRowIndex = 0; currentLedRowIndex < matrix.getRowCount(); currentLedRowIndex++) {
			for (int currentLedColumnIndex = 0; currentLedColumnIndex < matrix
					.getColumnCount(); currentLedColumnIndex++) {
				final Led led = matrix.getLedAt(currentLedColumnIndex, currentLedRowIndex);
				changeLed(led, currentLedColumnIndex, currentLedRowIndex);
			}
		}

		ledIterationEnded();
	}

	@Override
	public void reset(final Matrix matrix) {
		matrix.stream().forEach(led -> led.reset());
		resetSceneState();
	}

	abstract void resetSceneState();

	abstract void ledIterationEnded();

	abstract void changeLed(final Led led, int ledColumnIndex, int ledRowIndex);
}