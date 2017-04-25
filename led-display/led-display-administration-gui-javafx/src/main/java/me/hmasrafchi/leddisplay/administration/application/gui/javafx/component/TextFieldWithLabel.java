/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx.component;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * @author michelin
 *
 */
public final class TextFieldWithLabel extends HBox {
	public TextFieldWithLabel(final String labelText, final String textFieldValue) {
		final Label durationLabel = new Label(labelText);
		final TextField durationTextField = new TextField(textFieldValue);
		getChildren().addAll(durationLabel, durationTextField);
	}
}