/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx;

import java.util.Arrays;

import javafx.scene.layout.VBox;
import me.hmasrafchi.leddisplay.administration.application.gui.javafx.component.OverlayWithStatesGui;
import me.hmasrafchi.leddisplay.administration.application.gui.javafx.component.TextFieldWithLabel;
import me.hmasrafchi.leddisplay.administration.model.view.OverlayStationaryView;

/**
 * @author michelin
 *
 */
public final class OverlayStationaryGui extends VBox implements OverlayGui {
	public OverlayStationaryGui(final OverlayStationaryView overlayStationaryView) {
		final OverlayWithStatesGui overlayWithStatesGui = new OverlayWithStatesGui(overlayStationaryView.getStates(),
				overlayStationaryView.getOnColor(), overlayStationaryView.getOffColor());

		final int duration = overlayStationaryView.getDuration();
		final TextFieldWithLabel textField = new TextFieldWithLabel("duration", String.valueOf(duration));

		getChildren().addAll(Arrays.asList(overlayWithStatesGui, textField));
	}
}