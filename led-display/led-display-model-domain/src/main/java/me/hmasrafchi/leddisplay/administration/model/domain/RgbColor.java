/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.model.domain;

import lombok.Value;

/**
 * @author michelin
 *
 */
// TODO: make inner class of Led
@Value
public class RgbColor {
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