/**
 * 
 */
package me.hmasrafchi.leddisplay.util;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.google.common.base.Preconditions;

import lombok.EqualsAndHashCode;

/**
 * @author michelin
 *
 */
@EqualsAndHashCode
public final class TwoDimensionalListRectangular<T> {
	private final List<List<T>> data;

	public static <T> TwoDimensionalListRectangular<T> create(final Supplier<? extends T> supplier, final int rowCount,
			final int columnCount) {
		final List<List<T>> data = new ArrayList<>();
		for (int i = 0; i < rowCount; i++) {
			final List<T> dataRow = new ArrayList<>();
			for (int j = 0; j < columnCount; j++) {
				final T t = supplier.get();
				dataRow.add(t);
			}

			data.add(dataRow);
		}

		return new TwoDimensionalListRectangular<>(data);
	}

	public TwoDimensionalListRectangular(final List<? extends List<? extends T>> data) {
		Preconditions.checkNotNull(data);
		checkIfDataHasAtLeastOneElement(data);
		checkIfDataRowsHasTheSameSize(Preconditions.checkNotNull(data));

		final List<List<T>> temp = new ArrayList<>();
		for (final List<? extends T> row : data) {
			temp.add(unmodifiableList(row));
		}
		this.data = unmodifiableList(temp);
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

	public <R> TwoDimensionalListRectangular<R> map(final Function<? super T, ? extends R> mapper) {
		final List<List<R>> result = new ArrayList<>();
		for (final List<? extends T> row : data) {
			final List<R> newRow = new ArrayList<>();
			for (final T element : row) {
				final R newElement = mapper.apply(element);
				newRow.add(newElement);
			}
			result.add(newRow);
		}

		return new TwoDimensionalListRectangular<>(result);
	}

	@Override
	public String toString() {
		final StringBuilder strBuilder = new StringBuilder();
		for (int i = 0; i < data.size(); i++) {
			strBuilder.append("\n[");
			for (int j = 0; j < data.get(0).size(); j++) {
				strBuilder.append(data.get(i).get(j) + ", ");
			}
			strBuilder.append("]");
		}

		return strBuilder.toString();
	}
}