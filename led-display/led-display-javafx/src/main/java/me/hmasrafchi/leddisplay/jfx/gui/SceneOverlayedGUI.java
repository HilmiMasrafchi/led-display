/**
 * 
 */
package me.hmasrafchi.leddisplay.jfx.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import me.hmasrafchi.leddisplay.model.Scene;
import me.hmasrafchi.leddisplay.model.SceneOverlayed;
import me.hmasrafchi.leddisplay.model.overlay.Overlay;
import me.hmasrafchi.leddisplay.model.overlay.OverlayRollHorizontal;
import me.hmasrafchi.leddisplay.model.overlay.OverlayStationary;

/**
 * @author michelin
 *
 */
public final class SceneOverlayedGUI extends Accordion implements Model<me.hmasrafchi.leddisplay.model.Scene> {
	private final Collection<Model<Overlay>> overlayModels = new ArrayList<>();

	public SceneOverlayedGUI(final SceneOverlayed overlayedScene) {
		final Collection<Overlay> overlays = overlayedScene.getOverlays();
		for (final Overlay currentOverlay : overlays) {
			if (currentOverlay instanceof OverlayStationary) {
				final OverlayStationary overlayStationary = (OverlayStationary) currentOverlay;
				final OverlayStationaryGUI overlayStationaryGui = new OverlayStationaryGUI(overlayStationary);
				final String title = String.valueOf(currentOverlay.getClass().getSimpleName());
				final TitledPane titledPane = new TitledPane(title, overlayStationaryGui);
				getPanes().add(titledPane);
				overlayModels.add(overlayStationaryGui);
			}

			if (currentOverlay instanceof OverlayRollHorizontal) {
				final OverlayRollHorizontal overlayRollHorizontal = (OverlayRollHorizontal) currentOverlay;
				final OverlayRollHorizontalGUI overlayRollHorizontalGui = new OverlayRollHorizontalGUI(
						overlayRollHorizontal);
				final String title = String.valueOf(currentOverlay.getClass().getSimpleName());
				final TitledPane titledPane = new TitledPane(title, overlayRollHorizontalGui);
				getPanes().add(titledPane);
				overlayModels.add(overlayRollHorizontalGui);
			}
		}
	}

	@Override
	public Scene getModel() {
		return new SceneOverlayed(overlayModels.stream().map(model -> model.getModel()).collect(Collectors.toList()));
	}
}