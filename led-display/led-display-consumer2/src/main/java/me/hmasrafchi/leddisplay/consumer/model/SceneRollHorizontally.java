/**
 * 
 */
package me.hmasrafchi.leddisplay.consumer.model;

import java.util.List;

import me.hmasrafchi.leddisplay.api.LedState;
import me.hmasrafchi.leddisplay.api.RgbColor;

/**
 * @author michelin
 *
 */
class SceneRollHorizontally implements Scene, Overlay {
	private List<LedStateRow> states;
	private RgbColor onColor;
	private RgbColor offColor;
	private int yPosition;

	private int currentIndexMark;

	@Override
	public Led onLedVisited(final Led led, final int ledRowIndex, final int ledColumnIndex) {
		final LedState state = getStateAt(ledRowIndex, ledColumnIndex);
		switch (state) {
		case ON:
			return new Led(onColor);
		case OFF:
			return new Led(offColor);
		case TRANSPARENT:
			return new Led();
		default:
			return new Led();
		}
	}

	@Override
	public void onMatrixIterationEnded() {
		currentIndexMark--;
	}

	@Override
	public boolean isExhausted() {
		return currentIndexMark + states.get(0).getColumnCount() < 0;
	}

	@Override
	public LedState getStateAt(final int ledRowIndex, final int ledColumnIndex) {
		if (ledColumnIndex < currentIndexMark || ledColumnIndex > currentIndexMark + states.get(0).getColumnCount() - 1
				|| ledRowIndex < yPosition || ledRowIndex > states.size() - 1 + yPosition) {
			return LedState.TRANSPARENT;
		}

		return states.get(ledRowIndex - yPosition).getStateAt(ledColumnIndex - currentIndexMark);
	}

	@Override
	public String toString() {
		return "OverlayRollHorizontal";
	}
}