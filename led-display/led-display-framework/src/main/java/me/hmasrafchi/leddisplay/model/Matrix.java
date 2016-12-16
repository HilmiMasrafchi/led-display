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

/**
 * @author michelin
 *
 */
public final class Matrix {
	private final List<List<Led>> leds;

	private final Collection<Scene> scenes;
	private final CyclicIterator<Scene> scenesIterator;

	private final MatrixIterator matrixIterator;

	Matrix(final List<List<Led>> leds, final Collection<? extends Scene> scenes) {
		checkLedsPreconditions(leds);
		checkScenesPreconditions(scenes);

		this.leds = leds;

		this.scenes = new CopyOnWriteArrayList<>(scenes);
		this.scenesIterator = new CyclicIterator<>(scenes);

		this.matrixIterator = new MatrixIterator();
	}

	private void checkLedsPreconditions(final List<List<Led>> leds) {
		Preconditions.checkNotNull(leds);
		Preconditions.checkArgument(!leds.isEmpty());
		checkIfLedRowsHasTheSameSize(leds);
	}

	private void checkIfLedRowsHasTheSameSize(final List<List<Led>> leds) {
		final int expectedRowSize = leds.get(0).size();
		if (leds.stream().filter(row -> row.size() != expectedRowSize).findAny().isPresent()) {
			throw new IllegalArgumentException("led rows should have equal size each");
		}
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

	private Led getLedAt(final int columnsCount, final int rowsCount) {
		return leds.get(rowsCount).get(columnsCount);
	}

	private int getColumnCount() {
		return leds.get(0).size();
	}

	private int getRowCount() {
		return leds.size();
	}

	public Stream<Led> stream() {
		return leds.stream().flatMap(row -> row.stream());
	}

	private class MatrixIterator {
		void iterate(final TriConsumer<Led, Integer, Integer> consumer) {
			for (int currentLedRowIndex = 0; currentLedRowIndex < getRowCount(); currentLedRowIndex++) {
				for (int currentLedColumnIndex = 0; currentLedColumnIndex < getColumnCount(); currentLedColumnIndex++) {
					final Led currentLed = getLedAt(currentLedColumnIndex, currentLedRowIndex);
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