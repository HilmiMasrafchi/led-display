/**
 * 
 */
package me.hmasrafchi.leddisplay.framework;

import me.hmasrafchi.leddisplay.api.LED;

/**
 * @author michelin
 *
 */
public final class Board {
	private final LED[][] leds;

	public Board(final LED[][] leds) {
		this.leds = leds;
	}

	public LED getLED(int x, int y) {
		return leds[x][y];
	}

	public LED[][] getLEDs() {
		return leds;
	}
}