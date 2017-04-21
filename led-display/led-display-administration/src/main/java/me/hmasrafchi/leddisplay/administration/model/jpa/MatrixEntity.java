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
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

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
	private Integer id;

	@Basic
	private int rowCount;

	@Basic
	private int columnCount;

	@OrderColumn
	@JoinColumn
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<SceneEntity> scenes;

	public MatrixEntity(Integer id, int rowCount, int columnCount, List<SceneEntity> scenes) {
		this.id = id;
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		this.scenes = scenes;
	}

	MatrixEntity() {
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