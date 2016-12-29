/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

import com.google.common.base.Preconditions;

import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.util.CyclicIterator;
import me.hmasrafchi.leddisplay.util.TriConsumer;
import me.hmasrafchi.leddisplay.util.TwoDimensionalListRectangular;

/**
 * @author michelin
 *
 */
public final class Matrix {
	private final TwoDimensionalListRectangular<Led> leds;

	private final Collection<Scene> scenes;
	private final CyclicIterator<Scene> scenesIterator;

	private final MatrixIterator matrixIterator;

	Matrix(final List<? extends List<? extends Led>> leds, final Collection<? extends Scene> scenes) {
		checkScenesPreconditions(scenes);

		this.leds = new TwoDimensionalListRectangular(leds);

		this.scenes = new CopyOnWriteArrayList<>(scenes);
		this.scenesIterator = new CyclicIterator<>(scenes);

		this.matrixIterator = new MatrixIterator();
	}

	private void checkScenesPreconditions(final Collection<? extends Scene> scenes) {
		Preconditions.checkNotNull(scenes);
		Preconditions.checkArgument(!scenes.isEmpty());
	}

	public void nextFrame() {
		final Scene currentScene = scenesIterator.getCurrentElement();
		if (currentScene.isExhausted()) {
			currentScene.onMatrixReset();
			scenesIterator.next();
			matrixIterator.iterate((currentLed, i, j) -> currentLed.reset());
		} else {
			currentScene.onMatrixIterationEnded();
		}
		matrixIterator.iterate(scenesIterator.getCurrentElement()::onLedVisited);
	}

	public void addScene(final Scene scene) {
		scenes.add(scene);
	}

	public void removeScene(final Scene scene) {
		scenes.remove(scene);
	}

	public Stream<Led> stream() {
		return leds.stream();
	}

	private class MatrixIterator {
		void iterate(final TriConsumer<Led, Integer, Integer> consumer) {
			for (int currentLedRowIndex = 0; currentLedRowIndex < leds.getRowCount(); currentLedRowIndex++) {
				for (int currentLedColumnIndex = 0; currentLedColumnIndex < leds
						.getColumnCount(); currentLedColumnIndex++) {
					final Led currentLed = leds.getValueAt(currentLedColumnIndex, currentLedRowIndex);
					consumer.accept(currentLed, currentLedColumnIndex, currentLedRowIndex);
				}
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((leds == null) ? 0 : leds.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Matrix other = (Matrix) obj;
		if (leds == null) {
			if (other.leds != null)
				return false;
		} else if (!leds.equals(other.leds))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Matrix [leds=" + leds + "]";
	}
}

interface MatrixEventListener {
	void onLedVisited(Led led, int currentLedColumnIndex, int currentLedRowIndex);

	void onMatrixReset();

	void onMatrixIterationEnded();
}