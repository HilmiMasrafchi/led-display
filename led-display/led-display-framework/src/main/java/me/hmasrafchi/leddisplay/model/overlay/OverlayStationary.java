/**
 * 
 */
package me.hmasrafchi.leddisplay.model.overlay;

import java.util.List;

import com.google.common.base.Preconditions;

import lombok.Getter;
import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.api.Led.RgbColor;
import me.hmasrafchi.leddisplay.util.TwoDimensionalListRectangular;

/**
 * @author michelin
 *
 */
@Getter
public final class OverlayStationary implements Overlay {
	private final TwoDimensionalListRectangular<State> states;
	private final Led.RgbColor onColor;
	private final Led.RgbColor offColor;

	public OverlayStationary(final List<? extends List<? extends State>> states, final RgbColor onColor,
			final RgbColor offColor) {
		this.states = new TwoDimensionalListRectangular<>(states);
		this.onColor = Preconditions.checkNotNull(onColor);
		this.offColor = Preconditions.checkNotNull(offColor);
	}

	@Override
	public void onLedVisited(final Led led, final int ledColumndIndex, final int ledRowIndex) {
		final Overlay.State state = states.getValueAt(ledColumndIndex, ledRowIndex);
		if (state.equals(Overlay.State.ON)) {
			led.setRgbColor(onColor);
		} else if (state.equals(Overlay.State.OFF)) {
			led.setRgbColor(offColor);
		}
		led.setOpacityLevels(1);
	}

	@Override
	public void onMatrixIterationEnded() {

	}

	@Override
	public boolean isExhausted() {
		return true;
	}

	@Override
	public void onMatrixReset() {

	}

	@Override
	public Overlay.State getStateAt(int columnIndex, int rowIndex) {
		if (columnIndex > states.getColumnCount() - 1 || rowIndex > states.getRowCount() - 1) {
			return State.TRANSPARENT;
		}

		return states.getValueAt(columnIndex, rowIndex);
	}

	@Override
	public String toString() {
		return "OverlayedStationary";
	}
}