/**
 * 
 */
package me.hmasrafchi.leddisplay.framework.generator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.google.common.base.Preconditions;

import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.framework.Matrix;

/**
 * @author michelin
 *
 */
public final class GeneratorMatrix {
	private final GeneratorLed ledGenerator;

	@Inject
	public GeneratorMatrix(final GeneratorLed ledGenerator) {
		Preconditions.checkNotNull(ledGenerator);
		this.ledGenerator = ledGenerator;
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

				currentCoordinateX += ledGenerator.getLedMaximumWidth() + 1;
			}
			leds.add(currentLedRow);

			currentCoordinateY += ledGenerator.getLedMaximumHeight();
			currentCoordinateX = 0;
		}

		return new Matrix(leds);
	}
}