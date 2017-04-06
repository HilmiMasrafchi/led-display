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
class OverlayStationary extends Overlay {
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn
	private List<LedStateRow> states;

	@OneToOne(cascade = CascadeType.ALL)
	private RgbColor onColor;

	@OneToOne(cascade = CascadeType.ALL)
	private RgbColor offColor;

	private int duration;

	@Transient
	@JsonIgnore
	private int durationCounter = 1;

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
	@JsonIgnore
	boolean isExhausted() {
		return durationCounter > duration;
	}

	@Override
	@JsonIgnore
	Led.State getStateAt(final int rowIndex, final int columnIndex) {
		// TODO: refactor .get(0).getColumnCount()
		if (rowIndex < 0 || rowIndex >= states.size() || columnIndex < 0
				|| columnIndex >= states.get(0).getColumnCount()) {
			return Led.State.TRANSPARENT;
		}

		return states.get(rowIndex).getStateAt(columnIndex);
	}

	@Override
	public String toString() {
		return "OverlayedStationary";
	}
}