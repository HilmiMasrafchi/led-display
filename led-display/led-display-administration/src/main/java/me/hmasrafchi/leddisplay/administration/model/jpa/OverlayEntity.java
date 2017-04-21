/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.model.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;

import lombok.Data;

/**
 * @author michelin
 *
 */
@Data
@Entity
@Inheritance
public abstract class OverlayEntity {
	@Id
	@GeneratedValue
	protected Integer id;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OverlayEntity other = (OverlayEntity) obj;
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