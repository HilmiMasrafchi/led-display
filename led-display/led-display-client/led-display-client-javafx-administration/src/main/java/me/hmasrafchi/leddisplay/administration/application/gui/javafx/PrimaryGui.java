/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx;

import static java.util.Collections.emptyList;

import java.util.Collection;
import java.util.List;

import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import me.hmasrafchi.leddisplay.administration.application.RestClient;
import me.hmasrafchi.leddisplay.model.view.MatrixView;

/**
 * @author michelin
 *
 */
public final class PrimaryGui extends StackPane {
	private final ProgressBarPane progressBarPane = new ProgressBarPane();

	private PrimaryPane currentPrimaryPane = new PrimaryPane();

	public PrimaryGui() {
		getChildren().addAll(currentPrimaryPane);
		showProgressBar();
		refreshGui();
	}

	public void refreshGui() {
		currentPrimaryPane.stopAnimations();
		RestClient.getAllMatrices(new InvocationCallback<Response>() {
			@Override
			public void completed(final Response response) {
				final int statusCode = response.getStatus();
				final Status status = Response.Status.fromStatusCode(statusCode);
				switch (status) {
				case OK:
					final List<MatrixView> matrices = response.readEntity(new GenericType<List<MatrixView>>() {
					});

					currentPrimaryPane = new PrimaryPane(matrices);
					hideProgressBar();
					Platform.runLater(() -> {
						getChildren().clear();
						getChildren().add(currentPrimaryPane);
					});
					break;
				case NOT_FOUND:
					Platform.runLater(() -> {
						final Alert alertInfo = new Alert(AlertType.INFORMATION);
						alertInfo.setHeaderText(null);
						alertInfo.setContentText("There aren't any matrices created");
						alertInfo.showAndWait();
						getChildren().clear();
						currentPrimaryPane = new PrimaryPane();
						getChildren().add(currentPrimaryPane);
					});
					break;
				default:
					RestClient.close();
					Platform.runLater(() -> {
						final Alert alertError = new Alert(AlertType.ERROR);
						alertError.setHeaderText(null);
						alertError.setContentText("Unexpected error");
						alertError.showAndWait();
						Platform.exit();
						System.exit(0);
					});
				}
			}

			@Override
			public void failed(final Throwable throwable) {
				RestClient.close();
				Platform.runLater(() -> {
					final Alert alertError = new Alert(AlertType.ERROR);
					alertError.setHeaderText(null);
					alertError.setContentText("Unexpected error");
					alertError.showAndWait();
					Platform.exit();
					System.exit(0);
				});
			}
		});
	}

	public void showProgressBar() {
		Platform.runLater(() -> {
			currentPrimaryPane.setDisable(true);

			final ObservableList<Node> children = getChildren();
			final Node topNode = children.get(children.size() - 1);
			if (!(topNode instanceof ProgressBarPane)) {
				children.add(progressBarPane);
			}
		});
	}

	public void hideProgressBar() {
		Platform.runLater(() -> {
			currentPrimaryPane.setDisable(false);

			final ObservableList<Node> children = getChildren();
			final Node topNode = children.get(children.size() - 1);
			children.remove(topNode);
		});
	}
}

class ProgressBarPane extends VBox {
	ProgressBarPane() {
		setAlignment(Pos.CENTER);

		final ProgressIndicator progressIndicator = new ProgressIndicator();
		getChildren().add(progressIndicator);
	}
}

class PrimaryPane extends BorderPane {
	private final TreeViewMatrices matricesTreeView;

	public PrimaryPane() {
		this(emptyList());
	}

	public PrimaryPane(final List<MatrixView> matrices) {
		this.matricesTreeView = new TreeViewMatrices(matrices);
		setLeft(matricesTreeView);
	}

	void stopAnimations() {
		matricesTreeView.stopAllAnimations();
	}

	class TreeViewMatrices extends TreeView<TreeItemModel> {
		private TreeItemModel currentlySelectedTreeItemModel;

		TreeViewMatrices(final List<MatrixView> matrices) {
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
						final TreeItemModel overlayItemModel = new OverlayTreeItemModel(
								overlay.getClass().getSimpleName(), matrixGui, overlay);
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
				final VBox vBox = new VBox();
				vBox.getChildren().addAll(guis);
				setCenter(vBox);
			});
		}

		public void stopAllAnimations() {
			if (currentlySelectedTreeItemModel != null) {
				currentlySelectedTreeItemModel.stopAnimation();
			}
		}
	}
}