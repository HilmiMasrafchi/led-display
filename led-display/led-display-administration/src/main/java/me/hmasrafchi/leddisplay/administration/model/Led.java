/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author michelin
 *
 */
@Data
@ToString
@EqualsAndHashCode
public class Led {
	private static final String DEFAULT_TEXT = "█";
	private static final RgbColor DEFAULT_COLOR = RgbColor.BLACK;

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

	public enum State {
		TRANSPARENT, ON, OFF, UNRECOGNIZED;
	}
}