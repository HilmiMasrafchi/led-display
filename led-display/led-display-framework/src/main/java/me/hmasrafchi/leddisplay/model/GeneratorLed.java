/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import me.hmasrafchi.leddisplay.api.Led;

/**
 * @author michelin
 *
 */
public interface GeneratorLed {
	Led next();

	double getLedMaximumWidth();

	double getLedMaximumHeight();
}