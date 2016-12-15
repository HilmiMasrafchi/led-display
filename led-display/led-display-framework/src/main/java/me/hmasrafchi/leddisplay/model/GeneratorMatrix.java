/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Preconditions;

import me.hmasrafchi.leddisplay.model.api.Led;

/**
 * @author michelin
 *
 */
public final class GeneratorMatrix {
	private final GeneratorLed ledGenerator;
	private final int horizontalGap;
	private final int verticalGap;

	public GeneratorMatrix(final GeneratorLed ledGenerator, final int horizontalGap, final int verticalGap) {
		this.ledGenerator = Preconditions.checkNotNull(ledGenerator);

		Preconditions.checkArgument(horizontalGap >= 0);
		this.horizontalGap = horizontalGap;

		Preconditions.checkArgument(verticalGap >= 0);
		this.verticalGap = verticalGap;
	}

	public Matrix next(final Collection<? extends Scene> scenes, final int columnsCount, final int rowsCount) {
		Preconditions.checkArgument(columnsCount > 0);
		Preconditions.checkArgument(rowsCount > 0);

		final List<List<Led>> leds = new ArrayList<>();

		double currentCoordinateY = 0;
		for (int i = 0; i < rowsCount; i++) {
			final List<Led> currentLedRow = nextLedRow(columnsCount, currentCoordinateY);
			leds.add(currentLedRow);

			currentCoordinateY += (ledGenerator.getLedMaximumHeight() + verticalGap);
		}

		return new Matrix(leds, scenes);
	}

	private List<Led> nextLedRow(final int columnsCount, final double currentCoordinateY) {
		final List<Led> ledRow = new ArrayList<>(columnsCount);
		double currentCoordinateX = 0;
		for (int j = 0; j < columnsCount; j++) {
			final Led currentLed = ledGenerator.next();
			currentLed.setCoordinateX(currentCoordinateX);
			currentLed.setCoordinateY(currentCoordinateY);
			ledRow.add(currentLed);

			currentCoordinateX += (ledGenerator.getLedMaximumWidth() + horizontalGap);
		}

		return ledRow;
	}
}