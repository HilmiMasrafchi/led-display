/**
 * 
 */
package me.hmasrafchi.leddisplay.administration;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

/**
 * @author michelin
 *
 */
@Setter
@Entity
class SceneComposite extends Scene {
	@Getter
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn
	private List<Scene> scenes;
}