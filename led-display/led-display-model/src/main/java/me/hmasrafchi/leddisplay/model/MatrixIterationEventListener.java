/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

/**
 * @author michelin
 *
 */
abstract class MatrixIterationEventListener {
	abstract Led onLedVisited(int ledRowIndex, int ledColumnIndex);

	abstract void onMatrixIterationEnded();
}