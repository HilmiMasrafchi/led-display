/**
 * 
 */
package me.hmasrafchi.leddisplay.framework;

import java.util.List;

/**
 * @author michelin
 *
 */
public interface Scene {
	boolean hasNext();

	void iterateLeds(List<? extends List<? extends Led>> leds);
}