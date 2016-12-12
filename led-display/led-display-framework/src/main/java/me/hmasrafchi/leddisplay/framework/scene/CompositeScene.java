/**
 * 
 */
package me.hmasrafchi.leddisplay.framework.scene;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import com.google.common.base.Preconditions;

import me.hmasrafchi.leddisplay.framework.Matrix;
import me.hmasrafchi.leddisplay.util.CyclicIterator;

/**
 * @author michelin
 *
 */
public final class CompositeScene implements Scene {
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
	public void nextFrame(final Matrix matrix) {
		if (!currentScene.hasNextFrame()) {
			currentScene.reset(matrix);
			currentScene = scenesIterator.next();
		}

		currentScene.nextFrame(matrix);
	}

	@Override
	public void reset(final Matrix matrix) {
		scenes.forEach(scene -> scene.reset(matrix));
	}

	public void addScene(final Scene scene) {
		scenes.add(scene);
	}

	public void removeScene(final Scene scene) {
		scenes.remove(scene);
	}
}