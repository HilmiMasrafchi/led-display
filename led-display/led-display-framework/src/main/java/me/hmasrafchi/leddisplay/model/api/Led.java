/**
 * 
 */
package me.hmasrafchi.leddisplay.model.api;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * @author michelin
 *
 */
public interface Led {
	void setCoordinateX(double coordinateX);

	void setCoordinateY(double coordinateY);

	void setOpacityLevels(double opacity);

	void setRgbColor(RgbColor rgbColor);

	double getHeight();

	double getWidth();

	RgbColor getRgbColor();

	void setText(String text);

	void setTextFontSize(double ledTextFontSize);

	void reset();

	@Getter
	@ToString
	@EqualsAndHashCode
	@RequiredArgsConstructor
	class RgbColor {
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
	}
}