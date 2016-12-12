/**
 * 
 */
package me.hmasrafchi.leddisplay.infrastructure;

import me.hmasrafchi.leddisplay.model.api.Led;

/**
 * @author michelin
 *
 */
public abstract class GeneratorLed {
	abstract Led next();

	abstract double getLedMaximumWidth();

	abstract double getLedMaximumHeight();
}