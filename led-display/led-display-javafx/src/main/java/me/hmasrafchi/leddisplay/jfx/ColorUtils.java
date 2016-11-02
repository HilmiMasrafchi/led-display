/**
 * 
 */
package me.hmasrafchi.leddisplay.jfx;

import javafx.scene.paint.Color;
import me.hmasrafchi.leddisplay.api.Led;

/**
 * @author michelin
 *
 */
public final class ColorUtils {
	public static Color toJavaFxColor(final Led.RgbColor ledRgbColor) {
		final int red = ledRgbColor.getR();
		final int green = ledRgbColor.getG();
		final int blue = ledRgbColor.getB();

		return new Color(red / 255d, green / 255d, blue / 255d, 1d);
	}

	public static Led.RgbColor toLedRgbColor(final Color javaFxColor) {
		final double red = javaFxColor.getRed();
		final double green = javaFxColor.getGreen();
		final double blue = javaFxColor.getBlue();

		return new Led.RgbColor((int) (red * 255), (int) (green * 255), (int) (blue * 255));
	}
}