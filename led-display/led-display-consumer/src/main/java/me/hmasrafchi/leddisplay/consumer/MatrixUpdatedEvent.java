/**
 * 
 */
package me.hmasrafchi.leddisplay.consumer;

import java.util.List;

import lombok.Data;

/**
 * @author michelin
 *
 */
@Data
public class MatrixUpdatedEvent {
	private int matrixId;
	private List<List<List<Led>>> compiledFrames;

	public MatrixUpdatedEvent() {
	}

	public MatrixUpdatedEvent(int matrixId, List<List<List<Led>>> compiledFrames) {
		this.matrixId = matrixId;
		this.compiledFrames = compiledFrames;
	}
}