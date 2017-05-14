/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.infrastructure;

import javax.persistence.Embeddable;

import lombok.Data;

/**
 * @author michelin
 *
 */
@Data
@Embeddable
public class LedEmbeddable {
	private String text;
	private RgbColorEmbeddable rgbColor;

	LedEmbeddable() {
	}

	public LedEmbeddable(String text, RgbColorEmbeddable rgbColor) {
		this.text = text;
		this.rgbColor = rgbColor;
	}
}