/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import static me.hmasrafchi.leddisplay.api.LedState.TRANSPARENT;

import java.util.Collection;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.api.LedState;

/**
 * @author michelin
 *
 */
@RequiredArgsConstructor
public final class SceneOverlayed extends Scene {
	@Getter
	private final Collection<? extends Overlay> overlays;

	@Override
	Led onLedVisited(final int ledRowIndex, final int ledColumnIndex) {
		Overlay winnerOverlay = null;
		for (final Overlay currentOverlay : overlays) {
			final LedState currentState = currentOverlay.getStateAt(ledRowIndex, ledColumnIndex);
			if (!currentState.equals(TRANSPARENT)) {
				winnerOverlay = currentOverlay;
			}
		}

		if (winnerOverlay != null) {
			return winnerOverlay.onLedVisited(ledRowIndex, ledColumnIndex);
		} else {
			return new Led();
		}
	}

	@Override
	boolean isExhausted() {
		boolean allMatch = overlays.stream().allMatch(Overlay::isExhausted);
		return allMatch;
	}

	@Override
	void onMatrixIterationEnded() {
		overlays.stream().forEach(Overlay::onMatrixIterationEnded);
	}
}