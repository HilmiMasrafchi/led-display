/**
 * 
 */
package me.hmasrafchi.leddisplay.framework.scene.overlay;

import java.util.List;

import me.hmasrafchi.leddisplay.api.Led;

/**
 * @author michelin
 *
 */
public interface Overlay {
	Overlay.State getStateAt(int columnIndex, int rowIndex);

	List<List<Overlay.State>> getStates();

	void changeLed(Led led, int ledColumndIndex, int ledRowIndex);

	void matrixIterationEnded();

	boolean isEndReached();

	void reset();

	enum State {
		TRANSPARENT, ON, OFF;
	}
}