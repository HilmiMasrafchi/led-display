/**
 * 
 */
package me.hmasrafchi.leddisplay.api;

/**
 * @author michelin
 *
 */
public interface Board {
	void nextFrame();

	void startAnimation();

	void pauseAnimation();

	void stopAnimation();
}