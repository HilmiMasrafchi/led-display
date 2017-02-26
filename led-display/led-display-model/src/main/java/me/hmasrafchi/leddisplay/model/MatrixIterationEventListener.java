/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import me.hmasrafchi.leddisplay.api.Led;

/**
 * @author michelin
 *
 */
abstract class MatrixIterationEventListener {
	abstract Led onLedVisited(int ledRowIndex, int ledColumnIndex);

	abstract void onMatrixIterationEnded();
}