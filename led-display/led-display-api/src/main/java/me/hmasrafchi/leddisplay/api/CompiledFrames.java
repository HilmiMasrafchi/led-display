/**
 * 
 */
package me.hmasrafchi.leddisplay.api;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author michelin
 *
 */
public final class CompiledFrames {
	private final List<Frame> compiledFramesData;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((compiledFramesData == null) ? 0 : compiledFramesData.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompiledFrames other = (CompiledFrames) obj;
		if (compiledFramesData == null) {
			if (other.compiledFramesData != null)
				return false;
		} else if (!compiledFramesData.equals(other.compiledFramesData))
			return false;
		return true;
	}
}