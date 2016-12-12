/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import com.google.common.base.Preconditions;

import me.hmasrafchi.leddisplay.util.CyclicIterator;

/**
 * @author michelin
 *
 */
public final class CompositeScene extends Scene {
	private final CopyOnWriteArrayList<Scene> scenes;

	private final CyclicIterator<Scene> scenesIterator;

	private Scene currentScene;

	public CompositeScene(final Collection<? extends Scene> scenes) {
		Preconditions.checkNotNull(scenes);
		Preconditions.checkArgument(!scenes.isEmpty());

		this.scenes = new CopyOnWriteArrayList<>(scenes);

		this.scenesIterator = new CyclicIterator<>(this.scenes);
		this.currentScene = scenesIterator.next();
	}

	@Override
	public boolean hasNextFrame() {
		return true;
	}

	@Override
	public void nextFrame(final MatrixIterator matrixIterator) {
		if (!currentScene.hasNextFrame()) {
			currentScene.reset(matrixIterator);
			currentScene = scenesIterator.next();
		}

		currentScene.nextFrame(matrixIterator);
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
}