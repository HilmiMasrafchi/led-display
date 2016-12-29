/**
 * 
 */
package me.hmasrafchi.leddisplay.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.google.common.base.Preconditions;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author michelin
 *
 */
@ToString
@EqualsAndHashCode
public final class TwoDimensionalListRectangular<T> {
	private final List<List<? extends T>> data;

	public TwoDimensionalListRectangular(List<? extends List<? extends T>> data) {
		checkIfDataRowsHasTheSameSize(Preconditions.checkNotNull(data));
		this.data = new ArrayList<>(data);
	}

	private void checkIfDataRowsHasTheSameSize(final List<? extends List<? extends T>> data) {
		Preconditions.checkArgument(!data.isEmpty());
		final int expectedRowSize = data.get(0).size();
		if (data.stream().filter(row -> row.size() != expectedRowSize).findAny().isPresent()) {
			throw new IllegalArgumentException("rows should have equal size each");
		}
	}

	public T getValueAt(final int columnIndex, final int rowIndex) {
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