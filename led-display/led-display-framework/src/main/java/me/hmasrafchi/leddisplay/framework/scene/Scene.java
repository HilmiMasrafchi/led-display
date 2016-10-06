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
	boolean hasNext();

	void nextFrame(Matrix matrix);
}