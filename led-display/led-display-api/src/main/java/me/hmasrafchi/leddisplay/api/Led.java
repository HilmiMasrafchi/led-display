/**
 * 
 */
package me.hmasrafchi.leddisplay.api;

import static me.hmasrafchi.leddisplay.api.LedRgbColor.BLACK;

/**
 * @author michelin
 *
 */
public class Led {
	private static final String DEFAULT_TEXT = "█";
	private static final LedRgbColor DEFAULT_COLOR = BLACK;

	private final String text;
	private final LedRgbColor rgbColor;

	public Led() {
		this(DEFAULT_TEXT, DEFAULT_COLOR);
	}

	public Led(final LedRgbColor rgbColor) {
		this(DEFAULT_TEXT, rgbColor);
	}

	public Led(final String text, final LedRgbColor rgbColor) {
		this.text = text;
		this.rgbColor = rgbColor;
	}

	public LedRgbColor getRgbColor() {
		return rgbColor;
	}

	public String getText() {
		return text;
	}
}