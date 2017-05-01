/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx;

import java.util.EnumSet;

import javafx.scene.control.Label;
import javafx.scene.control.TreeCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * @author michelin
 *
 */
final class TreeViewCellFactoryMatrices extends TreeCell<TreeItemModel> {
	@Override
	protected void updateItem(final TreeItemModel item, final boolean empty) {
		super.updateItem(item, empty);

		// setOnMouseEntered(mouseEvent -> {
		// if (!isEmpty() && !isSelected()) {
		// final Node node = (Node) mouseEvent.getSource();
		// node.setStyle("-fx-background-color: lightblue;");
		// }
		// });
		//
		// setOnMouseExited(mouseEvent -> {
		// if (!isEmpty()) {
		// final Node node = (Node) mouseEvent.getSource();
		// node.setStyle("-fx-background-color: white;");
		// }
		// });

		if (empty || item == null) {
			setGraphic(null);
			setText(null);
		} else {
			final BorderPane borderPane = new BorderPane();

			final Label label = new Label(item.getLabel());
			borderPane.setLeft(label);

			final EnumSet<TreeViewControlButtonIcons> allowedControlButtonIcons = item.getAllowedControlButtonIcons();
			final TreeViewControlButton[] iconNodesArray = allowedControlButtonIcons.stream()
					.map(controlButtonIcon -> new TreeViewControlButton(item, controlButtonIcon))
					.toArray(size -> new TreeViewControlButton[size]);
			final HBox hbox = new HBox(10, iconNodesArray);
			borderPane.setRight(hbox);
			setGraphic(borderPane);
			setText(null);
		}
	}
}