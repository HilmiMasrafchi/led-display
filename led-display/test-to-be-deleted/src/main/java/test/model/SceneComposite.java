/**
 * 
 */
package test.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

/**
 * @author michelin
 *
 */
@Entity
public class SceneComposite extends Scene {
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn
	@Getter
	@Setter
	private Set<Scene> scenes;

	public SceneComposite() {
		this.scenes = new HashSet<>();
		this.scenes.add(new SceneOverlayed());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((scenes == null) ? 0 : scenes.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SceneComposite other = (SceneComposite) obj;
		if (scenes == null) {
			if (other.scenes != null)
				return false;
		} else if (!scenes.equals(other.scenes))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SceneComposite []";
	}
}