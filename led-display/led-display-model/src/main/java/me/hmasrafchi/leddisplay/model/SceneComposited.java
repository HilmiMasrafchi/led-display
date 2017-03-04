/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.api.Scene;
import me.hmasrafchi.leddisplay.util.CyclicIterator;

/**
 * @author michelin
 *
 */
final class SceneComposited implements Scene {
	@Getter
	private final List<Scene> scenes;
	private final CyclicIterator<Scene> scenesIterator;

	public SceneComposited(final List<? extends Scene> scenes) {
		this.scenes = new ArrayList<>(scenes);
		this.scenesIterator = new CyclicIterator<>(scenes);
	}

	@Override
	public Led onLedVisited(final Led led, final int ledRowIndex, final int ledColumnIndex) {
		final Scene currentScene = scenesIterator.getCurrentElement();
		return currentScene.onLedVisited(led, ledRowIndex, ledColumnIndex);
	}

	@Override
	public void onMatrixIterationEnded() {
		final Scene currentScene = scenesIterator.getCurrentElement();
		currentScene.onMatrixIterationEnded();
	}

	@Override
	public boolean isExhausted() {
		final Scene currentScene = scenesIterator.getCurrentElement();
		if (currentScene.isExhausted()) {
			scenesIterator.next();
		}
		return scenesIterator.getCurrentElement().isExhausted();
	}
}