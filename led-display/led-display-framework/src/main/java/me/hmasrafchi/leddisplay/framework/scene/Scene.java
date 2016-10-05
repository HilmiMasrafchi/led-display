/**
 * 
 */
package me.hmasrafchi.leddisplay.framework.scene;

import me.hmasrafchi.leddisplay.api.Led;

/**
 * @author michelin
 *
 */
public interface Scene {
	boolean hasNext();

	void nextFrame(Led led, int x, int y);
}