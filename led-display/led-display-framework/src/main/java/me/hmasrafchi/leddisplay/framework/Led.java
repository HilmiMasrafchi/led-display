/**
 * 
 */
package me.hmasrafchi.leddisplay.framework;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author michelin
 *
 */
public interface Led {
	void setCoordinateX(double x);

	double getCoordinateX();

	void setCoordinateY(double y);

	double getCoordinateY();

	double getHeight();

	double getWidth();

	void setOpacityLevels(double opacity);

	void setRgbColor(RgbColor rgbColor);

	@Getter
	@RequiredArgsConstructor
	class RgbColor {
		public static final RgbColor INDIGO = new RgbColor(75, 0, 130);
		public static final RgbColor BLUE = new RgbColor(0, 0, 255);
		public static final RgbColor GREEN = new RgbColor(0, 255, 0);
		public static final RgbColor YELLOW = new RgbColor(255, 255, 0);
		public static final RgbColor ORANGE = new RgbColor(255, 127, 0);
		public static final RgbColor RED = new RgbColor(255, 0, 0);

		private final int r;
		private final int g;
		private final int b;
	}
}