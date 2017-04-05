/**
 * 
 */
package me.hmasrafchi.leddisplay.consumer.model;

import me.hmasrafchi.leddisplay.api.LedState;

/**
 * @author michelin
 *
 */
// TODO: think how to make it private package
interface Overlay {
	LedState getStateAt(int rowIndex, int columnIndex);
}