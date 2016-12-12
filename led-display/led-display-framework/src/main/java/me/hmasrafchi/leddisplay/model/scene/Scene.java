/**
 * 
 */
package me.hmasrafchi.leddisplay.model.scene;

/**
 * @author michelin
 *
 */
public abstract class Scene {
	public abstract void nextFrame(final MatrixIterator matrixIterator);

	abstract boolean hasNextFrame();

	abstract void reset(MatrixIterator matrixIterator);
}