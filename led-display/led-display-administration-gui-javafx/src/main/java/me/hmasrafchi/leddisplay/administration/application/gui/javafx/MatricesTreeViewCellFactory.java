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
public final class MatricesTreeViewCellFactory extends TreeCell<TreeItemModel> {
	@Override
	protected void updateItem(final TreeItemModel item, final boolean empty) {
		super.updateItem(item, empty);

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

			// plusSignImageView.setOnMouseClicked(event -> {
			// if (item instanceof MatricesTreeItemModel) {
			// final Dialog<CreateMatrixCommand> dialog = new Dialog<>();
			// MatrixView matrix = new MatrixView(5, 20);
			// final MatrixInfoGui matrixInfoGui = new MatrixInfoGui(matrix);
			// dialog.getDialogPane().setContent(matrixInfoGui);
			// ButtonType buttonType Ok = new ButtonType("Okay",
			// ButtonData.OK_DONE);
			// dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
			// dialog.setResultConverter(buttonType -> {
			// return new CreateMatrixCommand(matrixInfoGui.getMatrixName(),
			// matrixInfoGui.getMatrixRowCount(),
			// matrixInfoGui.getMatrixColumnCount(), new ArrayList<>());
			// });
			//
			// Optional<CreateMatrixCommand> result = dialog.showAndWait();
			// result.ifPresent(createMatrixCommand -> {
			// final Client jaxRsClient = ClientBuilder.newClient();
			// final Response postMatrixResponse = jaxRsClient
			// .target("http://localhost:8080/led-display-administration").path("matrices")
			// .request(APPLICATION_JSON).post(Entity.json(createMatrixCommand));
			// final int status = postMatrixResponse.getStatus();
			//
			// final Response getAllMatricesResponse = jaxRsClient
			// .target("http://localhost:8080/led-display-administration").path("matrices")
			// .request(APPLICATION_JSON).get();
			// final List<MatrixView> matricesUpdated = getAllMatricesResponse
			// .readEntity(new GenericType<List<MatrixView>>() {
			// });
			// });
			// }
			// });

			setGraphic(borderPane);
			setText(null);
		}
	}
}