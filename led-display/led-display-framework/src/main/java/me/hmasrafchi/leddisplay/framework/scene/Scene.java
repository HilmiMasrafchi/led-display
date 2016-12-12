/**
 * 
 */
package me.hmasrafchi.leddisplay.framework.scene;

import me.hmasrafchi.leddisplay.framework.Matrix;

/**
 * @author michelin
 *
 */
public interface Scene {
	boolean hasNextFrame();

	void nextFrame(Matrix matrix);

	void reset(Matrix matrix);
}