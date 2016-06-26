/**
 * 
 */
package me.hmasrafchi.leddisplay.framework;

import javax.inject.Inject;
import javax.inject.Provider;

import me.hmasrafchi.leddisplay.api.LED;

/**
 * @author michelin
 *
 */
public final class BoardGenerator {
	private final Provider<LED> ledProvider;

	@Inject
	public BoardGenerator(Provider<LED> ledProvider) {
		this.ledProvider = ledProvider;
	}

	public Board getByRowAndColumnCount(int rowsCount, int columnsCount) {
		final LED[][] leds = new LED[rowsCount][columnsCount];

		double currentX = 0;
		double currentY = 0;
		LED currentLED = null;
		for (int i = 0; i < rowsCount; i++) {
			for (int j = 0; j < columnsCount; j++) {
				leds[i][j] = ledProvider.get();
				currentLED = leds[i][j];
				currentLED.setCoordinateX(currentX);
				currentLED.setCoordinateY(currentY);
				currentX += currentLED.getWidth();
			}

			currentY += currentLED.getHeight();
			currentX = 0;
		}

		return new Board(leds);
	}
}