/**
 * 
 */
package me.hmasrafchi.leddisplay.model.scene;

import lombok.RequiredArgsConstructor;
import me.hmasrafchi.leddisplay.model.Led;
import me.hmasrafchi.leddisplay.model.Matrix;

/**
 * @author michelin
 *
 */
@RequiredArgsConstructor
public class MatrixIterator {
	private final Matrix matrix;

	public void iterate(final MatrixIteratorCallback callback) {
		for (int currentLedRowIndex = 0; currentLedRowIndex < matrix.getRowCount(); currentLedRowIndex++) {
			for (int currentLedColumnIndex = 0; currentLedColumnIndex < matrix
					.getColumnCount(); currentLedColumnIndex++) {
				Led led = matrix.getLedAt(currentLedColumnIndex, currentLedRowIndex);
				callback.apply(led, currentLedColumnIndex, currentLedRowIndex);
			}
		}
	}

}

@FunctionalInterface
interface MatrixIteratorCallback {
	void apply(Led led, int currentColumnIndex, int currentRowIndex);
}