/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import java.util.Collection;

import lombok.RequiredArgsConstructor;
import me.hmasrafchi.leddisplay.model.api.Led;
import me.hmasrafchi.leddisplay.model.overlay.Overlay;
import me.hmasrafchi.leddisplay.model.overlay.Overlay.State;

/**
 * @author michelin
 *
 */
@RequiredArgsConstructor
public final class SceneOverlayed implements Scene {
	private final Collection<Overlay> overlays;

	@Override
	public void onLedVisited(final Led led, final int currentLedColumnIndex, final int currentLedRowIndex) {
		Overlay winnerOverlay = null;
		for (final Overlay currentOverlay : overlays) {
			final State currentState = currentOverlay.getStateAt(currentLedColumnIndex, currentLedRowIndex);
			if (!currentState.equals(Overlay.State.TRANSPARENT)) {
				winnerOverlay = currentOverlay;
			}
		}

		if (winnerOverlay != null) {
			winnerOverlay.onLedVisited(led, currentLedColumnIndex, currentLedRowIndex);
		} else {
			led.reset();
		}
	}

	@Override
	public boolean isExhausted() {
		return overlays.stream().filter(Overlay::isExhausted).findAny().isPresent();
	}

	@Override
	public void onMatrixReset() {
		overlays.stream().forEach(Overlay::onMatrixReset);
	}

	@Override
	public void onMatrixIterationEnded() {
		overlays.stream().forEach(Overlay::onMatrixIterationEnded);
	}
}