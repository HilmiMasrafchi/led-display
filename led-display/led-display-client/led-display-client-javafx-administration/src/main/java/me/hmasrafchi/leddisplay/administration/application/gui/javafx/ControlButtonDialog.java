/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx;

import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.util.Callback;

/**
 * @author michelin
 *
 */
final class ControlButtonDialog<T> extends Dialog<T> {
	ControlButtonDialog(final Node content, final Callback<ButtonType, T> callback) {
		final ButtonType buttonTypeOk = new ButtonType("OK", ButtonData.OK_DONE);
		final ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		getDialogPane().getButtonTypes().addAll(buttonTypeOk, buttonTypeCancel);

		setResizable(true);
		setResultConverter(callback);

		getDialogPane().setContent(content);
	}
}