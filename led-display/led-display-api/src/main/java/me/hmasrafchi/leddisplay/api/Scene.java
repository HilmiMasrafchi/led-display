/**
 * 
 */
package me.hmasrafchi.leddisplay.api;

/**
 * @author michelin
 *
 */
public interface Scene {
	Led onLedVisited(Led led, int ledRowIndex, int ledColumnIndex);

	void onMatrixIterationEnded();

	boolean isExhausted();
}