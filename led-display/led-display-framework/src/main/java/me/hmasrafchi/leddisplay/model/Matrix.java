/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

import com.google.common.base.Preconditions;

import me.hmasrafchi.leddisplay.model.api.Led;
import me.hmasrafchi.leddisplay.util.CyclicIterator;
import me.hmasrafchi.leddisplay.util.TriConsumer;

/**
 * @author michelin
 *
 */
public final class Matrix {
	private final List<List<Led>> leds;

	private final Collection<MatrixEventListener> matrixEventListeners;
	private final CyclicIterator<MatrixEventListener> matrixEventListenersIterator;

	private final MatrixIterator matrixIterator;

	Matrix(final List<List<Led>> leds, final Collection<? extends MatrixEventListener> matrixEventListeners) {
		checkLedsPreconditions(leds);
		checkMatrixEventListenerPreconditions(matrixEventListeners);

		this.leds = leds;

		this.matrixEventListeners = new CopyOnWriteArrayList<>(matrixEventListeners);
		this.matrixEventListenersIterator = new CyclicIterator<>(matrixEventListeners);

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

	private void checkMatrixEventListenerPreconditions(
			final Collection<? extends MatrixEventListener> matrixEventListeners) {
		Preconditions.checkNotNull(matrixEventListeners);
		Preconditions.checkArgument(!matrixEventListeners.isEmpty());
	}

	public void nextFrame() {
		final MatrixEventListener currentListener = matrixEventListenersIterator.getCurrentElement();
		if (currentListener.isExhausted()) {
			currentListener.onMatrixReset();
			matrixEventListenersIterator.next();
		} else {
			currentListener.onMatrixIterationEnded();
		}
		matrixIterator.iterate(currentListener::onLedVisited);
	}

	public void addMatrixEventListener(final MatrixEventListener listener) {
		matrixEventListeners.add(listener);
	}

	public void removeMatrixEventListener(final MatrixEventListener listener) {
		matrixEventListeners.remove(listener);
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