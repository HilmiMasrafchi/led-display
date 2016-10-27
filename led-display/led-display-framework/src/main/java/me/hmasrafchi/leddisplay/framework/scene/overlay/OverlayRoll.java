/**
 * 
 */
package me.hmasrafchi.leddisplay.framework.scene.overlay;

import java.util.List;

import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.api.Led.RgbColor;

/**
 * @author michelin
 *
 */
public final class OverlayRoll implements Overlay {
	private final List<List<Overlay.State>> states;
	private final Led.RgbColor color;
	private final int yPosition;
	private final int matrixWidth;

	private int currentIndexMark;

	public OverlayRoll(List<List<State>> states, RgbColor color, int yPosition, int matrixWidth) {
		this.states = states;
		this.color = color;
		this.yPosition = yPosition;
		this.matrixWidth = matrixWidth;

		this.currentIndexMark = matrixWidth;
	}

	@Override
	public void changeLed(final Led led, final int currentLedColumnIndex, final int currentLedRowIndex) {
		final State state = getStateAt(currentLedColumnIndex, currentLedRowIndex);
		if (state.equals(State.ROLL_ON)) {
			led.setRgbColor(color);
		}
	}

	@Override
	public void matrixIterationEnded() {
		currentIndexMark--;
	}

	@Override
	public boolean isEndReached() {
		return currentIndexMark >= -getWidth() + 1;
	}

	@Override
	public void reset() {
		currentIndexMark = matrixWidth;
	}

	@Override
	public State getStateAt(final int currentLedColumnIndex, final int currentLedRowIndex) {
		if (currentLedColumnIndex < currentIndexMark || currentLedColumnIndex > currentIndexMark + getWidth() - 1
				|| currentLedRowIndex > getHeight() - 1 + yPosition || currentLedRowIndex < yPosition) {
			return State.TRANSPARENT;
		}

		return states.get(currentLedRowIndex - yPosition).get(currentLedColumnIndex - currentIndexMark);
	}

	private int getWidth() {
		return states.get(0).size();
	}

	private int getHeight() {
		return states.size();
	}
}