/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.model.jpa;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.validation.constraints.Min;

import lombok.Data;

/**
 * @author michelin
 *
 */
@Data
@Entity
public class MatrixEntity {
	@Id
	@GeneratedValue
	private int id;

	@Basic
	@Min(value = 1, message = "row count should be bigger or equal to 1")
	private int rowCount;

	@Basic
	@Min(value = 1, message = "column count should be bigger or equal to 1")
	private int columnCount;

	@OrderColumn
	@OneToMany(mappedBy = "matrix", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<SceneEntity> scenes;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MatrixEntity other = (MatrixEntity) obj;
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