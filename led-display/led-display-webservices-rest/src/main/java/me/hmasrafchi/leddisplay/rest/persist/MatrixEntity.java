/**
 * 
 */
package me.hmasrafchi.leddisplay.rest.persist;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

/**
 * @author michelin
 *
 */
@Data
@Entity
@XmlRootElement
public class MatrixEntity {
	@Id
	@GeneratedValue
	Integer id;

	int columnCount;

	int rowCount;

	@OneToMany(mappedBy = "ownerMatrix")
	List<SceneFrames> sceneFrames;

	public List<SceneFrames> getSceneFrames() {
		if (sceneFrames == null) {
			sceneFrames = new ArrayList<>();
		}

		return sceneFrames;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MatrixEntity other = (MatrixEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
}

@Entity
class Scene {
	@Id
	@GeneratedValue
	int id;
}
