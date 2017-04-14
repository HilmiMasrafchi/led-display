/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application;

import java.util.List;

import lombok.Data;
import me.hmasrafchi.leddisplay.administration.model.Led;

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