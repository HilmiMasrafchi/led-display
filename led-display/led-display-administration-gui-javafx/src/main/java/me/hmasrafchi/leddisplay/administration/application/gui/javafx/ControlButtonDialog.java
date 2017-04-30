/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx;

import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

/**
 * @author michelin
 *
 */
public final class ControlButtonDialog<T> extends Dialog<T> {
	public ControlButtonDialog(final Node content) {
		final ButtonType buttonTypeOk = new ButtonType("OK", ButtonData.OK_DONE);
		final ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		getDialogPane().getButtonTypes().addAll(buttonTypeOk, buttonTypeCancel);
		getDialogPane().setContent(content);
		setResizable(true);
	}
}