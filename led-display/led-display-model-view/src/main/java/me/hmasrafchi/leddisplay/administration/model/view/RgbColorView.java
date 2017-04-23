/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.model.view;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

/**
 * @author michelin
 *
 */
@Value
public class RgbColorView implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final RgbColorView WHITE = new RgbColorView(255, 255, 255);
	public static final RgbColorView BLACK = new RgbColorView(0, 0, 0);

	public static final RgbColorView RED = new RgbColorView(255, 0, 0);
	public static final RgbColorView ORANGE = new RgbColorView(255, 127, 0);
	public static final RgbColorView YELLOW = new RgbColorView(255, 255, 0);
	public static final RgbColorView GREEN = new RgbColorView(0, 255, 0);
	public static final RgbColorView BLUE = new RgbColorView(0, 0, 255);
	public static final RgbColorView INDIGO = new RgbColorView(75, 0, 130);

	private final int r;
	private final int g;
	private final int b;

	@JsonCreator
	public RgbColorView(@JsonProperty("r") final int r, @JsonProperty("g") final int g,
			@JsonProperty("b") final int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
}