/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.model.view;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * @author michelin
 *
 */
@Getter
@EqualsAndHashCode
@ToString
public final class Led {
	private static final String DEFAULT_TEXT = "█";

	private final String text;
	private final RgbColor rgbColor;

	@JsonCreator
	public Led(@JsonProperty("text") final String text, @JsonProperty("rgbColor") final RgbColor rgbColor) {
		this.text = text;
		this.rgbColor = rgbColor;
	}

	public Led(final RgbColor rgbColor) {
		this(DEFAULT_TEXT, rgbColor);
	}
}