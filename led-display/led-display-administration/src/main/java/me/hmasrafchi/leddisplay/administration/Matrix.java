/**
 * 
 */
package me.hmasrafchi.leddisplay.administration;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

/**
 * @author michelin
 *
 */
@Entity
@Getter
class Matrix {
	@Id
	@GeneratedValue
	private Integer id;

	private int rowCount;
	private int columnCount;

	@Setter
	@OneToOne(cascade = CascadeType.ALL)
	private SceneComposite scene;

	public CompiledFrames getCompiledFrames() {
		final CompiledFrames compiledFrames = new CompiledFrames();
		final Frame currentFrame = new Frame(Led::new, rowCount, columnCount);
		while (!scene.does()) {
			final Frame nextFrame = currentFrame.map(scene::onLedVisited);
			scene.onMatrixIterationEnded();
			compiledFrames.addFrame(nextFrame);
		}

		return compiledFrames;
	}
}