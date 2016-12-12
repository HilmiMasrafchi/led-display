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
import me.hmasrafchi.leddisplay.model.Matrix;
import me.hmasrafchi.leddisplay.model.MatrixIterator;
import me.hmasrafchi.leddisplay.model.MatrixIteratorLeftToRightTopToBottom;
import me.hmasrafchi.leddisplay.model.Scene;
import me.hmasrafchi.leddisplay.model.api.Board;

/**
 * @author michelin
 *
 */
public final class BoardJFX extends Pane implements Board {
	private final Timeline timeline;

	@Inject
	public BoardJFX(final Scene scene, final Matrix matrix, final Duration delayBetweenFrames) {
		this.timeline = new Timeline();
		final MatrixIterator matrixIterator = new MatrixIteratorLeftToRightTopToBottom(matrix);
		final EventHandler<ActionEvent> eventHandler = event -> scene.nextFrame(matrixIterator);
		final KeyFrame keyFrame = new KeyFrame(convertToJavaFxDuration(delayBetweenFrames), eventHandler);
		timeline.getKeyFrames().add(keyFrame);
		timeline.setCycleCount(Timeline.INDEFINITE);

		final Collection<Text> textNodes = matrix.stream().map(led -> (Text) led).collect(Collectors.toList());
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