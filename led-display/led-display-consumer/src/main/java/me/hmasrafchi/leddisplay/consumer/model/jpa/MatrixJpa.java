/**
 * 
 */
package me.hmasrafchi.leddisplay.consumer.model.jpa;

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

	@JoinColumn
	@OrderColumn
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<FrameJpa> compiledFrames;

	public MatrixJpa(final Integer matrixId, final List<FrameJpa> compiledFrames) {
		this.matrixId = matrixId;
		this.compiledFrames = compiledFrames;
	}

	MatrixJpa() {
	}
}