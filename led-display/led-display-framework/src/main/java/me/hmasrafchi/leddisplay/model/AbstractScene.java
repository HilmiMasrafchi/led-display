/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import lombok.RequiredArgsConstructor;
import me.hmasrafchi.leddisplay.model.api.Led;

/**
 * @author michelin
 *
 */
@RequiredArgsConstructor
abstract class AbstractScene extends Scene {
	@Override
	public final void nextFrame(final MatrixIterator matrixIterator) {
		matrixIterator.iterate((led, columnIndex, rowIndex) -> changeLed(led, columnIndex, rowIndex));
		ledIterationEnded();
	}

	@Override
	final void reset(final MatrixIterator matrixIterator) {
		matrixIterator.iterate((led, columnIndex, rowIndex) -> led.reset());
		resetSceneState();
	}

	abstract void resetSceneState();

	abstract void ledIterationEnded();

	abstract void changeLed(final Led led, int ledColumnIndex, int ledRowIndex);
}