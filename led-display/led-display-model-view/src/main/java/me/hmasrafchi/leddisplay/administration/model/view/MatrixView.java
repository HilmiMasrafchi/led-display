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

/**
 * @author michelin
 *
 */
@Getter
// TODO: remove this
@EqualsAndHashCode(exclude = "compiledFrames")
public final class MatrixView {
	private final Integer id;

	private final String name;
	private final int rowCount;
	private final int columnCount;

	private final List<List<OverlayView>> scenes;

	private final List<List<List<LedView>>> compiledFrames;

	// TODO: remove this constructor, you can pass empty list instead
	public MatrixView(final int rowCount, final int columnCount) {
		this(null, "", rowCount, columnCount, new ArrayList<>(), null);
	}

	public MatrixView(final String name, final int rowCount, final int columnCount) {
		this(null, name, rowCount, columnCount, new ArrayList<>(), null);
	}

	public MatrixView(final int rowCount, final int columnCount, final List<List<OverlayView>> scenes) {
		this(null, "", rowCount, columnCount, scenes, null);
	}

	public MatrixView(final String name, int rowCount, int columnCount, List<List<OverlayView>> scenes) {
		this(null, name, rowCount, columnCount, scenes, null);
	}

	@JsonCreator
	public MatrixView(@JsonProperty("id") final Integer id, @JsonProperty("name") final String name,
			@JsonProperty("rowCount") final int rowCount, @JsonProperty("columnCount") final int columnCount,
			@JsonProperty("scenes") final List<List<OverlayView>> scenes,
			@JsonProperty("compiledFrames") final List<List<List<LedView>>> compiledFrames) {
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("name can not be null or empty");
		}

		this.id = id;
		this.name = name;
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		this.scenes = scenes;
		this.compiledFrames = compiledFrames;
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

	@Override
	public String toString() {
		return name;
	}
}