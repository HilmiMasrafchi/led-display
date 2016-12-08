/**
 * 
 */
package me.hmasrafchi.leddisplay.framework.scene.overlay;

import java.util.List;

import lombok.Getter;
import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.framework.scene.Scene;
import me.hmasrafchi.leddisplay.framework.scene.overlay.Overlay.State;

/**
 * @author michelin
 *
 */
public final class OverlayedScene extends Scene {
	@Getter
	private List<Overlay> overlays;

	public OverlayedScene(final List<Overlay> overlays) {
		this.overlays = overlays;
	}

	@Override
	public boolean hasNextFrame() {
		for (final Overlay overlay : overlays) {
			if (overlay.isEndReached()) {
				return true;
			}
		}

		return false;
	}

	@Override
	protected void changeLed(final Led led, final int ledColumnIndex, final int ledRowIndex) {
		Overlay winnerOverlay = null;
		for (final Overlay currentOverlay : overlays) {
			final State currentState = currentOverlay.getStateAt(ledColumnIndex, ledRowIndex);
			if (!currentState.equals(Overlay.State.TRANSPARENT)) {
				winnerOverlay = currentOverlay;
			}
		}

		if (winnerOverlay != null) {
			winnerOverlay.changeLed(led, ledColumnIndex, ledRowIndex);
		} else {
			led.reset();
		}
	}

	@Override
	protected void matrixIterationEnded() {
		for (final Overlay overlay : overlays) {
			overlay.matrixIterationEnded();
		}
	}

	@Override
	protected void resetInternalState() {
		for (final Overlay overlay : overlays) {
			overlay.reset();
		}
	}

	@Override
	public String toString() {
		return "OverlayedScene";
	}
}