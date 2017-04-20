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
@EqualsAndHashCode
@ToString
public final class CompiledFrames {
	private final List<List<List<LedView>>> leds;

	@JsonCreator
	public CompiledFrames(@JsonProperty("leds") final List<List<List<LedView>>> leds) {
		this.leds = leds;
	}
}