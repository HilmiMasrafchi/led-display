/**
 * 
 */
package me.hmasrafchi.leddisplay.model.overlay;

import me.hmasrafchi.leddisplay.model.Scene;

/**
 * @author michelin
 *
 */
public interface Overlay extends Scene {
	State getStateAt(int columnIndex, int rowIndex);

	enum State {
		TRANSPARENT, ON, OFF;
	}
}