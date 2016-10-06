/**
 * 
 */
package me.hmasrafchi.leddisplay.framework.scene;

import java.util.ArrayList;
import java.util.List;

import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.api.Led.RgbColor;

/**
 * @author michelin
 *
 */
public final class RightToLeftCanvasScene extends AbstractScene {
	private final int yPosition;
	private final int columnCount;

	private int currentMarker;

	private final List<List<Boolean>> states;

	public RightToLeftCanvasScene(final int yPosition, final int columnCount) {
		this.yPosition = yPosition;
		this.columnCount = columnCount;

		this.currentMarker = columnCount - 1;

		this.states = new ArrayList<>(11);
		for (int i = 0; i < 8; i++) {
			final List<Boolean> currentRow = new ArrayList<>();
			for (int j = 0; j < 11; j++) {
				currentRow.add(Boolean.TRUE);
			}
			this.states.add(currentRow);
		}

		this.states.get(5).set(5, Boolean.FALSE);
		this.states.get(5).set(4, Boolean.FALSE);
		this.states.get(5).set(6, Boolean.FALSE);
		this.states.get(4).set(5, Boolean.FALSE);
		this.states.get(6).set(5, Boolean.FALSE);
	}

	@Override
	public boolean hasNext() {
		return currentMarker >= -states.get(0).size() + 1;
	}

	@Override
	void determineLedState(Led currentLed, int currentColumnIndex, int currentRowIndex) {
		boolean isInX = currentColumnIndex >= currentMarker
				&& currentMarker + states.get(0).size() > currentColumnIndex;
		boolean isInY = currentRowIndex >= yPosition && currentRowIndex < states.size();
		if (isInX && isInY) {
			final List<Boolean> currentRow = states.get(currentRowIndex);
			boolean isOn = currentRow.get(currentColumnIndex - currentMarker).booleanValue();
			if (isOn) {
				currentLed.setRgbColor(RgbColor.RED);
			} else {
				currentLed.setRgbColor(RgbColor.WHITE);
			}
		} else {
			currentLed.setRgbColor(RgbColor.WHITE);
		}
	}

	@Override
	void iterationEnded() {
		currentMarker--;
		if (currentMarker < -states.get(0).size()) {
			currentMarker = columnCount - 1;
		}
	}
}