/**
 * 
 */
package me.hmasrafchi.leddisplay.consumer.data.jpa;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
public class MatrixJpa {
	@Id
	private Integer matrixId;

	private int rowCount;
	private int columnCount;

	@JoinColumn
	@OrderColumn
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<FrameJpa> compiledFrames;

	public MatrixJpa(final Integer matrixId, final int rowCount, final int columnCount,
			final List<FrameJpa> compiledFrames) {
		this.matrixId = matrixId;
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		this.compiledFrames = compiledFrames;
	}

	MatrixJpa() {
	}
}