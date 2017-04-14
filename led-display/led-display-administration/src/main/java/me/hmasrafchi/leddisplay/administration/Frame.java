/**
 * 
 */
package me.hmasrafchi.leddisplay.administration;

import java.util.List;
import java.util.function.Supplier;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import me.hmasrafchi.leddisplay.administration.infrastructure.TriFunction;
import me.hmasrafchi.leddisplay.administration.infrastructure.TwoDimensionalListRectangular;
import me.hmasrafchi.leddisplay.administration.model.Led;

/**
 * @author michelin
 *
 */
@Data
@ToString
@EqualsAndHashCode
public final class Frame {
	private TwoDimensionalListRectangular<Led> frameData;

	public Frame() {
	}

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