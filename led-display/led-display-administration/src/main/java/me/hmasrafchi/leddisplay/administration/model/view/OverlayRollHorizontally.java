/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.model.view;

import java.util.List;

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
public final class OverlayRollHorizontally implements Overlay {
	private final List<List<LedState>> states;
	private final RgbColor onColor;
	private final RgbColor offColor;
	private final int beginIndexMark;
	private final int yposition;

	@JsonCreator
	public OverlayRollHorizontally(@JsonProperty("states") final List<List<LedState>> states,
			@JsonProperty("onColor") final RgbColor onColor, @JsonProperty("offColor") final RgbColor offColor,
			@JsonProperty("beginIndexMark") final int beginIndexMark, @JsonProperty("yposition") final int yposition) {
		this.states = states;
		this.onColor = onColor;
		this.offColor = offColor;
		this.beginIndexMark = beginIndexMark;
		this.yposition = yposition;
	}
}