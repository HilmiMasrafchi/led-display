/**
 * 
 */
package me.hmasrafchi.leddisplay.model.scene;

import me.hmasrafchi.leddisplay.model.Matrix;

/**
 * @author michelin
 *
 */
public abstract class Scene {
	public abstract void nextFrame(Matrix matrix);

	abstract boolean hasNextFrame();

	abstract void reset(Matrix matrix);
}