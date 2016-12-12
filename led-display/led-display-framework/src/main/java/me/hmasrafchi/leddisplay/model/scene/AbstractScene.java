/**
 * 
 */
package me.hmasrafchi.leddisplay.model.scene;

import lombok.RequiredArgsConstructor;
import me.hmasrafchi.leddisplay.model.Led;

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
	public final void reset(final MatrixIterator matrixIterator) {
		matrixIterator.iterate((led, columnIndex, rowIndex) -> led.reset());
		resetSceneState();
	}

	abstract void resetSceneState();

	abstract void ledIterationEnded();

	abstract void changeLed(final Led led, int ledColumnIndex, int ledRowIndex);
}