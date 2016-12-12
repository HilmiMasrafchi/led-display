/**
 * 
 */
package me.hmasrafchi.leddisplay.model.scene.overlay;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.hmasrafchi.leddisplay.model.api.Led;

/**
 * @author michelin
 *
 */
@Getter
@RequiredArgsConstructor
public final class OverlayStationary implements Overlay {
	private final List<List<Overlay.State>> states;
	private final Led.RgbColor onColor;
	private final Led.RgbColor offColor;

	@Override
	public void changeLed(final Led led, final int ledColumndIndex, final int ledRowIndex) {
		final Overlay.State state = states.get(ledRowIndex).get(ledColumndIndex);
		if (state.equals(Overlay.State.ON)) {
			led.setRgbColor(onColor);
		} else if (state.equals(Overlay.State.OFF)) {
			led.setRgbColor(offColor);
		}
	}

	@Override
	public void matrixIterationEnded() {

	}

	@Override
	public boolean isEndReached() {
		return false;
	}

	@Override
	public void reset() {

	}

	@Override
	public Overlay.State getStateAt(int columnIndex, int rowIndex) {
		if (columnIndex > getWidth() - 1 || rowIndex > getHeight() - 1) {
			return State.TRANSPARENT;
		}

		return states.get(rowIndex).get(columnIndex);
	}

	private int getWidth() {
		return states.get(0).size();
	}

	private int getHeight() {
		return states.size();
	}

	@Override
	public String toString() {
		return "OverlayedStationary";
	}
}