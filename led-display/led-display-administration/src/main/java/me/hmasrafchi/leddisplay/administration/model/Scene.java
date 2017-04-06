/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import me.hmasrafchi.leddisplay.administration.CompiledFrames;
import me.hmasrafchi.leddisplay.administration.Frame;

/**
 * @author michelin
 *
 */

@Getter

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, visible = true)
@JsonSubTypes({ @JsonSubTypes.Type(value = OverlayStationary.class, name = "stationary"),
		@JsonSubTypes.Type(value = OverlayRollHorizontally.class, name = "roll"),
		@JsonSubTypes.Type(value = SceneComposite.class, name = "composite") })

@Entity
@Inheritance
public abstract class Scene {
	@Id
	@GeneratedValue
	private Integer id;

	abstract Led onLedVisited(Led led, int ledRowIndex, int ledColumnIndex);

	abstract void onMatrixIterationEnded();

	abstract boolean isExhausted();

	public CompiledFrames getCompiledFrames(final int rowCount, final int columnCount) {
		final CompiledFrames compiledFrames = new CompiledFrames();
		final Frame currentFrame = new Frame(Led::new, rowCount, columnCount);
		while (!isExhausted()) {
			final Frame nextFrame = currentFrame.map(this::onLedVisited);
			onMatrixIterationEnded();
			compiledFrames.addFrame(nextFrame);
		}

		return compiledFrames;
	}
}