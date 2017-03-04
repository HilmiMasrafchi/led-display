/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import static me.hmasrafchi.leddisplay.api.LedState.TRANSPARENT;

import java.util.List;

import lombok.RequiredArgsConstructor;
import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.api.LedState;
import me.hmasrafchi.leddisplay.api.Scene;

/**
 * @author michelin
 *
 */
@RequiredArgsConstructor
public final class SceneOverlayed implements Scene {
	private final List<Overlay> overlays;

	@Override
	public Led onLedVisited(final Led led, final int ledRowIndex, final int ledColumnIndex) {
		Overlay winnerOverlay = null;
		for (final Overlay currentOverlay : overlays) {
			final LedState currentState = currentOverlay.getStateAt(ledRowIndex, ledColumnIndex);
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
		boolean allMatch = overlays.stream().allMatch(Overlay::isExhausted);
		return allMatch;
	}

	@Override
	public void onMatrixIterationEnded() {
		overlays.stream().forEach(Overlay::onMatrixIterationEnded);
	}
}