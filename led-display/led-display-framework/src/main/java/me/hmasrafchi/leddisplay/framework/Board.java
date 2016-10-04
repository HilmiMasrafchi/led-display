/**
 * 
 */
package me.hmasrafchi.leddisplay.framework;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import lombok.Getter;

/**
 * @author michelin
 *
 */
public final class Board {
	private final Configuration configuration;
	private final Provider<Led> ledProvider;
	private final Collection<? extends Scene> scenes;

	private Iterator<? extends Scene> sceneIterator;
	private Scene currentScene;

	@Getter
	private final List<List<Led>> leds;

	@Inject
	public Board(final Configuration configuration, final Provider<Led> ledProvider,
			final Collection<? extends Scene> scenes) {
		this.configuration = configuration;
		this.ledProvider = ledProvider;
		this.scenes = scenes;

		this.sceneIterator = this.scenes.iterator();
		this.currentScene = this.sceneIterator.next();

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
		if (currentScene.hasNext()) {
			currentScene.iterateLeds(leds);
			return;
		}

		if (sceneIterator.hasNext()) {
			currentScene = sceneIterator.next();
		} else {
			sceneIterator = scenes.iterator();
			currentScene = sceneIterator.next();
		}
	}
}