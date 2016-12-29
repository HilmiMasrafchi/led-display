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
public final class OverlayRollHorizontal implements Overlay {
	@Getter
	private final TwoDimensionalListRectangular<Overlay.State> states;
	@Getter
	private final Led.RgbColor onColor;
	@Getter
	private final Led.RgbColor offColor;
	@Getter
	private final int yPosition;
	@Getter
	private final int matrixWidth;

	private int currentIndexMark;

	public OverlayRollHorizontal(final List<? extends List<? extends Overlay.State>> states, final RgbColor onColor,
			final RgbColor offColor, final int yPosition, final int matrixWidth) {
		this.states = new TwoDimensionalListRectangular<>(states);

		this.onColor = Preconditions.checkNotNull(onColor);
		this.offColor = Preconditions.checkNotNull(offColor);

		Preconditions.checkArgument(yPosition >= 0);
		this.yPosition = yPosition;

		Preconditions.checkArgument(matrixWidth > 0);
		this.matrixWidth = matrixWidth;

		this.currentIndexMark = matrixWidth;
	}

	@Override
	public void onLedVisited(final Led led, final int currentLedColumnIndex, final int currentLedRowIndex) {
		final State state = getStateAt(currentLedColumnIndex, currentLedRowIndex);
		switch (state) {
		case ON:
			led.setRgbColor(onColor);
			break;
		case OFF:
			led.setRgbColor(offColor);
			break;
		case TRANSPARENT:
			led.reset();
			break;
		}
	}

	@Override
	public void onMatrixIterationEnded() {
		currentIndexMark--;
	}

	@Override
	public boolean isExhausted() {
		return currentIndexMark <= -states.getColumnCount();
	}

	@Override
	public void onMatrixReset() {
		currentIndexMark = matrixWidth - 1;
	}

	@Override
	public State getStateAt(final int currentLedColumnIndex, final int currentLedRowIndex) {
		if (currentLedColumnIndex < currentIndexMark
				|| currentLedColumnIndex > currentIndexMark + states.getColumnCount() - 1
				|| currentLedRowIndex > states.getRowCount() - 1 + yPosition || currentLedRowIndex < yPosition) {
			return State.TRANSPARENT;
		}

		return states.getValueAt(currentLedColumnIndex - currentIndexMark, currentLedRowIndex - yPosition);
	}

	@Override
	public String toString() {
		return "OverlayRollHorizontal";
	}
}