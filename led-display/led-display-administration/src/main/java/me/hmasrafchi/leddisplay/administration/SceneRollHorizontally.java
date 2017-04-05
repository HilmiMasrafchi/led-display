/**
 * 
 */
package me.hmasrafchi.leddisplay.administration;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author michelin
 *
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
class SceneRollHorizontally extends Scene implements Overlay {
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn
	private List<LedStateRow> states;

	@OneToOne(cascade = CascadeType.ALL)
	private RgbColor onColor;

	@OneToOne(cascade = CascadeType.ALL)
	private RgbColor offColor;

	private int yPosition;

	private int currentIndexMark;

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
	public boolean does() {
		return currentIndexMark + states.get(0).getColumnCount() < 0;
	}

	@Override
	public LedState getStateAt(final int ledRowIndex, final int ledColumnIndex) {
		if (ledColumnIndex < currentIndexMark || ledColumnIndex > currentIndexMark + states.get(0).getColumnCount() - 1
				|| ledRowIndex < yPosition || ledRowIndex > states.size() - 1 + yPosition) {
			return LedState.TRANSPARENT;
		}

		return states.get(ledRowIndex - yPosition).getStateAt(ledColumnIndex - currentIndexMark);
	}

	@Override
	public String toString() {
		return "OverlayRollHorizontal";
	}
}