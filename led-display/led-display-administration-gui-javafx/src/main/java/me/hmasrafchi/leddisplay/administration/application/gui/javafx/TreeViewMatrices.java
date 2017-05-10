/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx;

import java.util.Collection;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import me.hmasrafchi.leddisplay.model.view.MatrixView;

/**
 * @author michelin
 *
 */
final class TreeViewMatrices extends TreeView<TreeItemModel> {
	private TreeItemModel currentlySelectedTreeItemModel;

	TreeViewMatrices(final List<MatrixView> matrices, final BorderPane parent) {
		setPrefWidth(400);

		setCellFactory(c -> new TreeViewCellFactoryMatrices());

		final TreeItemModel matricesTreeItemModel = new MatricesTreeItemModel();
		final TreeItem<TreeItemModel> matricesTreeItem = new TreeItem<>(matricesTreeItemModel);
		matricesTreeItem.setExpanded(true);
		setRoot(matricesTreeItem);

		matrices.forEach(matrix -> {
			final GuiContext matrixGui = new GuiContext(matrix);

			final TreeItemModel matrixTreeItemModel = new MatrixTreeItemModel(matrix.getName(), matrixGui);
			final TreeItem<TreeItemModel> matrixTreeItem = new TreeItem<>(matrixTreeItemModel);

			matrixTreeItem.setExpanded(true);
			matricesTreeItem.getChildren().add(matrixTreeItem);

			final List<List<OverlayGui>> scenesGui = matrixGui.getScenesGui();
			scenesGui.forEach(scene -> {
				final TreeItemModel sceneItemModel = new SceneTreeItemModel(matrixGui, scene);
				final TreeItem<TreeItemModel> sceneTreeItem = new TreeItem<>(sceneItemModel);
				sceneTreeItem.setExpanded(true);
				matrixTreeItem.getChildren().add(sceneTreeItem);

				scene.forEach(overlay -> {
					final TreeItemModel overlayItemModel = new OverlayTreeItemModel(overlay.getClass().getSimpleName(),
							matrixGui, overlay);
					sceneTreeItem.getChildren().add(new TreeItem<>(overlayItemModel));
				});
			});
		});

		getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (oldValue != null) {
				final TreeItemModel previouslySelectedTreeItemModel = oldValue.getValue();
				previouslySelectedTreeItemModel.stopAnimation();
			}

			if (newValue != null) {
				currentlySelectedTreeItemModel = newValue.getValue();
				currentlySelectedTreeItemModel.startAnimation();
			}

			final Collection<Node> guis = currentlySelectedTreeItemModel.getNonNullGuis();
			final Button updateButton = new Button("Update");
			updateButton.setOnAction(event -> {
				currentlySelectedTreeItemModel.onUpdateAction();
			});
			guis.add(updateButton);
			final VBox vBox = new VBox();
			vBox.getChildren().addAll(guis);
			parent.setCenter(vBox);
		});
	}

	public void stopAllAnimations() {
		currentlySelectedTreeItemModel.stopAnimation();
	}
}