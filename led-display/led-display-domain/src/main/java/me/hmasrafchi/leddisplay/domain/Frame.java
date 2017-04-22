/**
 * 
 */
package me.hmasrafchi.leddisplay.domain;

import java.util.List;
import java.util.function.Supplier;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author michelin
 *
 */
@ToString
@EqualsAndHashCode
public final class Frame {
	private final TwoDimensionalListRectangular<Led> frameData;

	public Frame(final Supplier<? extends Led> supplier, final int rowCount, final int columnCount) {
		this.frameData = new TwoDimensionalListRectangular<>(supplier, rowCount, columnCount);
	}

	public Frame(final List<List<Led>> frameData) {
		this.frameData = new TwoDimensionalListRectangular<>(frameData);
	}

	public Frame map(final TriFunction<Led, Integer, Integer, Led> mapperFunction) {
		final TwoDimensionalListRectangular<Led> mappedList = frameData.map(mapperFunction);
		return new Frame(mappedList.getData());
	}

	public List<List<Led>> getFrameData() {
		return frameData.getData();
	}
}