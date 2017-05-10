/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.infrastructure;

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

/**
 * @author michelin
 *
 */
@Data
@Entity
public class OverlayRollHorizontallyEntity extends OverlayEntity {
	@OrderColumn
	@JoinColumn
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

	private int beginIndexMark;
	private int yPosition;

	public OverlayRollHorizontallyEntity(final List<LedStateRowEntity> states, final RgbColorEmbeddable onColor,
			final RgbColorEmbeddable offColor, final int beginIndexMark, final int yPosition) {
		this.states = states;
		this.onColor = onColor;
		this.offColor = offColor;
		this.beginIndexMark = beginIndexMark;
		this.yPosition = yPosition;
	}

	OverlayRollHorizontallyEntity() {
	}
}