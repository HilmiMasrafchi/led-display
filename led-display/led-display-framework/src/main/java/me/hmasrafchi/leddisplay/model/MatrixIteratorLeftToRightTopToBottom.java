/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import lombok.RequiredArgsConstructor;
import me.hmasrafchi.leddisplay.model.api.Led;

/**
 * @author michelin
 *
 */
@RequiredArgsConstructor
public final class MatrixIteratorLeftToRightTopToBottom extends MatrixIterator {
	private final Matrix matrix;

	@Override
	void iterate(final MatrixIteratorCallback callback) {
		for (int currentLedRowIndex = 0; currentLedRowIndex < matrix.getRowCount(); currentLedRowIndex++) {
			for (int currentLedColumnIndex = 0; currentLedColumnIndex < matrix
					.getColumnCount(); currentLedColumnIndex++) {
				final Led led = matrix.getLedAt(currentLedColumnIndex, currentLedRowIndex);
				callback.ledVisited(led, currentLedColumnIndex, currentLedRowIndex);
			}
		}
	}
}