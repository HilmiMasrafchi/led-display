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
public final class OverlayRollHorizontallyView implements OverlayView {
	private final List<List<LedStateView>> states;
	private final RgbColorView onColor;
	private final RgbColorView offColor;
	private final int beginIndexMark;
	private final int yposition;

	@JsonCreator
	public OverlayRollHorizontallyView(@JsonProperty("states") final List<List<LedStateView>> states,
			@JsonProperty("onColor") final RgbColorView onColor, @JsonProperty("offColor") final RgbColorView offColor,
			@JsonProperty("beginIndexMark") final int beginIndexMark, @JsonProperty("yposition") final int yposition) {
		this.states = states;
		this.onColor = onColor;
		this.offColor = offColor;
		this.beginIndexMark = beginIndexMark;
		this.yposition = yposition;
	}
}