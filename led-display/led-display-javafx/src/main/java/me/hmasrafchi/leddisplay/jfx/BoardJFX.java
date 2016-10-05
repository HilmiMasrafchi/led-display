/**
 * 
 */
package me.hmasrafchi.leddisplay.jfx;

import java.util.Collection;

import javax.inject.Inject;

import com.google.common.base.Preconditions;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import me.hmasrafchi.leddisplay.api.Board;
import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.framework.Matrix;
import me.hmasrafchi.leddisplay.framework.scene.Scene;
import me.hmasrafchi.leddisplay.util.CyclicIterator;

/**
 * @author michelin
 *
 */
public final class BoardJFX extends Pane implements Board {
	private final Matrix matrix;
	private final Timeline timeline;

	private CyclicIterator<Scene> scenesIterator;
	private Scene currentScene;

	@Inject
	public BoardJFX(final Matrix matrix, final Collection<Scene> scenes) {
		Preconditions.checkNotNull(scenes);
		Preconditions.checkArgument(!scenes.isEmpty());

		this.matrix = matrix;

		this.timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				nextFrame();
			}
		}));
		timeline.setCycleCount(Timeline.INDEFINITE);

		this.scenesIterator = new CyclicIterator<>(scenes);
		this.currentScene = scenesIterator.next();

		final Collection<Led> allLeds = this.matrix.getAllLeds();
		for (final Led currentLed : allLeds) {
			getChildren().add((Text) currentLed);
		}
	}

	@Override
	public void nextFrame() {
		if (!currentScene.hasNext()) {
			currentScene = scenesIterator.next();
		}

		for (int currentRowIndex = 0; currentRowIndex < matrix.getRowsCount(); currentRowIndex++) {
			for (int currentColumnIndex = 0; currentColumnIndex < matrix.getColumnsCount(); currentColumnIndex++) {
				final Led led = matrix.getLedAt(currentColumnIndex, currentRowIndex);
				currentScene.nextFrame(led, currentColumnIndex, currentRowIndex);
			}
		}
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