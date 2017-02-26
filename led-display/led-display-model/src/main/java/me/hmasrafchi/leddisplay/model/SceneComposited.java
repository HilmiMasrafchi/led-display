/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import java.util.ArrayList;
import java.util.Collection;

import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.util.CyclicIterator;

/**
 * @author michelin
 *
 */
public final class SceneComposited extends Scene {
	private final Collection<Scene> scenes;
	private final CyclicIterator<Scene> scenesIterator;

	public SceneComposited(final Collection<? extends Scene> scenes) {
		this.scenes = new ArrayList<>(scenes);
		this.scenesIterator = new CyclicIterator<>(scenes);
	}

	@Override
	Led onLedVisited(final int ledRowIndex, final int ledColumnIndex) {
		final Scene currentScene = scenesIterator.getCurrentElement();
		return currentScene.onLedVisited(ledRowIndex, ledColumnIndex);
	}

	@Override
	void onMatrixIterationEnded() {
		final Scene currentScene = scenesIterator.getCurrentElement();
		currentScene.onMatrixIterationEnded();
	}

	@Override
	boolean isExhausted() {
		final Scene currentScene = scenesIterator.getCurrentElement();
		if (currentScene.isExhausted()) {
			scenesIterator.next();
		}
		return scenesIterator.getCurrentElement().isExhausted();
	}
}