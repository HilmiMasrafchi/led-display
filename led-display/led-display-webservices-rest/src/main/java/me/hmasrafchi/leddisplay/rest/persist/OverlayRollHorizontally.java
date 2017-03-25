/**
 * 
 */
package me.hmasrafchi.leddisplay.rest.persist;

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
public class OverlayRollHorizontally extends Overlay {
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn
	List<LedStateRow> states;
	@OneToOne(cascade = CascadeType.ALL)
	LedRgbColor onColor;
	@OneToOne(cascade = CascadeType.ALL)
	LedRgbColor offColor;
	int beginIndexMark;
	int yposition;

	public OverlayRollHorizontally() {
	}

	public OverlayRollHorizontally(List<LedStateRow> states, LedRgbColor onColor, LedRgbColor offColor,
			int beginIndexMark, int yposition) {
		this.states = states;
		this.onColor = onColor;
		this.offColor = offColor;
		this.beginIndexMark = beginIndexMark;
		this.yposition = yposition;
	}
}