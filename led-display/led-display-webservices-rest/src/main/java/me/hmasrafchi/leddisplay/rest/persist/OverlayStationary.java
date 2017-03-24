/**
 * 
 */
package me.hmasrafchi.leddisplay.rest.persist;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
public class OverlayStationary extends Overlay {
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn
	List<LedStateRow> states;
	@OneToOne(cascade = CascadeType.ALL)
	LedRgbColor onColor;
	@OneToOne(cascade = CascadeType.ALL)
	LedRgbColor offColor;
	int duration;

	public OverlayStationary() {
	}

	public OverlayStationary(final List<LedStateRow> states, final LedRgbColor onColor, final LedRgbColor offColor,
			final int duration) {
		this.states = states;
		this.onColor = onColor;
		this.offColor = offColor;
		this.duration = duration;
	}
}