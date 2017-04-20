/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.model.domain;

import static me.hmasrafchi.leddisplay.administration.model.domain.Led.State.TRANSPARENT;

import java.util.Collection;
import java.util.Collections;

/**
 * @author michelin
 *
 */
public final class SceneOverlayed extends Scene {
	private Collection<Overlay> overlays;

	public SceneOverlayed(final Collection<? extends Overlay> overlays2) {
		this.overlays = Collections.unmodifiableCollection(overlays2);
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
	public boolean isExhausted() {
		return overlays.stream().allMatch(Overlay::isExhausted);
	}

	@Override
	public void onMatrixIterationEnded() {
		overlays.stream().forEach(Overlay::onMatrixIterationEnded);
	}
}