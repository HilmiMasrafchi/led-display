/**
 * 
 */
package me.hmasrafchi.leddisplay.framework.scene;

import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.framework.Matrix;

/**
 * @author michelin
 *
 */
public abstract class Scene {
	public Scene() {

	}

	public void nextFrame(final Matrix matrix) {
		for (int currentLedRowIndex = 0; currentLedRowIndex < matrix.getRowsCount(); currentLedRowIndex++) {
			for (int currentLedColumnIndex = 0; currentLedColumnIndex < matrix
					.getColumnsCount(); currentLedColumnIndex++) {
				changeLed(matrix, currentLedColumnIndex, currentLedRowIndex);
			}
		}

		matrixIterationEnded();
	}

	public void reset(final Matrix matrix) {
		for (int currentLedRowIndex = 0; currentLedRowIndex < matrix.getRowsCount(); currentLedRowIndex++) {
			for (int currentLedColumnIndex = 0; currentLedColumnIndex < matrix
					.getColumnsCount(); currentLedColumnIndex++) {
				final Led currentLed = matrix.getLedAt(currentLedColumnIndex, currentLedRowIndex);
				currentLed.reset();
			}
		}

		resetInternalState();
	}

	public abstract boolean hasNextFrame();

	protected abstract void resetInternalState();

	protected abstract void matrixIterationEnded();

	protected abstract void changeLed(final Matrix matrix, int ledColumnIndex, int ledRowIndex);
}