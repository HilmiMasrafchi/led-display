/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

/**
 * @author michelin
 *
 */
public abstract class Overlay extends Scene {
	abstract State getStateAt(int rowIndex, int columnIndex);

	public enum State {
		TRANSPARENT, ON, OFF, UNRECOGNIZED;
	}
}