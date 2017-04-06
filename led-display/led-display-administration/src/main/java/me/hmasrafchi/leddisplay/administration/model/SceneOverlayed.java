/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.model;

import static me.hmasrafchi.leddisplay.administration.model.Led.State.TRANSPARENT;

import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author michelin
 *
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class SceneOverlayed extends Scene {
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn
	private List<Overlay> overlays;

	public SceneOverlayed() {

	}

	public SceneOverlayed(final List<? extends Overlay> overlays) {
		this.overlays = Collections.unmodifiableList(overlays);
	}

	@Override
	public Led onLedVisited(final Led led, final int ledRowIndex, final int ledColumnIndex) {
		Overlay winnerOverlay = null;
		for (final Overlay currentOverlay : overlays) {
			final Led.State currentState = currentOverlay.getStateAt(ledRowIndex, ledColumnIndex);
			if (!currentState.equals(TRANSPARENT)) {
				winnerOverlay = currentOverlay;
			}
		}

		if (winnerOverlay != null) {
			return winnerOverlay.onLedVisited(led, ledRowIndex, ledColumnIndex);
		} else {
			return new Led();
		}
	}

	@Override
	@JsonIgnore
	public boolean isExhausted() {
		return overlays.stream().allMatch(Overlay::isExhausted);
	}

	@Override
	public void onMatrixIterationEnded() {
		overlays.stream().forEach(Overlay::onMatrixIterationEnded);
	}
}