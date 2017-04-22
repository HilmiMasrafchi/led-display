/**
 * 
 */
package me.hmasrafchi.leddisplay.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author michelin
 *
 */
@ToString
@EqualsAndHashCode
public final class CompiledFrames {
	private final List<Frame> compiledFramesData;

	public CompiledFrames(final List<Frame> compiledFramesData) {
		this.compiledFramesData = new ArrayList<>(compiledFramesData);
	}

	public Stream<Frame> stream() {
		return compiledFramesData.stream();
	}
}