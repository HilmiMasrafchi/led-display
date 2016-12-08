/**
 * 
 */
package me.hmasrafchi.leddisplay.jfx;

import java.time.Duration;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.inject.Inject;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import me.hmasrafchi.leddisplay.api.Board;
import me.hmasrafchi.leddisplay.framework.Matrix;

/**
 * @author michelin
 *
 */
public final class BoardJFX extends Pane implements Board {
	private final Timeline timeline;

	@Inject
	public BoardJFX(final Matrix matrix, final Duration delayBetweenFrames) {
		this.timeline = new Timeline();
		final EventHandler<ActionEvent> eventHandler = event -> matrix.nextFrame();
		final KeyFrame keyFrame = new KeyFrame(convertToJavaFxDuration(delayBetweenFrames), eventHandler);
		timeline.getKeyFrames().add(keyFrame);
		timeline.setCycleCount(Timeline.INDEFINITE);

		final Collection<Text> textNodes = matrix.getAllLeds().stream().map(led -> (Text) led)
				.collect(Collectors.toList());
		getChildren().addAll(textNodes);
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