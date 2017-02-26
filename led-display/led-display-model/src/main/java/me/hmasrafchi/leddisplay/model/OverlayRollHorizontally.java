/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import com.google.common.base.Preconditions;

import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.api.LedRgbColor;
import me.hmasrafchi.leddisplay.api.LedState;
import me.hmasrafchi.leddisplay.util.TwoDimensionalListRectangular;

/**
 * @author michelin
 *
 */
public final class OverlayRollHorizontally extends Overlay {
	private final TwoDimensionalListRectangular<LedState> states;
	private final LedRgbColor onColor;
	private final LedRgbColor offColor;
	private final int yPosition;

	private int currentIndexMark;

	public OverlayRollHorizontally(final TwoDimensionalListRectangular<LedState> states, final LedRgbColor onColor,
			final LedRgbColor offColor, final int beginIndexMark, final int yPosition) {
		this.states = Preconditions.checkNotNull(states);

		this.onColor = Preconditions.checkNotNull(onColor);
		this.offColor = Preconditions.checkNotNull(offColor);

		this.currentIndexMark = beginIndexMark;
		this.yPosition = yPosition;
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
		currentIndexMark--;
	}

	@Override
	boolean isExhausted() {
		return currentIndexMark + states.getColumnCount() < 0;
	}

	@Override
	LedState getStateAt(final int ledRowIndex, final int ledColumnIndex) {
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