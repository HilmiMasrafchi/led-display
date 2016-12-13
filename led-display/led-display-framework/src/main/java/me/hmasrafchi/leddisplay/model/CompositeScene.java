/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import com.google.common.base.Preconditions;

import me.hmasrafchi.leddisplay.model.api.Led;
import me.hmasrafchi.leddisplay.util.CyclicIterator;

/**
 * @author michelin
 *
 */
public final class CompositeScene extends Scene {
	private final MatrixIterator matrixIterator;
	private final CopyOnWriteArrayList<Scene> scenes;

	private final CyclicIterator<Scene> scenesIterator;

	public CompositeScene(final Collection<? extends Scene> scenes, final MatrixIterator matrixIterator) {
		Preconditions.checkNotNull(scenes);
		Preconditions.checkArgument(!scenes.isEmpty());

		this.scenes = new CopyOnWriteArrayList<>(scenes);

		this.scenesIterator = new CyclicIterator<>(this.scenes);

		this.matrixIterator = matrixIterator;
	}

	@Override
	public boolean hasNextFrame() {
		return true;
	}

	@Override
	public void nextFrame() {
		final Scene currentScene = scenesIterator.getCurrentElement();
		if (!currentScene.hasNextFrame()) {
			currentScene.reset(matrixIterator);
			scenesIterator.next();
		}

		matrixIterator.iterate((led, columnIndex, rowIndex) -> currentScene.changeLed(led, columnIndex, rowIndex));
		ledIterationEnded();
	}

	@Override
	public void reset(final MatrixIterator matrixIterator) {
		scenes.forEach(scene -> scene.reset(matrixIterator));
	}

	public void addScene(final Scene scene) {
		scenes.add(scene);
	}

	public void removeScene(final Scene scene) {
		scenes.remove(scene);
	}

	@Override
	void changeLed(Led led, int ledColumnIndex, int ledRowIndex) {
		// TODO Auto-generated method stub

	}

	@Override
	void ledIterationEnded() {
		// TODO Auto-generated method stub

	}
}