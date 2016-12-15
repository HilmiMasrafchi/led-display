/**
 * 
 */
package me.hmasrafchi.leddisplay.model.overlay;

import java.util.List;

import me.hmasrafchi.leddisplay.model.Scene;

/**
 * @author michelin
 *
 */
public interface Overlay extends Scene {
	Overlay.State getStateAt(int columnIndex, int rowIndex);

	List<List<Overlay.State>> getStates();

	enum State {
		TRANSPARENT, ON, OFF;
	}
}