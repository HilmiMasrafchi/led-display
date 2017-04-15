/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.model.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * @author michelin
 *
 */
public final class SceneComposite extends Scene {
	private final Iterator<? extends Scene> scenesIterator;

	private Scene currentScene;

	public SceneComposite(final Collection<? extends Scene> scenes) {
		Preconditions.checkNotNull(scenes);
		Preconditions.checkArgument(!scenes.isEmpty());
		this.scenesIterator = Collections.unmodifiableCollection(scenes).iterator();
		this.currentScene = scenesIterator.next();
	}

	@Override
	Led onLedVisited(final Led led, final int ledRowIndex, final int ledColumnIndex) {
		return currentScene.onLedVisited(led, ledRowIndex, ledColumnIndex);
	}

	@Override
	void onMatrixIterationEnded() {
		currentScene.onMatrixIterationEnded();
	}

	@Override
	boolean isExhausted() {
		if (currentScene.isExhausted() && scenesIterator.hasNext()) {
			currentScene = scenesIterator.next();
		}
		return currentScene.isExhausted();
	}
}