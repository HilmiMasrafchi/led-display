/**
 * 
 */
package me.hmasrafchi.leddisplay.api;

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

	public Frame map(final TriFunction<Led, Integer, Integer, Led> mapperFunction, final int rowCount,
			final int columnCount) {
		final TwoDimensionalListRectangular<Led> mappedList = frameData.map(mapperFunction, rowCount, columnCount);
		return new Frame(mappedList);
	}

	private Frame(final TwoDimensionalListRectangular<Led> frameData) {
		this.frameData = frameData;
	}

	public List<List<Led>> getFrameData() {
		return frameData.getData();
	}
}