/**
 * 
 */
package me.hmasrafchi.leddisplay.model.scene;

import java.util.Collection;
import java.util.Collections;

import lombok.Getter;
import me.hmasrafchi.leddisplay.model.Led;
import me.hmasrafchi.leddisplay.model.scene.overlay.Overlay;
import me.hmasrafchi.leddisplay.model.scene.overlay.Overlay.State;

/**
 * @author michelin
 *
 */
public final class OverlayedScene extends AbstractScene {
	@Getter
	private Collection<Overlay> overlays;

	public OverlayedScene(final Collection<? extends Overlay> overlays) {
		this.overlays = Collections.unmodifiableCollection(overlays);
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
	protected void ledIterationEnded() {
		for (final Overlay overlay : overlays) {
			overlay.matrixIterationEnded();
		}
	}

	@Override
	protected void resetSceneState() {
		for (final Overlay overlay : overlays) {
			overlay.reset();
		}
	}

	@Override
	public String toString() {
		return "OverlayedScene";
	}
}