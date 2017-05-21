/**
 * 
 */
package me.hmasrafchi.leddisplay.consumer.data.jpa;

import javax.persistence.Embeddable;

import lombok.Data;

/**
 * @author michelin
 *
 */
@Data
@Embeddable
public class RgbColorJpa {
	private int r;
	private int g;
	private int b;

	public RgbColorJpa(final int r, final int g, final int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	RgbColorJpa() {
	}
}