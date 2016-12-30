/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import com.google.common.base.Preconditions;

import me.hmasrafchi.leddisplay.model.Led.RgbColor;
import me.hmasrafchi.leddisplay.util.TwoDimensionalListRectangular;

/**
 * @author michelin
 *
 */
public final class OverlayRollHorizontally extends Overlay {
	private final TwoDimensionalListRectangular<State> states;
	private final RgbColor onColor;
	private final RgbColor offColor;
	private final int yPosition;

	private int currentIndexMark;

	public OverlayRollHorizontally(final TwoDimensionalListRectangular<State> states, final RgbColor onColor,
			final RgbColor offColor, final int beginIndexMark, final int yPosition) {
		this.states = Preconditions.checkNotNull(states);

		this.onColor = Preconditions.checkNotNull(onColor);
		this.offColor = Preconditions.checkNotNull(offColor);

		this.currentIndexMark = beginIndexMark;
		this.yPosition = yPosition;
	}

	@Override
	Led onLedVisited(final int ledRowIndex, final int ledColumnIndex) {
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
		currentIndexMark--;
	}

	@Override
	boolean isExhausted() {
		return currentIndexMark + states.getColumnCount() < 0;
	}

	@Override
	State getStateAt(final int ledRowIndex, final int ledColumnIndex) {
		if (ledColumnIndex < currentIndexMark || ledColumnIndex > currentIndexMark + states.getColumnCount() - 1
				|| ledRowIndex < yPosition || ledRowIndex > states.getRowCount() - 1 + yPosition) {
			return State.TRANSPARENT;
		}

		return states.getValueAt(ledRowIndex - yPosition, ledColumnIndex - currentIndexMark);
	}

	@Override
	public String toString() {
		return "OverlayRollHorizontal";
	}
}