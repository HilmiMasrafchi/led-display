/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.api.LedState;
import me.hmasrafchi.leddisplay.api.RgbColor;
import me.hmasrafchi.leddisplay.util.Preconditions;
import me.hmasrafchi.leddisplay.util.TwoDimensionalListRectangular;

/**
 * @author michelin
 *
 */
final class OverlayRollHorizontally implements Overlay {
	private final TwoDimensionalListRectangular<LedState> states;
	private final RgbColor onColor;
	private final RgbColor offColor;
	private final int yPosition;

	private int currentIndexMark;

	OverlayRollHorizontally(final TwoDimensionalListRectangular<LedState> states, final RgbColor onColor,
			final RgbColor offColor, final int beginIndexMark, final int yPosition) {
		this.states = Preconditions.checkNotNull(states);

		this.onColor = Preconditions.checkNotNull(onColor);
		this.offColor = Preconditions.checkNotNull(offColor);

		this.currentIndexMark = beginIndexMark;
		this.yPosition = yPosition;
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
		currentIndexMark--;
	}

	@Override
	public boolean isExhausted() {
		return currentIndexMark + states.getColumnCount() < 0;
	}

	@Override
	public LedState getStateAt(final int ledRowIndex, final int ledColumnIndex) {
		if (ledColumnIndex < currentIndexMark || ledColumnIndex > currentIndexMark + states.getColumnCount() - 1
				|| ledRowIndex < yPosition || ledRowIndex > states.getRowCount() - 1 + yPosition) {
			return LedState.TRANSPARENT;
		}

		return states.getValueAt(ledRowIndex - yPosition, ledColumnIndex - currentIndexMark);
	}

	@Override
	public String toString() {
		return "OverlayRollHorizontal";
	}
}