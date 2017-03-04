/**
 * 
 */
package me.hmasrafchi.leddisplay.api;

/**
 * @author michelin
 *
 */
public interface MatrixIterationEventListener {
	Led onLedVisited(Led led, int ledRowIndex, int ledColumnIndex);

	void onMatrixIterationEnded();
}