/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx;

import java.util.Arrays;
import java.util.List;

import javafx.scene.layout.VBox;
import me.hmasrafchi.leddisplay.administration.application.gui.javafx.component.OverlayWithStatesGui;
import me.hmasrafchi.leddisplay.administration.application.gui.javafx.component.TextFieldWithLabel;
import me.hmasrafchi.leddisplay.administration.model.view.LedStateView;
import me.hmasrafchi.leddisplay.administration.model.view.OverlayStationaryView;
import me.hmasrafchi.leddisplay.administration.model.view.OverlayView;
import me.hmasrafchi.leddisplay.administration.model.view.RgbColorView;

/**
 * @author michelin
 *
 */
public final class OverlayStationaryGui extends VBox implements OverlayGui {
	// TODO: move this to overlaygui
	private final OverlayWithStatesGui overlayWithStatesGui;
	private TextFieldWithLabel durationTextField;

	public OverlayStationaryGui(final OverlayStationaryView overlayStationaryView) {
		this.overlayWithStatesGui = new OverlayWithStatesGui(overlayStationaryView.getStates(),
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