/**
 * 
 */
package me.hmasrafchi.leddisplay.framework;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import lombok.Getter;
import me.hmasrafchi.leddisplay.api.Led;

/**
 * @author michelin
 *
 */
public final class Board {
	private final Configuration configuration;
	private final Provider<Led> ledProvider;

	@Getter
	private final List<List<Led>> leds;

	@Inject
	public Board(final Configuration configuration, final Provider<Led> ledProvider) {
		this.configuration = configuration;
		this.ledProvider = ledProvider;
		this.leds = initializeLeds();
	}

	private List<List<Led>> initializeLeds() {
		final int rowsCount = configuration.getBoardRowsCount();
		final int columnsCount = configuration.getBoardColumnsCount();
		final List<List<Led>> leds = new ArrayList<>(rowsCount);

		double currentX = 0;
		double currentY = 0;
		Led currentLed = null;
		for (int i = 0; i < rowsCount; i++) {
			final List<Led> currentRow = new ArrayList<>(columnsCount);
			leds.add(currentRow);
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

		return leds;
	}

	public void nextFrame() {
	}
}