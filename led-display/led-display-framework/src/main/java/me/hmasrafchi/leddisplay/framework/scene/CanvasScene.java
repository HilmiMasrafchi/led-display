/**
 * 
 */
package me.hmasrafchi.leddisplay.framework.scene;

import java.util.ArrayList;
import java.util.List;

import me.hmasrafchi.leddisplay.api.Led;

/**
 * @author michelin
 *
 */
public final class CanvasScene implements Scene {
	private final int columnCount;
	private final int rowCount;

	private final List<List<Boolean>> states;

	public CanvasScene(final int columnCount, final int rowCount) {
		this.columnCount = columnCount;
		this.rowCount = rowCount;

		this.states = new ArrayList<>(11);
		for (int i = 0; i < 11; i++) {
			final List<Boolean> currentRow = new ArrayList<>();
			for (int j = 0; j < 11; j++) {
				currentRow.add(Boolean.TRUE);
			}
			this.states.add(currentRow);
		}

		this.states.get(5).set(5, Boolean.FALSE);
	}

	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public void nextFrame(final Led led, int x, int y) {

	}
}