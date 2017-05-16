/**
 * 
 */
package me.hmasrafchi.leddisplay.data.jpa;

import javax.persistence.Embeddable;

import lombok.Data;

/**
 * @author michelin
 *
 */
@Data
@Embeddable
public class RgbColorEmbeddable {
	public static final RgbColorEmbeddable WHITE = new RgbColorEmbeddable(255, 255, 255);
	public static final RgbColorEmbeddable BLACK = new RgbColorEmbeddable(0, 0, 0);

	public static final RgbColorEmbeddable RED = new RgbColorEmbeddable(255, 0, 0);
	public static final RgbColorEmbeddable ORANGE = new RgbColorEmbeddable(255, 127, 0);
	public static final RgbColorEmbeddable YELLOW = new RgbColorEmbeddable(255, 255, 0);
	public static final RgbColorEmbeddable GREEN = new RgbColorEmbeddable(0, 255, 0);
	public static final RgbColorEmbeddable BLUE = new RgbColorEmbeddable(0, 0, 255);
	public static final RgbColorEmbeddable INDIGO = new RgbColorEmbeddable(75, 0, 130);

	private int r;
	private int g;
	private int b;

	public RgbColorEmbeddable() {
	}

	public RgbColorEmbeddable(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
}