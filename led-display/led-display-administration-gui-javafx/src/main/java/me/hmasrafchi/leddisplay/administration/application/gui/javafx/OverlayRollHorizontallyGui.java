/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx;

import java.util.Arrays;

import javafx.scene.layout.VBox;
import me.hmasrafchi.leddisplay.administration.application.gui.javafx.component.OverlayWithStatesGui;
import me.hmasrafchi.leddisplay.administration.application.gui.javafx.component.TextFieldWithLabel;
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

		final int beginIndexMark = overlayRollHorizontallyView.getBeginIndexMark();
		final TextFieldWithLabel beginIndexMarkTextField = new TextFieldWithLabel("Begin index mark: ",
				String.valueOf(beginIndexMark));
		final int yposition = overlayRollHorizontallyView.getYposition();
		final TextFieldWithLabel yPositionTextField = new TextFieldWithLabel("Y position: ", String.valueOf(yposition));

		getChildren().addAll(Arrays.asList(overlayWithStatesGui, beginIndexMarkTextField, yPositionTextField));
	}
}