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

	private String name;
	private int rowCount;
	private int columnCount;

	@OrderColumn
	@JoinColumn
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<SceneEntity> scenes;

	@OrderColumn
	@JoinColumn
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<FrameEntity> compiledFrames;

	public MatrixEntity(final Integer id, final String name, final int rowCount, final int columnCount,
			final List<SceneEntity> scenes, final List<FrameEntity> compiledFrames) {
		this.id = id;
		this.name = name;
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		this.scenes = scenes;
		this.compiledFrames = compiledFrames;
	}

	MatrixEntity() {
	}
}