/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.PostLoad;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.hmasrafchi.leddisplay.administration.infrastructure.CyclicIterator;

/**
 * @author michelin
 *
 */
@Setter
@Entity
@EqualsAndHashCode(callSuper = true, exclude = "scenesIterator")
@ToString
public class SceneComposite extends Scene {
	@Getter
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn
	@OrderColumn
	private List<Scene> scenes;

	@Transient
	private CyclicIterator<Scene> scenesIterator;

	@Override
	Led onLedVisited(final Led led, final int ledRowIndex, final int ledColumnIndex) {
		final Scene currentScene = scenesIterator.getCurrentElement();
		return currentScene.onLedVisited(led, ledRowIndex, ledColumnIndex);
	}

	@Override
	void onMatrixIterationEnded() {
		final Scene currentScene = scenesIterator.getCurrentElement();
		currentScene.onMatrixIterationEnded();
	}

	@Override
	@JsonIgnore
	boolean isExhausted() {
		final Scene currentScene = scenesIterator.getCurrentElement();
		if (currentScene.isExhausted()) {
			scenesIterator.next();
		}
		return scenesIterator.getCurrentElement().isExhausted();
	}

	@PostLoad
	public void setScenesIterator() {
		if (scenes != null && !scenes.isEmpty()) {
			this.scenesIterator = new CyclicIterator<>(scenes);
		}
	}

	public SceneComposite(final List<Scene> scenes) {
		this.scenes = scenes;
		this.scenesIterator = new CyclicIterator<>(scenes);
	}

	public SceneComposite() {
	}
}