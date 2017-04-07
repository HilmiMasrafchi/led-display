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
import javax.persistence.OrderColumn;
import javax.persistence.PostLoad;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import me.hmasrafchi.leddisplay.administration.infrastructure.Preconditions;

/**
 * @author michelin
 *
 */
@Data
@Entity
@ToString
@EqualsAndHashCode(callSuper = true, exclude = "currentIndexMark")
public class OverlayRollHorizontally extends Overlay {
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn
	@OrderColumn
	private List<LedStateRow> states;

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "r", column = @Column(name = "onColorR")),
			@AttributeOverride(name = "g", column = @Column(name = "onColorG")),
			@AttributeOverride(name = "b", column = @Column(name = "onColorB")) })
	private RgbColor onColor;

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "r", column = @Column(name = "offColorR")),
			@AttributeOverride(name = "g", column = @Column(name = "offColorG")),
			@AttributeOverride(name = "b", column = @Column(name = "offColorB")) })
	private RgbColor offColor;

	private int beginIndexMark;

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

	OverlayRollHorizontally() {
	}

	public OverlayRollHorizontally(final List<LedStateRow> states, final RgbColor onColor, final RgbColor offColor,
			final int beginIndexMark, final int yPosition) {
		this.states = Preconditions.checkNotNull(states);

		this.onColor = Preconditions.checkNotNull(onColor);
		this.offColor = Preconditions.checkNotNull(offColor);

		this.beginIndexMark = beginIndexMark;
		this.currentIndexMark = beginIndexMark;
		this.yPosition = yPosition;
	}

	@PostLoad
	void setCurrentIndexMark() {
		this.currentIndexMark = beginIndexMark;
	}
}