/**
 * 
 */
package me.hmasrafchi.leddisplay.model.scene;

import lombok.RequiredArgsConstructor;
import me.hmasrafchi.leddisplay.model.Led;
import me.hmasrafchi.leddisplay.model.Matrix;

/**
 * @author michelin
 *
 */
@RequiredArgsConstructor
abstract class AbstractScene extends Scene {
	@Override
	public final void nextFrame(final Matrix matrix) {
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
	public final void reset(final Matrix matrix) {
		matrix.stream().forEach(led -> led.reset());
		resetSceneState();
	}

	abstract void resetSceneState();

	abstract void ledIterationEnded();

	abstract void changeLed(final Led led, int ledColumnIndex, int ledRowIndex);
}