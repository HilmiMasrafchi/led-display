/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author michelin
 *
 */
@Getter
@EqualsAndHashCode
public final class Led {
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

	@Getter
	@EqualsAndHashCode
	@RequiredArgsConstructor
	public static class RgbColor {
		public static final RgbColor WHITE = new RgbColor(255, 255, 255);
		public static final RgbColor BLACK = new RgbColor(0, 0, 0);

		public static final RgbColor RED = new RgbColor(255, 0, 0);
		public static final RgbColor ORANGE = new RgbColor(255, 127, 0);
		public static final RgbColor YELLOW = new RgbColor(255, 255, 0);
		public static final RgbColor GREEN = new RgbColor(0, 255, 0);
		public static final RgbColor BLUE = new RgbColor(0, 0, 255);
		public static final RgbColor INDIGO = new RgbColor(75, 0, 130);

		private final int r;
		private final int g;
		private final int b;

		@Override
		public String toString() {
			return String.format("(%d, %d, %d)", r, g, b);
		}
	}

	@Override
	public String toString() {
		return "Led [" + rgbColor + "]";
	}
}