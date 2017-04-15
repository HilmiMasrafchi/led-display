/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.model.jpa;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

import lombok.Data;

/**
 * @author michelin
 *
 */
@Data
@Entity
public class SceneEntity {
	@Id
	@GeneratedValue
	private int id;

	@ManyToOne
	private MatrixEntity matrix;

	@OrderColumn
	@OneToMany(mappedBy = "scene", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<OverlayEntity> overlays;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SceneEntity other = (SceneEntity) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}
}