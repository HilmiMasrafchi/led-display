/**
 * 
 */
package me.hmasrafchi.leddisplay.jfx;

import java.time.Duration;

import com.google.common.base.Preconditions;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import me.hmasrafchi.leddisplay.api.Board;
import me.hmasrafchi.leddisplay.model.Matrix;

/**
 * @author michelin
 *
 */
public final class BoardJFX implements Board {
	private final Timeline timeline;

	public BoardJFX(final Matrix matrix, final Duration delayBetweenFrames) {
		Preconditions.checkNotNull(matrix);
		Preconditions.checkNotNull(delayBetweenFrames);
		Preconditions.checkArgument(!delayBetweenFrames.isNegative() && !delayBetweenFrames.isZero());

		final EventHandler<ActionEvent> eventHandler = event -> matrix.nextFrame();
		final KeyFrame keyFrame = new KeyFrame(convertToJavaFxDuration(delayBetweenFrames), eventHandler);
		this.timeline = new Timeline(keyFrame);
		this.timeline.setCycleCount(Timeline.INDEFINITE);
	}

	private javafx.util.Duration convertToJavaFxDuration(final Duration duration) {
		return new javafx.util.Duration(duration.toMillis());
	}

	@Override
	public void startAnimation() {
		timeline.play();
	}

	@Override
	public void pauseAnimation() {
		timeline.play();
	}

	@Override
	public void stopAnimation() {
		timeline.pause();
	}
}