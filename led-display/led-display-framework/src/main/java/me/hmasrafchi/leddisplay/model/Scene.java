/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

/**
 * @author michelin
 *
 */
public abstract class Scene {
	public abstract void nextFrame(MatrixIterator matrixIterator);

	abstract boolean hasNextFrame();

	abstract void reset(MatrixIterator matrixIterator);
}