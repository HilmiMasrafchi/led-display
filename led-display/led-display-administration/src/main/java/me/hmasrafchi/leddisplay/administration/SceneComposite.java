/**
 * 
 */
package me.hmasrafchi.leddisplay.administration;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;
import me.hmasrafchi.leddisplay.util.CyclicIterator;

/**
 * @author michelin
 *
 */
@Setter
@Entity
class SceneComposite extends Scene {
	@Getter
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn
	private List<Scene> scenes;

	@Transient
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
	public boolean does() {
		final Scene currentScene = scenesIterator.getCurrentElement();
		if (currentScene.does()) {
			scenesIterator.next();
		}
		return scenesIterator.getCurrentElement().does();
	}

	@PostLoad
	public void setScenes() {
		if (scenes != null && !scenes.isEmpty()) {
			this.scenesIterator = new CyclicIterator<>(scenes);
		}
	}
}