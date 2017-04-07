/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.model;

import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.hmasrafchi.leddisplay.util.Preconditions;

/**
 * @author michelin
 *
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class OverlayStationary extends Overlay {
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn
	@NotNull
	private List<LedStateRow> states;

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "r", column = @Column(name = "onColorR")),
			@AttributeOverride(name = "g", column = @Column(name = "onColorG")),
			@AttributeOverride(name = "b", column = @Column(name = "onColorB")) })
	@NotNull
	private RgbColor onColor;

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "r", column = @Column(name = "offColorR")),
			@AttributeOverride(name = "g", column = @Column(name = "offColorG")),
			@AttributeOverride(name = "b", column = @Column(name = "offColorB")) })
	@NotNull
	private RgbColor offColor;

	@Min(1)
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

	public OverlayStationary(final List<LedStateRow> states, final RgbColor onColor, final RgbColor offColor,
			final int duration) {
		this.states = Preconditions.checkNotNull(states);
		this.onColor = Preconditions.checkNotNull(onColor);
		this.offColor = Preconditions.checkNotNull(offColor);
		Preconditions.checkArgument(duration > 0);
		this.duration = duration;
	}

	public OverlayStationary(final List<LedStateRow> states, final RgbColor onColor, final RgbColor offColor) {
		this(states, onColor, offColor, 1);
	}

	OverlayStationary() {
	}
}