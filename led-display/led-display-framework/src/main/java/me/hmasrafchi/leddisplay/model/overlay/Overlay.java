/**
 * 
 */
package me.hmasrafchi.leddisplay.model.overlay;

import java.util.List;

import me.hmasrafchi.leddisplay.model.MatrixEventListener;

/**
 * @author michelin
 *
 */
public interface Overlay extends MatrixEventListener {
	Overlay.State getStateAt(int columnIndex, int rowIndex);

	List<List<Overlay.State>> getStates();

	enum State {
		TRANSPARENT, ON, OFF;
	}
}