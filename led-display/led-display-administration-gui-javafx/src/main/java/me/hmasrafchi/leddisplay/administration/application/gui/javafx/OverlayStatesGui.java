/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx;

import static java.util.Arrays.asList;

import java.util.List;

import javafx.scene.layout.VBox;
import me.hmasrafchi.leddisplay.model.view.LedStateView;
import me.hmasrafchi.leddisplay.model.view.RgbColorView;

/**
 * @author michelin
 *
 */
final class OverlayStatesGui extends VBox {
	private final LedStateComponent ledStateGui;
	private final RgbColorGui onColorGui;
	private final RgbColorGui offColorGui;

	OverlayStatesGui(final List<List<LedStateView>> states, final RgbColorView onColor, final RgbColorView offColor) {
		this.ledStateGui = new LedStateComponent(states);
		this.onColorGui = new RgbColorGui("On color: ", onColor);
		this.offColorGui = new RgbColorGui("Off color: ", offColor);
		getChildren().addAll(asList(ledStateGui, onColorGui, offColorGui));
	}

	public List<List<LedStateView>> getLedStatesModel() {
		return this.ledStateGui.getLedStatesModel();
	}

	public RgbColorView getOnColorModel() {
		return onColorGui.getColorModel();

	}

	public RgbColorView getOffColorModel() {
		return offColorGui.getColorModel();
	}
}