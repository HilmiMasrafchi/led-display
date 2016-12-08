/**
 * 
 */
package me.hmasrafchi.leddisplay.framework;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.framework.scene.Scene;
import me.hmasrafchi.leddisplay.util.CyclicIterator;

/**
 * @author michelin
 *
 */
public final class Matrix {
	private final List<List<Led>> leds;

	private final CopyOnWriteArrayList<Scene> scenes;
	private final CyclicIterator<Scene> scenesIterator;

	private Scene currentScene;

	public Matrix(final List<List<Led>> leds) {
		// check leds being rectangular
		this.leds = new ArrayList<>(leds);

		this.scenes = new CopyOnWriteArrayList<>();

		this.scenesIterator = new CyclicIterator<>(scenes);
		this.currentScene = scenesIterator.next();
	}

	public void nextFrame() {
		if (!scenes.isEmpty()) {
			if (currentScene == null) {
				this.currentScene = scenesIterator.next();
			}
			if (!currentScene.hasNextFrame()) {
				currentScene.reset(leds);
				currentScene = scenesIterator.next();
			}

			currentScene.nextFrame(leds);
		}
	}

	public void addScene(final Scene scene) {
		scenes.add(scene);
	}

	public void removeScene(final Scene scene) {
		scenes.remove(scene);
	}

	public Collection<Led> getAllLeds() {
		return leds.stream().flatMap(row -> row.stream()).collect(Collectors.toList());
	}
}