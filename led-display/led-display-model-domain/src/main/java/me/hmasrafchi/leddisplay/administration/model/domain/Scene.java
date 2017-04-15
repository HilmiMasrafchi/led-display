/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.model.domain;

/**
 * @author michelin
 *
 */
public abstract class Scene {
	abstract Led onLedVisited(Led led, int ledRowIndex, int ledColumnIndex);

	abstract void onMatrixIterationEnded();

	abstract boolean isExhausted();
}