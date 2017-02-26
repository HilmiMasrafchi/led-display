/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import static me.hmasrafchi.leddisplay.api.LedState.TRANSPARENT;

import com.google.common.base.Preconditions;

import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.api.LedRgbColor;
import me.hmasrafchi.leddisplay.api.LedState;
import me.hmasrafchi.leddisplay.util.TwoDimensionalListRectangular;

/**
 * @author michelin
 *
 */
public final class OverlayStationary extends Overlay {
	private final TwoDimensionalListRectangular<LedState> states;
	private final LedRgbColor onColor;
	private final LedRgbColor offColor;
	private final int duration;

	private int durationCounter = 1;

	// TODO: make constructor with default duration of 1
	public OverlayStationary(final TwoDimensionalListRectangular<LedState> states, final LedRgbColor onColor,
			final LedRgbColor offColor, final int duration) {
		this.states = Preconditions.checkNotNull(states);
		this.onColor = Preconditions.checkNotNull(onColor);
		this.offColor = Preconditions.checkNotNull(offColor);
		Preconditions.checkArgument(duration > 0);
		this.duration = duration;
	}

	@Override
	Led onLedVisited(final int ledRowIndex, final int ledColumnIndex) {
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
	void onMatrixIterationEnded() {
		durationCounter++;
	}

	@Override
	boolean isExhausted() {
		return durationCounter > duration;
	}

	@Override
	LedState getStateAt(final int rowIndex, final int columnIndex) {
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