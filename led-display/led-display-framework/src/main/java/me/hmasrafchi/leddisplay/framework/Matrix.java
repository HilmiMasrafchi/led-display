/**
 * 
 */
package me.hmasrafchi.leddisplay.framework;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.google.common.base.Preconditions;

import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.framework.generator.GeneratorLed;

/**
 * @author michelin
 *
 */
public final class Matrix {
	private final int columnsCount;
	private final int rowsCount;

	private final List<List<Led>> leds;

	@Inject
	public Matrix(final GeneratorLed ledGenerator, @Named("matrixColumnsCount") final int columnsCount,
			@Named("matrixRowsCount") final int rowsCount) {
		Preconditions.checkArgument(columnsCount > 0);
		this.columnsCount = columnsCount;

		Preconditions.checkArgument(rowsCount > 0);
		this.rowsCount = rowsCount;

		this.leds = generateLedCollections(Preconditions.checkNotNull(ledGenerator), columnsCount, rowsCount);
	}

	private List<List<Led>> generateLedCollections(final GeneratorLed ledGenerator, final int columnsCount,
			final int rowsCount) {
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

		return leds;
	}

	public Led getLedAt(final int columnIndex, final int rowIndex) {
		return leds.get(rowIndex).get(columnIndex);
	}

	public Collection<Led> getAllLeds() {
		final Collection<Led> flattenedLedsList = new ArrayList<>();
		for (final List<Led> currentRow : leds) {
			for (final Led currentLed : currentRow) {
				flattenedLedsList.add(currentLed);
			}
		}

		return flattenedLedsList;
	}

	public int getColumnsCount() {
		return columnsCount;
	}

	public int getRowsCount() {
		return rowsCount;
	}
}