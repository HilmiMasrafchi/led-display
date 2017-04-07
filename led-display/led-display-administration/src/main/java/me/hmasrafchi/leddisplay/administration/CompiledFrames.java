/**
 * 
 */
package me.hmasrafchi.leddisplay.administration;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

/**
 * @author michelin
 *
 */
@ToString
@EqualsAndHashCode
public final class CompiledFrames {
	@Setter
	private List<Frame> compiledFramesData;

	public CompiledFrames() {
		this(Collections.emptyList());
	}

	public CompiledFrames(final List<Frame> compiledFramesData) {
		this.compiledFramesData = new ArrayList<>(compiledFramesData);
	}

	public List<Frame> getCompiledFramesData() {
		return unmodifiableList(compiledFramesData);
	}

	public void addFrame(final Frame frame) {
		compiledFramesData.add(frame);
	}

	public Stream<Frame> stream() {
		return compiledFramesData.stream();
	}
}