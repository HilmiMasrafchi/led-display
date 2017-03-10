/**
 * 
 */
package me.hmasrafchi.leddisplay.api;

import static me.hmasrafchi.leddisplay.api.RgbColor.BLACK;

/**
 * @author michelin
 *
 */
public class Led {
	private static final String DEFAULT_TEXT = "█";
	private static final RgbColor DEFAULT_COLOR = BLACK;

	private final String text;
	private final RgbColor rgbColor;

	public Led() {
		this(DEFAULT_TEXT, DEFAULT_COLOR);
	}

	public Led(final RgbColor rgbColor) {
		this(DEFAULT_TEXT, rgbColor);
	}

	public Led(final String text, final RgbColor rgbColor) {
		this.text = text;
		this.rgbColor = rgbColor;
	}

	public RgbColor getRgbColor() {
		return rgbColor;
	}

	public String getText() {
		return text;
	}
}