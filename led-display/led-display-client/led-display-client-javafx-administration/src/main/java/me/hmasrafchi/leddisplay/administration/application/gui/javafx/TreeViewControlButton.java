/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx;

import java.io.InputStream;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author michelin
 *
 */
final class TreeViewControlButton extends ImageView {
	TreeViewControlButton(final TreeItemModel treeItemModel, final TreeViewControlButtonIcons controlButtonIcon) {
		final InputStream resourceAsStream = getClass().getResourceAsStream(controlButtonIcon.getIconPath());
		final Image image = new Image(resourceAsStream);
		setImage(image);

		setOnMouseClicked(event -> {
			switch (controlButtonIcon) {
			case PLUS_SIGN:
				treeItemModel.onPlusSignAction();
				break;
			case MINUS_SIGN:
				treeItemModel.onMinusSignAction();
				break;
			default:
				throw new RuntimeException(
						"no corresponding handler method for control button: " + controlButtonIcon.name());
			}
		});
	}
}