/**
 * 
 */
package me.hmasrafchi.leddisplay.jfx.gui;

import javafx.geometry.Insets;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import me.hmasrafchi.leddisplay.api.Led.RgbColor;
import me.hmasrafchi.leddisplay.framework.scene.overlay.Overlay;
import me.hmasrafchi.leddisplay.framework.scene.overlay.OverlayRollHorizontal;
import me.hmasrafchi.leddisplay.jfx.ColorUtils;

/**
 * @author michelin
 *
 */
final class OverlayRollHorizontalGUI extends VBox implements Model<Overlay> {
	private final OverlayBaseGUI overlayBaseGui;
	private final ColorPicker colorPicker;
	private final TextField yPositionTextField;
	private final Label matrixWidth;

	OverlayRollHorizontalGUI(final OverlayRollHorizontal overlayRollHorizontal) {
		this.overlayBaseGui = new OverlayBaseGUI(overlayRollHorizontal.getStates());

		final RgbColor color = overlayRollHorizontal.getColor();
		final Color jfxColor = ColorUtils.toJavaFxColor(color);
		this.colorPicker = new ColorPicker(jfxColor);

		this.yPositionTextField = new TextField();
		this.yPositionTextField.setText(String.valueOf(overlayRollHorizontal.getYPosition()));
		this.yPositionTextField.setPromptText("Y Position");
		this.yPositionTextField.setPrefColumnCount(3);

		this.matrixWidth = new Label(String.valueOf(overlayRollHorizontal.getMatrixWidth()));

		final HBox hbox = new HBox(colorPicker, yPositionTextField, matrixWidth);
		hbox.setSpacing(10);
		hbox.setPadding(new Insets(10));

		getChildren().addAll(hbox, overlayBaseGui);
	}

	@Override
	public Overlay getModel() {
		return new OverlayRollHorizontal(overlayBaseGui.getStates(), ColorUtils.toLedRgbColor(colorPicker.getValue()),
				Integer.parseInt(yPositionTextField.getText()), Integer.valueOf(matrixWidth.getText()));
	}
}