/**
 * 
 */
package me.hmasrafchi.leddisplay.domain.event;

import java.io.Serializable;

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
@ToString
@EqualsAndHashCode
public final class LedView implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_TEXT = "█";

	private final String text;
	private final RgbColorView rgbColor;

	@JsonCreator
	public LedView(@JsonProperty("text") final String text, @JsonProperty("rgbColor") final RgbColorView rgbColor) {
		this.text = text;
		this.rgbColor = rgbColor;
	}

	public LedView(final RgbColorView rgbColor) {
		this(DEFAULT_TEXT, rgbColor);
	}
}