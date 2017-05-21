/**
 * 
 */
package me.hmasrafchi.leddisplay.model.event;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import me.hmasrafchi.leddisplay.model.view.LedView;

/**
 * @author michelin
 *
 */
@Getter
public final class MatrixUpdatedEvent implements Serializable {
	private static final long serialVersionUID = 1L;

	private final BigInteger id;
	private final int rowCount;
	private final int columnCount;
	// TODO: maybe implement own model, it drags the view module
	private final List<List<List<LedView>>> compiledFrames;

	@JsonCreator
	public MatrixUpdatedEvent(@JsonProperty("id") final BigInteger id, @JsonProperty("rowCount") final int rowCount,
			@JsonProperty("columnCount") final int columnCount,
			@JsonProperty("compiledFrames") final List<List<List<LedView>>> compiledFrames) {
		this.id = id;
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		this.compiledFrames = compiledFrames;
	}
}