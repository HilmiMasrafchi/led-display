/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx.component;

import static javafx.scene.paint.Color.rgb;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import me.hmasrafchi.leddisplay.administration.model.view.RgbColorView;

/**
 * @author michelin
 *
 */
public final class RgbColorGui extends HBox {
	private ColorPicker colorPicker;

	public RgbColorGui(final String labelText, final RgbColorView rgbColor) {
		final Label label = new Label(labelText);

		this.colorPicker = new ColorPicker();
		this.colorPicker.setValue(rgb(rgbColor.getR(), rgbColor.getG(), rgbColor.getB()));
		getChildren().addAll(label, colorPicker);
	}

	public RgbColorView getColorModel() {
		final Color color = this.colorPicker.getValue();
		return new RgbColorView((int) (color.getRed() * 255), (int) (color.getGreen() * 255),
				(int) (color.getBlue() * 255));
	}
}