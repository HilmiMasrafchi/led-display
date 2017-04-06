/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.model;

import javax.persistence.Entity;

/**
 * @author michelin
 *
 */
@Entity
public abstract class Overlay extends Scene {
	abstract Led.State getStateAt(int rowIndex, int columnIndex);
}