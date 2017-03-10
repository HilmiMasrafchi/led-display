/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import javax.enterprise.inject.Default;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.hmasrafchi.leddisplay.api.CompiledFrames;
import me.hmasrafchi.leddisplay.api.Frame;
import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.api.Matrix;
import me.hmasrafchi.leddisplay.api.Scene;

/**
 * @author michelin
 *
 */
@ToString
@RequiredArgsConstructor
@Default
public final class MatrixDefault implements Matrix {
	@Override
	public CompiledFrames compile(final Scene scene, final int rowCount, final int columnCount) {
		final CompiledFrames compiledFrames = new CompiledFrames();
		final Frame currentFrame = new Frame(Led::new, rowCount, columnCount);
		while (!scene.isExhausted()) {
			final Frame nextFrame = currentFrame.map(scene::onLedVisited);
			scene.onMatrixIterationEnded();
			compiledFrames.addFrame(nextFrame);
		}

		return compiledFrames;
	}
}