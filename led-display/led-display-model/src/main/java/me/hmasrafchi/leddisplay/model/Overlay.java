/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import me.hmasrafchi.leddisplay.api.LedState;

/**
 * @author michelin
 *
 */
public abstract class Overlay extends Scene {
	abstract LedState getStateAt(int rowIndex, int columnIndex);
}