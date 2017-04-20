/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.model.view;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * @author michelin
 *
 */
@Getter
@ToString
@EqualsAndHashCode
public final class MatrixView {
	private final Integer id;

	private final int rowCount;
	private final int columnCount;

	private final List<List<OverlayView>> scenes;

	public MatrixView(final int rowCount, final int columnCount) {
		this(null, rowCount, columnCount, new ArrayList<>());
	}

	public MatrixView(final int rowCount, final int columnCount, final List<List<OverlayView>> scenes) {
		this(null, rowCount, columnCount, scenes);
	}

	@JsonCreator
	public MatrixView(@JsonProperty("id") final Integer id, @JsonProperty("rowCount") final int rowCount,
			@JsonProperty("columnCount") final int columnCount,
			@JsonProperty("scenes") final List<List<OverlayView>> scenes) {
		this.id = id;
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		this.scenes = scenes;
	}

	public void appendNewSceneAndAppendOverlayToIt(final OverlayView overlay) {
		final ArrayList<OverlayView> newScene = new ArrayList<>();
		newScene.add(overlay);

		this.scenes.add(newScene);
	}

	public void appendNewOverlayToScene(final int sceneIndex, final OverlayView overlay) {
		final List<OverlayView> scene = this.scenes.get(sceneIndex);
		scene.add(overlay);
	}
}