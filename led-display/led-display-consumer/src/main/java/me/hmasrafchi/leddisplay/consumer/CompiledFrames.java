/**
 * 
 */
package me.hmasrafchi.leddisplay.consumer;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author michelin
 *
 */
@Data
@Entity
@ToString
@EqualsAndHashCode
public class CompiledFrames {
	@Id
	private Integer matrixId;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn
	@OrderColumn
	private List<Frame> compiledFrames;

	public CompiledFrames(int matrixId, List<Frame> compiledFrames) {
		this.matrixId = matrixId;
		this.compiledFrames = compiledFrames;
	}

	public CompiledFrames() {
	}
}