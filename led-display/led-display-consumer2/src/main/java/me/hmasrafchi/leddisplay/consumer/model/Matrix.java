/**
 * 
 */
package me.hmasrafchi.leddisplay.consumer.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * @author michelin
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Matrix {
	private int id;

	private int rowCount;
	private int columnCount;

	private Scene scene;

	public CompiledFrames getCompiledFrames() {
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