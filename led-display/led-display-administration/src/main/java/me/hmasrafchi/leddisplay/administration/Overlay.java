/**
 * 
 */
package me.hmasrafchi.leddisplay.administration;

/**
 * @author michelin
 *
 */
// TODO: think how to make it private package
interface Overlay {
	LedState getStateAt(int rowIndex, int columnIndex);
}