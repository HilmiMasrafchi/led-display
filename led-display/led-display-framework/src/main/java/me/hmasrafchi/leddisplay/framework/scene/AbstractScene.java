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
public abstract class AbstractScene implements Scene {
	@Override
	public final void nextFrame(final Matrix matrix) {
		for (int currentRowIndex = 0; currentRowIndex < matrix.getRowsCount(); currentRowIndex++) {
			for (int currentColumnIndex = 0; currentColumnIndex < matrix.getColumnsCount(); currentColumnIndex++) {
				final Led currentLed = matrix.getLedAt(currentColumnIndex, currentRowIndex);
				determineLedState(currentLed, currentColumnIndex, currentRowIndex);
			}
		}

		iterationEnded();
	}

	abstract void determineLedState(Led currentLed, int currentColumnIndex, int currentRowIndex);

	abstract void iterationEnded();
}