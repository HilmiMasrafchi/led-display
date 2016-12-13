/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import me.hmasrafchi.leddisplay.model.api.Led;

/**
 * @author michelin
 *
 */
public abstract class MatrixIterator {
	abstract void iterate(MatrixIteratorCallback callback);

	@FunctionalInterface
	interface MatrixIteratorCallback {
		void ledVisited(Led led, int currentColumnIndex, int currentRowIndex);
	}
}