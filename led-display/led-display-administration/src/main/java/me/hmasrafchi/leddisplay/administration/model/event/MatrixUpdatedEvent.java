/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.model.event;

import java.util.List;

import me.hmasrafchi.leddisplay.administration.model.domain.Led;

/**
 * @author michelin
 *
 */
public final class MatrixUpdatedEvent {
	private Integer matrixId;
	private List<List<List<Led>>> compiledFrames;

	public MatrixUpdatedEvent(final Integer matrixId, final List<List<List<Led>>> compiledFrames) {
		this.matrixId = matrixId;
		this.compiledFrames = compiledFrames;
	}
}