/**
 * 
 */
package me.hmasrafchi.leddisplay.model.domain;

/**
 * @author michelin
 *
 */
public abstract class Overlay extends Scene {
	abstract Led.State getStateAt(int rowIndex, int columnIndex);
}