/**
 * 
 */
package me.hmasrafchi.leddisplay.domain;

/**
 * @author michelin
 *
 */
public abstract class Overlay extends Scene {
	abstract Led.State getStateAt(int rowIndex, int columnIndex);
}