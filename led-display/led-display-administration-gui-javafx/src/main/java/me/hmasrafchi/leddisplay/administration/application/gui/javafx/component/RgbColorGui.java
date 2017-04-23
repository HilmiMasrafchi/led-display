/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx.component;

import static javafx.scene.paint.Color.rgb;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import me.hmasrafchi.leddisplay.administration.model.view.RgbColorView;

/**
 * @author michelin
 *
 */
public final class RgbColorGui extends HBox {
	public RgbColorGui(final String labelText, final RgbColorView rgbColor) {
		final Label label = new Label(labelText);

		final ColorPicker colorPicker = new ColorPicker();
		colorPicker.setValue(rgb(rgbColor.getR(), rgbColor.getG(), rgbColor.getB()));
		getChildren().addAll(label, colorPicker);
	}
}