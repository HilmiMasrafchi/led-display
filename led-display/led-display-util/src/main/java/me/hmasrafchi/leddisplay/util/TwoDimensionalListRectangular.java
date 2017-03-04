/**
 * 
 */
package me.hmasrafchi.leddisplay.util;

import static java.util.Collections.unmodifiableList;
import static me.hmasrafchi.leddisplay.util.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * @author michelin
 *
 */
@ToString
@EqualsAndHashCode
public final class TwoDimensionalListRectangular<T> {
	@Getter
	private final List<List<T>> data;

	public TwoDimensionalListRectangular(final List<? extends List<? extends T>> data) {
		checkNotNull(data);
		checkIfDataHasAtLeastOneElement(data);
		checkIfDataRowsHasTheSameSize(data);

		this.data = mapToUnmodifiableList(data);
	}

	private <R> List<List<R>> mapToUnmodifiableList(final List<? extends List<? extends R>> data) {
		final List<List<R>> mappedList = new ArrayList<>();
		data.forEach(row -> mappedList.add(unmodifiableList(row)));
		return unmodifiableList(mappedList);
	}

	private void checkIfDataHasAtLeastOneElement(final List<? extends List<? extends T>> data) {
		Preconditions.checkArgument(!data.isEmpty());
		Preconditions.checkArgument(!data.get(0).isEmpty());
	}

	private void checkIfDataRowsHasTheSameSize(final List<? extends List<? extends T>> data) {
		Preconditions.checkArgument(!data.isEmpty());
		final int expectedRowSize = data.get(0).size();
		if (data.stream().filter(row -> row.size() != expectedRowSize).findAny().isPresent()) {
			throw new IllegalArgumentException("rows should have equal size each");
		}
	}

	public TwoDimensionalListRectangular(final Supplier<? extends T> supplier, final int rowCount,
			final int columnCount) {
		this.data = mapToList((t, r, c) -> supplier.get(), rowCount, columnCount);
	}

	private <R> List<List<R>> mapToList(final TriFunction<T, Integer, Integer, R> mapperFunction, final int rowCount,
			final int columnCount) {
		final List<List<R>> mappedData = new ArrayList<>();
		IntStream.range(0, rowCount).forEach(rowIndex -> {
			final List<R> currentLedRow = new ArrayList<>();
			IntStream.range(0, columnCount).forEach(columnIndex -> {
				final T currentElement = data == null ? null : getValueAt(rowIndex, columnIndex);
				final R currentMappedElement = mapperFunction.accept(currentElement, rowIndex, columnIndex);
				currentLedRow.add(currentMappedElement);
			});
			mappedData.add(currentLedRow);
		});

		return mapToUnmodifiableList(mappedData);
	}

	public <R> TwoDimensionalListRectangular<R> map(final TriFunction<T, Integer, Integer, R> mapperFunction,
			final int rowCount, final int columnCount) {
		final List<List<R>> mappedList = mapToList(mapperFunction, rowCount, columnCount);
		return new TwoDimensionalListRectangular<>(mappedList);
	}

	public List<T> getRowAt(final int rowIndex) {
		return data.get(rowIndex);
	}

	public T getValueAt(final int rowIndex, final int columnIndex) {
		return this.data.get(rowIndex).get(columnIndex);
	}

	public int getRowCount() {
		return data.size();
	}

	public int getColumnCount() {
		return data.get(0).size();
	}

	public Stream<T> stream() {
		return data.stream().flatMap(row -> row.stream());
	}
}