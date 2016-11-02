/**
 * 
 */
package me.hmasrafchi.leddisplay.jfx.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import me.hmasrafchi.leddisplay.framework.scene.Scene;
import me.hmasrafchi.leddisplay.framework.scene.overlay.Overlay;
import me.hmasrafchi.leddisplay.framework.scene.overlay.OverlayRollHorizontal;
import me.hmasrafchi.leddisplay.framework.scene.overlay.OverlayStationary;
import me.hmasrafchi.leddisplay.framework.scene.overlay.OverlayedScene;

/**
 * @author michelin
 *
 */
public final class SceneOverlayedGUI extends Accordion implements Model<Scene> {
	private final Collection<Model<Overlay>> overlayModels = new ArrayList<>();

	public SceneOverlayedGUI(final OverlayedScene overlayedScene) {
		final List<Overlay> overlays = overlayedScene.getOverlays();
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
		return new OverlayedScene(overlayModels.stream().map(model -> model.getModel()).collect(Collectors.toList()));
	}
}