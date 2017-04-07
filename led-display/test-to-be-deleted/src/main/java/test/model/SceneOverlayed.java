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
public class SceneOverlayed extends Scene {
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn
	@Getter
	@Setter
	private Set<Scene> overlays;

	public SceneOverlayed() {
		this.overlays = new HashSet<>();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((overlays == null) ? 0 : overlays.hashCode());
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
		SceneOverlayed other = (SceneOverlayed) obj;
		if (overlays == null) {
			if (other.overlays != null)
				return false;
		} else if (!overlays.equals(other.overlays))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SceneOverlayed [overlays=" + overlays + "]";
	}
}