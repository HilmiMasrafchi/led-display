/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.model.view;

import static java.util.Collections.emptyList;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

/**
 * @author michelin
 *
 */
@Value
public final class CreateMatrixCommand {
	private final String name;
	private final int rowCount;
	private final int columnCount;

	private final List<List<OverlayView>> scenes;

	@JsonCreator
	public CreateMatrixCommand(@JsonProperty("name") final String name, @JsonProperty("rowCount") final int rowCount,
			@JsonProperty("columnCount") final int columnCount,
			@JsonProperty("scenes") final List<List<OverlayView>> scenes) {
		this.name = name;
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		this.scenes = scenes;
	}

	public CreateMatrixCommand(final int rowCount, final int columnCount) {
		this("", rowCount, columnCount, emptyList());
	}
}