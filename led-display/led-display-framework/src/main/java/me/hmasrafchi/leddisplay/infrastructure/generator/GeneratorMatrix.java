/**
 * 
 */
package me.hmasrafchi.leddisplay.infrastructure.generator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.google.common.base.Preconditions;

import me.hmasrafchi.leddisplay.model.Led;
import me.hmasrafchi.leddisplay.model.Matrix;

/**
 * @author michelin
 *
 */
public final class GeneratorMatrix {
	private final GeneratorLed ledGenerator;
	private final int horizontalGap;
	private final int verticalGap;

	@Inject
	public GeneratorMatrix(final GeneratorLed ledGenerator, final int horizontalGap, final int verticalGap) {
		Preconditions.checkNotNull(ledGenerator);
		this.ledGenerator = ledGenerator;
		this.horizontalGap = horizontalGap;
		this.verticalGap = verticalGap;
	}

	public Matrix next(final int columnsCount, final int rowsCount) {
		Preconditions.checkArgument(columnsCount > 0);
		Preconditions.checkArgument(rowsCount > 0);

		final List<List<Led>> leds = new ArrayList<>();

		double currentCoordinateX = 0;
		double currentCoordinateY = 0;
		for (int i = 0; i < rowsCount; i++) {
			final List<Led> currentLedRow = new ArrayList<>(columnsCount);
			for (int j = 0; j < columnsCount; j++) {
				final Led currentLed = ledGenerator.next();
				currentLed.setCoordinateX(currentCoordinateX);
				currentLed.setCoordinateY(currentCoordinateY);
				currentLedRow.add(currentLed);

				currentCoordinateX += ledGenerator.getLedMaximumWidth() + horizontalGap;
			}
			leds.add(currentLedRow);

			currentCoordinateY += ledGenerator.getLedMaximumHeight() + verticalGap;
			currentCoordinateX = 0;
		}

		return new Matrix(leds);
	}
}