/**
 * 
 */
package me.hmasrafchi.leddisplay.jfx;

import java.time.Duration;

import javax.inject.Inject;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import me.hmasrafchi.leddisplay.model.Scene;
import me.hmasrafchi.leddisplay.model.api.Board;

/**
 * @author michelin
 *
 */
public final class BoardJFX implements Board {
	private final Timeline timeline;

	@Inject
	public BoardJFX(final Scene scene, final Duration delayBetweenFrames) {
		this.timeline = new Timeline();
		final EventHandler<ActionEvent> eventHandler = event -> scene.nextFrame();
		final KeyFrame keyFrame = new KeyFrame(convertToJavaFxDuration(delayBetweenFrames), eventHandler);
		timeline.getKeyFrames().add(keyFrame);
		timeline.setCycleCount(Timeline.INDEFINITE);
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