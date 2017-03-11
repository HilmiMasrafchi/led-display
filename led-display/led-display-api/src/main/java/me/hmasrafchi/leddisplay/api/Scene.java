/**
 * 
 */
package me.hmasrafchi.leddisplay.api;

/**
 * @author michelin
 *
 */
// TODO: think about how I can hide this interface, maybe using Builder to build
// Matrix using addScenes methods
public interface Scene {
	Led onLedVisited(Led led, int ledRowIndex, int ledColumnIndex);

	void onMatrixIterationEnded();

	boolean isExhausted();
}