/**
 * 
 */
package me.hmasrafchi.leddisplay.consumer;

import javax.persistence.Embeddable;

import lombok.Data;

/**
 * @author michelin
 *
 */
@Data
@Embeddable
// TODO: make inner class of Led
public class RgbColor {
	public static final RgbColor WHITE = new RgbColor(255, 255, 255);
	public static final RgbColor BLACK = new RgbColor(0, 0, 0);

	public static final RgbColor RED = new RgbColor(255, 0, 0);
	public static final RgbColor ORANGE = new RgbColor(255, 127, 0);
	public static final RgbColor YELLOW = new RgbColor(255, 255, 0);
	public static final RgbColor GREEN = new RgbColor(0, 255, 0);
	public static final RgbColor BLUE = new RgbColor(0, 0, 255);
	public static final RgbColor INDIGO = new RgbColor(75, 0, 130);

	private int r;
	private int g;
	private int b;

	public RgbColor() {
	}

	public RgbColor(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
}