/**
 * 
 */
package me.hmasrafchi.leddisplay.jfx;

import javax.inject.Inject;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import me.hmasrafchi.leddisplay.framework.Board;

/**
 * @author michelin
 *
 */
public final class AnimationJavaFX {
	private final Timeline timeline;

	@Inject
	public AnimationJavaFX(final Board board) {
		this.timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				board.nextFrame();
			}
		}));
		timeline.setCycleCount(Timeline.INDEFINITE);
	}

	public void start() {
		timeline.play();
	}

	public void pause() {
		timeline.play();
	}

	public void stop() {
		timeline.pause();
	}
}