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
import me.hmasrafchi.leddisplay.domain.event.LedView;

/**
 * @author michelin
 *
 */
@Getter
@EqualsAndHashCode
@ToString
// TODO: to be removed
public final class CompiledFrames {
	private final List<List<List<LedView>>> leds;

	@JsonCreator
	public CompiledFrames(@JsonProperty("leds") final List<List<List<LedView>>> leds) {
		this.leds = leds;
	}
}