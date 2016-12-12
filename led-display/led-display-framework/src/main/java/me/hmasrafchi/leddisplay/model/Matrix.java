/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import java.util.List;
import java.util.stream.Stream;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * @author michelin
 *
 */
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public final class Matrix {
	private final List<List<Led>> leds;

	public Led getLedAt(final int columnsCount, final int rowsCount) {
		return leds.get(rowsCount).get(columnsCount);
	}

	public int getColumnCount() {
		return leds.get(0).size();
	}

	public int getRowCount() {
		return leds.size();
	}

	public Stream<Led> stream() {
		return leds.stream().flatMap(row -> row.stream());
	}
}