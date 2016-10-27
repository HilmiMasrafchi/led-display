/**
 * 
 */
package me.hmasrafchi.leddisplay.framework.scene.overlay;

import me.hmasrafchi.leddisplay.api.Led;

/**
 * @author michelin
 *
 */
public interface Overlay {
	Overlay.State getStateAt(int columnIndex, int rowIndex);

	void changeLed(Led led, int ledColumndIndex, int ledRowIndex);

	void matrixIterationEnded();

	boolean isEndReached();

	void reset();

	enum State {
		TRANSPARENT, STATIONARY_ON, STATIONARY_OFF, ROLL_ON;
	}
}