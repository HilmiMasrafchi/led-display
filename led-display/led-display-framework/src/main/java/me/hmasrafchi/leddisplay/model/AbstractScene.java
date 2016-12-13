/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import lombok.RequiredArgsConstructor;

/**
 * @author michelin
 *
 */
@RequiredArgsConstructor
abstract class AbstractScene extends Scene {
	@Override
	public final void nextFrame() {
	}

	@Override
	final void reset(final MatrixIterator matrixIterator) {
		matrixIterator.iterate((led, columnIndex, rowIndex) -> led.reset());
		resetSceneState();
	}

	abstract void resetSceneState();
}