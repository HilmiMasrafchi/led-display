/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx;

import java.util.Arrays;

import javafx.scene.layout.VBox;
import me.hmasrafchi.leddisplay.administration.application.gui.javafx.component.OverlayWithStatesGui;
import me.hmasrafchi.leddisplay.administration.model.view.OverlayRollHorizontallyView;

/**
 * @author michelin
 *
 */
public final class OverlayRollHorizontallyGui extends VBox implements OverlayGui {
	public OverlayRollHorizontallyGui(final OverlayRollHorizontallyView overlayRollHorizontallyView) {
		final OverlayWithStatesGui overlayWithStatesGui = new OverlayWithStatesGui(
				overlayRollHorizontallyView.getStates(), overlayRollHorizontallyView.getOnColor(),
				overlayRollHorizontallyView.getOffColor());
		getChildren().addAll(Arrays.asList(overlayWithStatesGui));
	}
}