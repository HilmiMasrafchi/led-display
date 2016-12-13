/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import me.hmasrafchi.leddisplay.model.api.Led;

/**
 * @author michelin
 *
 */
public abstract class Scene {
	public abstract void nextFrame();

	public abstract boolean hasNextFrame();

	abstract void reset(MatrixIterator matrixIterator);

	abstract void changeLed(final Led led, int ledColumnIndex, int ledRowIndex);

	abstract void ledIterationEnded();
}