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
class SceneRollHorizontally extends Scene {
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn
	private List<LedStateRow> states;

	@OneToOne(cascade = CascadeType.ALL)
	private LedRgbColor onColor;

	@OneToOne(cascade = CascadeType.ALL)
	private LedRgbColor offColor;

	private int duration;
}