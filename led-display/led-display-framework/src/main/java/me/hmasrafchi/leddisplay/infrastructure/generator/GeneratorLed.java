/**
 * 
 */
package me.hmasrafchi.leddisplay.infrastructure.generator;

import me.hmasrafchi.leddisplay.model.Led;

/**
 * @author michelin
 *
 */
public abstract class GeneratorLed {
	abstract Led next();

	abstract double getLedMaximumWidth();

	abstract double getLedMaximumHeight();
}