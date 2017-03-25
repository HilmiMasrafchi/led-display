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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rgbColor == null) ? 0 : rgbColor.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Led other = (Led) obj;
		if (rgbColor == null) {
			if (other.rgbColor != null)
				return false;
		} else if (!rgbColor.equals(other.rgbColor))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}
}