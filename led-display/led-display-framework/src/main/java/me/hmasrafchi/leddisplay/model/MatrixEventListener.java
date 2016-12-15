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
	void onLedVisited(Led led, int currentLedColumnIndex, int currentLedRowIndex);

	void onMatrixReset();

	void onMatrixIterationEnded();
}