/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import me.hmasrafchi.leddisplay.model.api.Led;

/**
 * @author michelin
 *
 */
public interface MatrixEventListener {
	boolean isExhausted();

	void onLedVisited(Led led, int currentLedColumnIndex, int currentLedRowIndex);

	void onMatrixReset();

	void onMatrixIterationEnded();
}