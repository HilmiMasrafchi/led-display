/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import static me.hmasrafchi.leddisplay.api.LedState.TRANSPARENT;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonTypeName;

import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.api.LedState;
import me.hmasrafchi.leddisplay.api.RgbColor;
import me.hmasrafchi.leddisplay.util.Preconditions;
import me.hmasrafchi.leddisplay.util.TwoDimensionalListRectangular;

/**
 * @author michelin
 *
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@JsonTypeName("overlayStationary")
final class OverlayStationary implements Overlay {
	private final TwoDimensionalListRectangular<LedState> states;
	private final RgbColor onColor;
	private final RgbColor offColor;
	private final int duration;

	private int durationCounter = 1;

	OverlayStationary(final TwoDimensionalListRectangular<LedState> states, final RgbColor onColor,
			final RgbColor offColor) {
		this(states, onColor, offColor, 1);
	}

	OverlayStationary(final TwoDimensionalListRectangular<LedState> states, final RgbColor onColor,
			final RgbColor offColor, final int duration) {
		this.states = Preconditions.checkNotNull(states);
		this.onColor = Preconditions.checkNotNull(onColor);
		this.offColor = Preconditions.checkNotNull(offColor);
		Preconditions.checkArgument(duration > 0);
		this.duration = duration;
	}

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
		durationCounter++;
	}

	@Override
	public boolean isExhausted() {
		return durationCounter > duration;
	}

	@Override
	public LedState getStateAt(final int rowIndex, final int columnIndex) {
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