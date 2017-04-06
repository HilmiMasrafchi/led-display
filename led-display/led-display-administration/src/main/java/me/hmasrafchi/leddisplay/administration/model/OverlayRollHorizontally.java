/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author michelin
 *
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
class OverlayRollHorizontally extends Overlay {
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn
	private List<LedStateRow> states;

	@OneToOne(cascade = CascadeType.ALL)
	private RgbColor onColor;

	@OneToOne(cascade = CascadeType.ALL)
	private RgbColor offColor;

	private int yPosition;

	@Transient
	@JsonIgnore
	private int currentIndexMark;

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
	@JsonIgnore
	boolean isExhausted() {
		return currentIndexMark + states.get(0).getColumnCount() < 0;
	}

	@Override
	@JsonIgnore
	Led.State getStateAt(final int ledRowIndex, final int ledColumnIndex) {
		// TODO: refactor .get(0).getColumnCount()
		if (ledColumnIndex < currentIndexMark || ledColumnIndex > currentIndexMark + states.get(0).getColumnCount() - 1
				|| ledRowIndex < yPosition || ledRowIndex > states.size() - 1 + yPosition) {
			return Led.State.TRANSPARENT;
		}

		return states.get(ledRowIndex - yPosition).getStateAt(ledColumnIndex - currentIndexMark);
	}

	@Override
	public String toString() {
		return "OverlayRollHorizontal";
	}
}