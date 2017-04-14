/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import me.hmasrafchi.leddisplay.administration.CompiledFrames;
import me.hmasrafchi.leddisplay.administration.Frame;

/**
 * @author michelin
 *
 */
@Data
@Entity
public class Matrix {
	@Id
	@GeneratedValue
	private int id;

	private int rowCount;
	private int columnCount;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Scene scene;

	public Matrix() {

	}

	public Matrix(final int rowCount, final int columnCount, final Scene scene) {
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		this.scene = scene;
	}

	@JsonIgnore
	public CompiledFrames getCompiledFrames() {
		final CompiledFrames compiledFrames = new CompiledFrames();
		final Frame currentFrame = new Frame(Led::new, rowCount, columnCount);
		while (!scene.isExhausted()) {
			final Frame nextFrame = currentFrame.map(scene::onLedVisited);
			scene.onMatrixIterationEnded();
			compiledFrames.addFrame(nextFrame);
		}

		return compiledFrames;
	}
}