/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import static me.hmasrafchi.leddisplay.model.Overlay.State.TRANSPARENT;

import com.google.common.base.Preconditions;

import me.hmasrafchi.leddisplay.model.Led.RgbColor;
import me.hmasrafchi.leddisplay.util.TwoDimensionalListRectangular;

/**
 * @author michelin
 *
 */
public final class OverlayStationary extends Overlay {
	private final TwoDimensionalListRectangular<State> states;
	private final RgbColor onColor;
	private final RgbColor offColor;

	private boolean isExhaustedCalled = false;

	public OverlayStationary(final TwoDimensionalListRectangular<State> states, final RgbColor onColor,
			final RgbColor offColor) {
		this.states = Preconditions.checkNotNull(states);
		this.onColor = Preconditions.checkNotNull(onColor);
		this.offColor = Preconditions.checkNotNull(offColor);
	}

	@Override
	Led onLedVisited(final int ledRowIndex, final int ledColumnIndex) {
		isExhaustedCalled = true;

		final State state = getStateAt(ledRowIndex, ledColumnIndex);
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
	void onMatrixIterationEnded() {

	}

	@Override
	boolean isExhausted() {
		return isExhaustedCalled;
	}

	@Override
	Overlay.State getStateAt(final int rowIndex, final int columnIndex) {
		if (rowIndex < 0 || rowIndex >= states.getRowCount() || columnIndex < 0
				|| columnIndex >= states.getColumnCount()) {
			return TRANSPARENT;
		}
		return states.getValueAt(rowIndex, columnIndex);
	}

	@Override
	public String toString() {
		return "OverlayedStationary";
	}
}