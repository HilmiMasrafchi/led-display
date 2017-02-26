/**
 * 
 */
package me.hmasrafchi.leddisplay.api;

import lombok.Data;

/**
 * @author michelin
 *
 */
@Data
public class LedRgbColor {
	public static final LedRgbColor WHITE = new LedRgbColor(255, 255, 255);
	public static final LedRgbColor BLACK = new LedRgbColor(0, 0, 0);

	public static final LedRgbColor RED = new LedRgbColor(255, 0, 0);
	public static final LedRgbColor ORANGE = new LedRgbColor(255, 127, 0);
	public static final LedRgbColor YELLOW = new LedRgbColor(255, 255, 0);
	public static final LedRgbColor GREEN = new LedRgbColor(0, 255, 0);
	public static final LedRgbColor BLUE = new LedRgbColor(0, 0, 255);
	public static final LedRgbColor INDIGO = new LedRgbColor(75, 0, 130);

	private int r;
	private int g;
	private int b;

	public LedRgbColor() {
	}

	public LedRgbColor(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	@Override
	public String toString() {
		return String.format("(%d, %d, %d)", r, g, b);
	}
}