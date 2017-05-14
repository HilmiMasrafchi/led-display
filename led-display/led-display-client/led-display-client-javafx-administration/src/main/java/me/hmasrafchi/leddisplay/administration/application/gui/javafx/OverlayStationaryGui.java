/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx;

import java.util.Arrays;
import java.util.List;

import javafx.scene.layout.VBox;
import me.hmasrafchi.leddisplay.model.view.LedStateView;
import me.hmasrafchi.leddisplay.model.view.OverlayStationaryView;
import me.hmasrafchi.leddisplay.model.view.OverlayView;
import me.hmasrafchi.leddisplay.model.view.RgbColorView;

/**
 * @author michelin
 *
 */
final class OverlayStationaryGui extends VBox implements OverlayGui {
	// TODO: move this to overlaygui
	private final OverlayStatesGui overlayWithStatesGui;
	private TextFieldWithLabel durationTextField;

	OverlayStationaryGui(final OverlayStationaryView overlayStationaryView) {
		this.overlayWithStatesGui = new OverlayStatesGui(overlayStationaryView.getStates(),
				overlayStationaryView.getOnColor(), overlayStationaryView.getOffColor());

		final int duration = overlayStationaryView.getDuration();
		this.durationTextField = new TextFieldWithLabel("duration", String.valueOf(duration));

		getChildren().addAll(Arrays.asList(overlayWithStatesGui, durationTextField));
	}

	@Override
	public OverlayView getOverlayModel() {
		final List<List<LedStateView>> ledStatesModel = overlayWithStatesGui.getLedStatesModel();
		final RgbColorView onColorModel = overlayWithStatesGui.getOnColorModel();
		final RgbColorView offColorModel = overlayWithStatesGui.getOffColorModel();
		final Integer duration = Integer.valueOf(durationTextField.getText());

		return new OverlayStationaryView(ledStatesModel, onColorModel, offColorModel, duration);
	}

	@Override
	public String toString() {
		return "OverlayStationary";
	}
}