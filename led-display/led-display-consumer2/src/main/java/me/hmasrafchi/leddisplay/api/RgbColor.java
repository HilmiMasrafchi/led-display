/**
 * 
 */
package me.hmasrafchi.leddisplay.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author michelin
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
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

	public int getR() {
		return r;
	}

	public int getG() {
		return g;
	}

	public int getB() {
		return b;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + b;
		result = prime * result + g;
		result = prime * result + r;
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
		RgbColor other = (RgbColor) obj;
		if (b != other.b)
			return false;
		if (g != other.g)
			return false;
		if (r != other.r)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("(%d, %d, %d)", r, g, b);
	}
}