/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.hmasrafchi.leddisplay.util.TwoDimensionalListRectangular;

/**
 * @author michelin
 *
 */
@ToString
@RequiredArgsConstructor
public final class MatrixDefault implements Matrix {
	@Override
	public CompiledFrames compile(final Scene scene, final int rowCount, final int columnCount) {
		final List<TwoDimensionalListRectangular<Led>> frames = new ArrayList<>();

		final TwoDimensionalListRectangular<Led> currentFrame = TwoDimensionalListRectangular.create(Led::new, rowCount,
				columnCount);
		while (!scene.isExhausted()) {
			final TwoDimensionalListRectangular<Led> nextFrame = iterate(currentFrame, scene::onLedVisited);
			scene.onMatrixIterationEnded();
			frames.add(nextFrame);
		}

		return new CompiledFrames(frames);
	}

	private TwoDimensionalListRectangular<Led> iterate(final TwoDimensionalListRectangular<Led> frame,
			final BiFunction<Integer, Integer, Led> biFunction) {
		final List<List<Led>> result = new ArrayList<>();
		for (int currentLedRowIndex = 0; currentLedRowIndex < frame.getRowCount(); currentLedRowIndex++) {
			final List<Led> row = new ArrayList<>();
			for (int currentLedColumnIndex = 0; currentLedColumnIndex < frame
					.getColumnCount(); currentLedColumnIndex++) {
				final Led accept = biFunction.apply(currentLedRowIndex, currentLedColumnIndex);
				row.add(accept);
			}
			result.add(row);
		}

		return new TwoDimensionalListRectangular<>(result);
	}
}