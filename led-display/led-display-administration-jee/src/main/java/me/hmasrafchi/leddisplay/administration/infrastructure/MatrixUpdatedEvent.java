/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.infrastructure;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import me.hmasrafchi.leddisplay.model.view.LedView;

/**
 * @author michelin
 *
 */
@Getter
public final class MatrixUpdatedEvent implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer matrixId;
	private List<List<List<LedView>>> compiledFrames;

	public MatrixUpdatedEvent(final Integer matrixId, final List<List<List<LedView>>> compiledFrames) {
		this.matrixId = matrixId;
		this.compiledFrames = compiledFrames;
	}
}