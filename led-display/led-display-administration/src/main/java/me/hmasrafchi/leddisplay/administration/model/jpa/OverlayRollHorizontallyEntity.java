/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.model.jpa;

import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
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
public class OverlayRollHorizontallyEntity extends OverlayEntity {
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn
	@OrderColumn
	private List<LedStateRowEntity> states;

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

	@Basic
	private int beginIndexMark;

	@Basic
	private int yPosition;

	public OverlayRollHorizontallyEntity(List<LedStateRowEntity> statesPersist, RgbColor onColor, RgbColor offColor,
			int beginIndexMark, int yPosition) {
		this.states = statesPersist;
		this.onColor = onColor;
		this.offColor = offColor;
		this.beginIndexMark = beginIndexMark;
		this.yPosition = yPosition;
	}

	public OverlayRollHorizontallyEntity() {
	}
}