/**
 * 
 */
package me.hmasrafchi.leddisplay.consumer.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import me.hmasrafchi.leddisplay.util.CyclicIterator;

/**
 * @author michelin
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class SceneComposite implements Scene {
	private List<Scene> scenes;

	private CyclicIterator<Scene> scenesIterator;

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

	public void setScenes(List<Scene> scenes) {
		this.scenesIterator = new CyclicIterator<>(scenes);
	}
}