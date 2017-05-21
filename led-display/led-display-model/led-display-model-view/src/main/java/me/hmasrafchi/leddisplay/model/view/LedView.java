/**
 * 
 */
package me.hmasrafchi.leddisplay.model.view;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author michelin
 *
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public final class LedView implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_TEXT = "█";

	private String text;
	private RgbColorView rgbColor;

	@JsonCreator
	public LedView(@JsonProperty("text") final String text, @JsonProperty("rgbColor") final RgbColorView rgbColor) {
		this.text = text;
		this.rgbColor = rgbColor;
	}

	public LedView() {
	}

	public LedView(final RgbColorView rgbColor) {
		this(DEFAULT_TEXT, rgbColor);
	}
}