/**
 * 
 */
package me.hmasrafchi.leddisplay.api;

import static me.hmasrafchi.leddisplay.api.LedRgbColor.BLACK;

import lombok.Data;

/**
 * @author michelin
 *
 */
@Data
public class Led {
	private static final String DEFAULT_TEXT = "█";
	private static final LedRgbColor DEFAULT_COLOR = BLACK;

	private String text;
	private LedRgbColor rgbColor;

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
}