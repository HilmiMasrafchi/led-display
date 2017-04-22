/**
 * 
 */
package me.hmasrafchi.leddisplay.domain.event;

import java.util.List;

import lombok.Getter;

/**
 * @author michelin
 *
 */
@Getter
public final class MatrixUpdatedEvent {
	private Integer matrixId;
	private List<List<List<LedView>>> compiledFrames;

	public MatrixUpdatedEvent(final Integer matrixId, final List<List<List<LedView>>> compiledFrames) {
		this.matrixId = matrixId;
		this.compiledFrames = compiledFrames;
	}
}