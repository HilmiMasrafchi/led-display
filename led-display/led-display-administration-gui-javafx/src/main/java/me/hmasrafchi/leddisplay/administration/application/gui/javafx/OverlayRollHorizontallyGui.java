/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx;

import java.util.Arrays;
import java.util.List;

import javafx.scene.layout.VBox;
import me.hmasrafchi.leddisplay.administration.application.gui.javafx.component.OverlayWithStatesGui;
import me.hmasrafchi.leddisplay.administration.application.gui.javafx.component.TextFieldWithLabel;
import me.hmasrafchi.leddisplay.model.view.LedStateView;
import me.hmasrafchi.leddisplay.model.view.OverlayRollHorizontallyView;
import me.hmasrafchi.leddisplay.model.view.OverlayView;
import me.hmasrafchi.leddisplay.model.view.RgbColorView;

/**
 * @author michelin
 *
 */
final class OverlayRollHorizontallyGui extends VBox implements OverlayGui {
	private final OverlayWithStatesGui overlayWithStatesGui;
	private final TextFieldWithLabel beginIndexMarkTextField;
	private final TextFieldWithLabel yPositionTextField;

	OverlayRollHorizontallyGui(final OverlayRollHorizontallyView overlayRollHorizontallyView) {
		this.overlayWithStatesGui = new OverlayWithStatesGui(overlayRollHorizontallyView.getStates(),
				overlayRollHorizontallyView.getOnColor(), overlayRollHorizontallyView.getOffColor());

		final int beginIndexMark = overlayRollHorizontallyView.getBeginIndexMark();
		beginIndexMarkTextField = new TextFieldWithLabel("Begin index mark: ", String.valueOf(beginIndexMark));
		final int yposition = overlayRollHorizontallyView.getYposition();
		yPositionTextField = new TextFieldWithLabel("Y position: ", String.valueOf(yposition));

		getChildren().addAll(Arrays.asList(overlayWithStatesGui, beginIndexMarkTextField, yPositionTextField));
	}

	@Override
	public OverlayView getOverlayModel() {
		final List<List<LedStateView>> ledStates = overlayWithStatesGui.getLedStatesModel();
		final RgbColorView onColor = overlayWithStatesGui.getOnColorModel();
		final RgbColorView offColor = overlayWithStatesGui.getOffColorModel();
		final int beginIndexMark = Integer.valueOf(beginIndexMarkTextField.getText());
		final int yposition = Integer.valueOf(yPositionTextField.getText());

		return new OverlayRollHorizontallyView(ledStates, onColor, offColor, beginIndexMark, yposition);
	}

	@Override
	public String toString() {
		return "OverlayRollHorizontally";
	}
}