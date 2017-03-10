/**
 * 
 */
package me.hmasrafchi.leddisplay.api;

import java.util.List;
import java.util.function.Supplier;

import lombok.EqualsAndHashCode;
import me.hmasrafchi.leddisplay.util.TriFunction;
import me.hmasrafchi.leddisplay.util.TwoDimensionalListRectangular;

/**
 * @author michelin
 *
 */
@EqualsAndHashCode
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
}