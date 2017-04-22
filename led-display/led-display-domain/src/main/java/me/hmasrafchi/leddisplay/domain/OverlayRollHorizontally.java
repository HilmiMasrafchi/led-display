/**
 * 
 */
package me.hmasrafchi.leddisplay.domain;

import java.util.List;

/**
 * @author michelin
 *
 */
public class OverlayRollHorizontally extends Overlay {
	private final List<List<Led.State>> states;
	private final RgbColor onColor;
	private final RgbColor offColor;
	private final int yPosition;

	private int currentIndexMark;

	public OverlayRollHorizontally(final List<List<Led.State>> states, final RgbColor onColor, final RgbColor offColor,
			final int beginIndexMark, final int yPosition) {
		this.states = Preconditions.checkNotNull(states);

		this.onColor = Preconditions.checkNotNull(onColor);
		this.offColor = Preconditions.checkNotNull(offColor);

		this.currentIndexMark = beginIndexMark;
		this.yPosition = yPosition;
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
		currentIndexMark--;
	}

	@Override
	boolean isExhausted() {
		return currentIndexMark + states.get(0).size() < 0;
	}

	@Override
	Led.State getStateAt(final int ledRowIndex, final int ledColumnIndex) {
		// TODO: refactor .get(0).getColumnCount()
		if (ledColumnIndex < currentIndexMark || ledColumnIndex > currentIndexMark + states.get(0).size() - 1
				|| ledRowIndex < yPosition || ledRowIndex > states.size() - 1 + yPosition) {
			return Led.State.TRANSPARENT;
		}

		return states.get(ledRowIndex - yPosition).get(ledColumnIndex - currentIndexMark);
	}
}