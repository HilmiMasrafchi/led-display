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
import me.hmasrafchi.leddisplay.domain.event.RgbColorView;

/**
 * @author michelin
 *
 */
@Getter
@ToString
@EqualsAndHashCode
public final class OverlayStationaryView implements OverlayView {
	private final List<List<LedStateView>> states;
	private final RgbColorView onColor;
	private final RgbColorView offColor;
	private final int duration;

	@JsonCreator
	public OverlayStationaryView(@JsonProperty("states") final List<List<LedStateView>> states,
			@JsonProperty("onColor") final RgbColorView onColor, @JsonProperty("offColor") final RgbColorView offColor,
			@JsonProperty("duration") final int duration) {
		this.states = states;
		this.onColor = onColor;
		this.offColor = offColor;
		this.duration = duration;
	}
}