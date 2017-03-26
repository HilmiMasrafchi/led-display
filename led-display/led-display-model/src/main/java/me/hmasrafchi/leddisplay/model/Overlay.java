/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import me.hmasrafchi.leddisplay.api.LedState;
import me.hmasrafchi.leddisplay.api.Scene;

/**
 * @author michelin
 *
 */
// TODO: think how to make it private package
public interface Overlay extends Scene {
	LedState getStateAt(int rowIndex, int columnIndex);
}