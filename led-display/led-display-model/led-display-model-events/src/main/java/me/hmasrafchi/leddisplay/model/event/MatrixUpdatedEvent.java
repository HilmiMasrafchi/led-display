/**
 * 
 */
package me.hmasrafchi.leddisplay.model.event;

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

	private final Integer matrixId;
	private final int rowCount;
	private final int columnCount;
	// TODO: maybe implement own model, it drags the view module
	private final List<List<List<LedView>>> compiledFrames;

	public MatrixUpdatedEvent(final Integer matrixId, final int rowCount, final int columnCount,
			final List<List<List<LedView>>> compiledFrames) {
		this.matrixId = matrixId;
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		this.compiledFrames = compiledFrames;
	}
}