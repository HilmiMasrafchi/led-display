/**
 * 
 */
package me.hmasrafchi.leddisplay.model.domain;

import java.util.List;

/**
 * @author michelin
 *
 */
public class OverlayStationary extends Overlay {
	private List<List<Led.State>> states;
	private RgbColor onColor;
	private RgbColor offColor;
	private int duration;

	private int durationCounter = 1;

	public OverlayStationary(final List<List<Led.State>> states, final RgbColor onColor, final RgbColor offColor,
			final int duration) {
		this.states = Preconditions.checkNotNull(states);
		this.onColor = Preconditions.checkNotNull(onColor);
		this.offColor = Preconditions.checkNotNull(offColor);
		Preconditions.checkArgument(duration > 0);
		this.duration = duration;
	}

	public OverlayStationary(final List<List<Led.State>> states, final RgbColor onColor, final RgbColor offColor) {
		this(states, onColor, offColor, 1);
	}

	@Override
	Led onLedVisited(final Led led, final int ledRowIndex, final int ledColumnIndex) {
		final Led.State state = getStateAt(ledRowIndex, ledColumnIndex);
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
	Led.State getStateAt(final int rowIndex, final int columnIndex) {
		// TODO: refactor .get(0).getColumnCount()
		if (rowIndex < 0 || rowIndex >= states.size() || columnIndex < 0 || columnIndex >= states.get(0).size()) {
			return Led.State.TRANSPARENT;
		}

		return states.get(rowIndex).get(columnIndex);
	}
}