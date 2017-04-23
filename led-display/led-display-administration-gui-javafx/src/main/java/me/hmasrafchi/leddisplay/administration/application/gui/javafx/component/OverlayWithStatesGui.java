/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx.component;

import static java.util.Arrays.asList;

import java.util.List;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import me.hmasrafchi.leddisplay.administration.model.view.LedStateView;
import me.hmasrafchi.leddisplay.administration.model.view.RgbColorView;

/**
 * @author michelin
 *
 */
public final class OverlayWithStatesGui extends VBox {
	public OverlayWithStatesGui(final List<List<LedStateView>> states, final RgbColorView onColor,
			final RgbColorView offColor) {
		final Node ledStateGui = new LedStateComponent(states);
		final Node onColorGui = new RgbColorGui("On color: ", onColor);
		final Node offColorGui = new RgbColorGui("Off color: ", offColor);
		getChildren().addAll(asList(ledStateGui, onColorGui, offColorGui));
	}
}