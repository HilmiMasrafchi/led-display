/**
 * 
 */
package me.hmasrafchi.leddisplay.consumer.model.jpa;

import javax.persistence.Embeddable;

import lombok.Data;

/**
 * @author michelin
 *
 */
@Data
@Embeddable
public class LedJpa {
	private String text;
	private RgbColorJpa rgbColor;

	public LedJpa(final String text, final RgbColorJpa rgbColor) {
		this.text = text;
		this.rgbColor = rgbColor;
	}

	public LedJpa() {
	}
}