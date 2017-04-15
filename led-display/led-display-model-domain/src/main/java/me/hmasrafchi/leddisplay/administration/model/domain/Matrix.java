/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.model.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author michelin
 *
 */
public final class Matrix {
	private final int rowCount;
	private final int columnCount;

	private final Optional<Scene> sceneOptional;

	public Matrix(final int rowCount, final int columnCount, final Optional<Scene> sceneOptional) {
		Preconditions.checkArgument(rowCount > 0);
		Preconditions.checkArgument(columnCount > 0);
		Preconditions.checkNotNull(sceneOptional);

		this.rowCount = rowCount;
		this.columnCount = columnCount;
		this.sceneOptional = sceneOptional;
	}

	public Optional<CompiledFrames> getCompiledFrames() {
		return sceneOptional.map(scene -> {
			final List<Frame> compiledFramesList = new ArrayList<>();
			final Frame currentFrame = new Frame(Led::new, rowCount, columnCount);
			while (!scene.isExhausted()) {
				final Frame nextFrame = currentFrame.map(scene::onLedVisited);
				scene.onMatrixIterationEnded();
				compiledFramesList.add(nextFrame);
			}
			return Optional.of(new CompiledFrames(compiledFramesList));
		}).orElse(Optional.empty());
	}
}