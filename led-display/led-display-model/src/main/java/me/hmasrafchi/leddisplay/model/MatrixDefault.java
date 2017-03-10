/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;

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
@RequestScoped
@RequiredArgsConstructor
class MatrixDefault implements Matrix, Serializable {
	private static final long serialVersionUID = -2660964310070724009L;

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