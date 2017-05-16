/**
 * 
 */
package me.hmasrafchi.leddisplay.data.jpa;

import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author michelin
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class OverlayStationaryEntity extends OverlayEntity {
	@JoinColumn
	@OrderColumn
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<LedStateRowEntity> states;

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "r", column = @Column(name = "onColorR")),
			@AttributeOverride(name = "g", column = @Column(name = "onColorG")),
			@AttributeOverride(name = "b", column = @Column(name = "onColorB")) })
	private RgbColorEmbeddable onColor;

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "r", column = @Column(name = "offColorR")),
			@AttributeOverride(name = "g", column = @Column(name = "offColorG")),
			@AttributeOverride(name = "b", column = @Column(name = "offColorB")) })
	private RgbColorEmbeddable offColor;

	private int duration;

	public OverlayStationaryEntity(final List<LedStateRowEntity> states, final RgbColorEmbeddable onColor,
			final RgbColorEmbeddable offColor, final int duration) {
		this.states = states;
		this.onColor = onColor;
		this.offColor = offColor;
		this.duration = duration;
	}

	OverlayStationaryEntity() {
	}
}