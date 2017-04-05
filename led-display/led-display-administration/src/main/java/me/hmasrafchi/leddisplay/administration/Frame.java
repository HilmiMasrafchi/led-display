/**
 * 
 */
package me.hmasrafchi.leddisplay.administration;

import java.util.List;
import java.util.function.Supplier;

import me.hmasrafchi.leddisplay.util.TriFunction;
import me.hmasrafchi.leddisplay.util.TwoDimensionalListRectangular;

/**
 * @author michelin
 *
 */
public final class Frame {
	private final TwoDimensionalListRectangular<Led> frameData;

	public Frame(final Supplier<? extends Led> supplier, final int rowCount, final int columnCount) {
		this.frameData = new TwoDimensionalListRectangular<>(supplier, rowCount, columnCount);
	}

	public Frame(final TwoDimensionalListRectangular<Led> frameData) {
		this.frameData = frameData;
	}

	public Frame map(final TriFunction<Led, Integer, Integer, Led> mapperFunction) {
		final TwoDimensionalListRectangular<Led> mappedList = frameData.map(mapperFunction);
		return new Frame(mappedList);
	}

	public List<List<Led>> getFrameData() {
		return frameData.getData();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((frameData == null) ? 0 : frameData.hashCode());
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
		Frame other = (Frame) obj;
		if (frameData == null) {
			if (other.frameData != null)
				return false;
		} else if (!frameData.equals(other.frameData))
			return false;
		return true;
	}
}