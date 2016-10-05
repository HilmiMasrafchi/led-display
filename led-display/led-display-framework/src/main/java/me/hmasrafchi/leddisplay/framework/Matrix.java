/**
 * 
 */
package me.hmasrafchi.leddisplay.framework;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import lombok.Getter;
import me.hmasrafchi.leddisplay.api.Led;

/**
 * @author michelin
 *
 */
public final class Matrix {
	@Getter
	private final Integer columnsCount;
	@Getter
	private final Integer rowsCount;

	private final List<List<Led>> leds;

	@Inject
	public Matrix(@Named("columnsCount") final Integer columnsCount, @Named("rowsCount") final Integer rowsCount,
			final Provider<Led> ledProvider) {
		this.columnsCount = columnsCount;
		this.rowsCount = rowsCount;
		this.leds = new ArrayList<>(rowsCount);

		double currentX = 0;
		double currentY = 0;
		Led currentLed = null;
		for (int i = 0; i < rowsCount; i++) {
			final List<Led> currentRow = new ArrayList<>(columnsCount);
			this.leds.add(currentRow);
			for (int j = 0; j < columnsCount; j++) {
				currentLed = ledProvider.get();
				currentLed.setCoordinateX(currentX);
				currentLed.setCoordinateY(currentY);
				currentX += currentLed.getWidth();
				currentRow.add(currentLed);
			}

			currentY += currentLed.getHeight();
			currentX = 0;
		}
	}

	public Led getLedAt(final int columnIndex, final int rowIndex) {
		return leds.get(rowIndex).get(columnIndex);
	}

	public Collection<Led> getAllLeds() {
		final Collection<Led> flattenedList = new ArrayList<>();
		for (final List<Led> currentRow : leds) {
			for (final Led currentLed : currentRow) {
				flattenedList.add(currentLed);
			}
		}

		return flattenedList;
	}
}