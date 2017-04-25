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
	private final TextField textField;

	public TextFieldWithLabel(final String labelText, final String textFieldValue) {
		this(labelText, textFieldValue, false);
	}

	public TextFieldWithLabel(final String labelText, final String textFieldValue, final boolean disabled) {
		final Label label = new Label(labelText);
		this.textField = new TextField(textFieldValue);
		this.textField.setDisable(disabled);
		getChildren().addAll(label, textField);
	}

	public String getText() {
		return textField.getText();
	}
}