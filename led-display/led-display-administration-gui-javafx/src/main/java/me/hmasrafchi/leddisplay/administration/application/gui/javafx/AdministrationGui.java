/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx;

import static java.util.stream.Collectors.toList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import lombok.Value;
import me.hmasrafchi.leddisplay.administration.model.view.CreateMatrixCommand;
import me.hmasrafchi.leddisplay.administration.model.view.LedStateView;
import me.hmasrafchi.leddisplay.administration.model.view.MatrixView;
import me.hmasrafchi.leddisplay.administration.model.view.OverlayRollHorizontallyView;
import me.hmasrafchi.leddisplay.administration.model.view.OverlayStationaryView;
import me.hmasrafchi.leddisplay.administration.model.view.OverlayView;
import me.hmasrafchi.leddisplay.administration.model.view.RgbColorView;

/**
 * @author michelin
 *
 */
public final class AdministrationGui extends BorderPane {
	private final Button addMatrixButton = new Button("Add Matrix");
	private final Button addOverlayStationaryButton = new Button("Add Overlay Stationary");
	private final Button addOverlayRollHorizontallyButton = new Button("Add Overlay Roll Horizontally");
	private final Button updateButton;

	// TODO: think how to update this properly
	private MatricesGui matricesGui;

	private CompiledFramesGui currentlySelectedCompiledFrameGui;
	private int selectedIndex;

	public AdministrationGui(final List<MatrixView> matrices) {
		this.matricesGui = new MatricesGui(matrices);
		setCenter(matricesGui);

		addMatrixButton.setOnAction(event -> {
			final Dialog<CreateMatrixCommand> dialog = new Dialog<>();
			MatrixView matrix = new MatrixView(5, 20);
			final MatrixInfoGui matrixInfoGui = new MatrixInfoGui(matrix);
			dialog.getDialogPane().setContent(matrixInfoGui);
			ButtonType buttonTypeOk = new ButtonType("Okay", ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
			dialog.setResultConverter(buttonType -> {
				return new CreateMatrixCommand(matrixInfoGui.getMatrixName(), matrixInfoGui.getMatrixRowCount(),
						matrixInfoGui.getMatrixColumnCount(), new ArrayList<>());
			});

			Optional<CreateMatrixCommand> result = dialog.showAndWait();
			result.ifPresent(createMatrixCommand -> {
				final Client jaxRsClient = ClientBuilder.newClient();
				final Response postMatrixResponse = jaxRsClient
						.target("http://localhost:8080/led-display-administration").path("matrices")
						.request(APPLICATION_JSON).post(Entity.json(createMatrixCommand));
				final int status = postMatrixResponse.getStatus();

				final Response getAllMatricesResponse = jaxRsClient
						.target("http://localhost:8080/led-display-administration").path("matrices")
						.request(APPLICATION_JSON).get();
				final List<MatrixView> matricesUpdated = getAllMatricesResponse
						.readEntity(new GenericType<List<MatrixView>>() {
						});

				final MatricesGui administrationGuiUpdated = new MatricesGui(matricesUpdated);
				this.matricesGui = administrationGuiUpdated;
				setCenter(administrationGuiUpdated);
			});
		});

		addOverlayStationaryButton.setOnAction(event -> {
			final Dialog<OverlayView> dialog = new Dialog<>();
			dialog.resizableProperty().set(true);

			final List<LedStateView> a = new ArrayList<>();
			a.add(LedStateView.ON);
			final List<List<LedStateView>> asd = new ArrayList<>();
			asd.add(a);
			final OverlayStationaryView overlayStationaryView = new OverlayStationaryView(asd,
					new RgbColorView(255, 0, 0), new RgbColorView(0, 255, 0), 1);
			final OverlayStationaryGui overlayStationaryGui = new OverlayStationaryGui(overlayStationaryView);

			dialog.getDialogPane().setContent(overlayStationaryGui);
			ButtonType buttonTypeOk = new ButtonType("Okay", ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
			dialog.setResultConverter(buttonType -> {
				return overlayStationaryGui.getOverlayModel();
			});

			Optional<OverlayView> result = dialog.showAndWait();
			result.ifPresent(overlayView -> {
				final MatrixView selectedMatrix = matricesGui.getSelectedMatrixModel();
				selectedMatrix.appendNewSceneAndAppendOverlayToIt(overlayView);
				final Client jaxRsClient = ClientBuilder.newClient();
				final Response postMatrixResponse = jaxRsClient
						.target("http://localhost:8080/led-display-administration").path("matrices")
						.request(APPLICATION_JSON).put(Entity.json(selectedMatrix));
				final int status = postMatrixResponse.getStatus();

				final Response getAllMatricesResponse = jaxRsClient
						.target("http://localhost:8080/led-display-administration").path("matrices")
						.request(APPLICATION_JSON).get();
				final List<MatrixView> matricesUpdated = getAllMatricesResponse
						.readEntity(new GenericType<List<MatrixView>>() {
						});

				final MatricesGui administrationGuiUpdated = new MatricesGui(matricesUpdated);
				this.matricesGui = administrationGuiUpdated;
				setCenter(administrationGuiUpdated);
			});
		});

		addOverlayRollHorizontallyButton.setOnAction(event -> {
			final Dialog<OverlayView> dialog = new Dialog<>();
			dialog.resizableProperty().set(true);

			final List<LedStateView> a = new ArrayList<>();
			a.add(LedStateView.ON);
			final List<List<LedStateView>> asd = new ArrayList<>();
			asd.add(a);
			final OverlayRollHorizontallyView overlayRollHorizontallyView = new OverlayRollHorizontallyView(asd,
					new RgbColorView(255, 0, 0), new RgbColorView(0, 255, 0), 5, 1);
			final OverlayRollHorizontallyGui overlayRollHorizontallyGui = new OverlayRollHorizontallyGui(
					overlayRollHorizontallyView);

			dialog.getDialogPane().setContent(overlayRollHorizontallyGui);
			ButtonType buttonTypeOk = new ButtonType("Okay", ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
			dialog.setResultConverter(buttonType -> {
				return overlayRollHorizontallyGui.getOverlayModel();
			});

			Optional<OverlayView> result = dialog.showAndWait();
			result.ifPresent(overlayView -> {
				final MatrixView selectedMatrix = matricesGui.getSelectedMatrixModel();
				selectedMatrix.appendNewSceneAndAppendOverlayToIt(overlayView);
				final Client jaxRsClient = ClientBuilder.newClient();
				final Response postMatrixResponse = jaxRsClient
						.target("http://localhost:8080/led-display-administration").path("matrices")
						.request(APPLICATION_JSON).put(Entity.json(selectedMatrix));
				final int status = postMatrixResponse.getStatus();

				final Response getAllMatricesResponse = jaxRsClient
						.target("http://localhost:8080/led-display-administration").path("matrices")
						.request(APPLICATION_JSON).get();
				final List<MatrixView> matricesUpdated = getAllMatricesResponse
						.readEntity(new GenericType<List<MatrixView>>() {
						});

				final MatricesGui administrationGuiUpdated = new MatricesGui(matricesUpdated);
				this.matricesGui = administrationGuiUpdated;
				setCenter(administrationGuiUpdated);
			});
		});

		this.updateButton = new Button("Update");
		setTop(updateButton);
		this.updateButton.setOnAction(event -> {
			final MatrixView selectedMatrix = matricesGui.getSelectedMatrixModel();
			if (selectedMatrix != null) {
				final Client jaxRsClient = ClientBuilder.newClient();
				final Response putMatrixResponse = jaxRsClient
						.target("http://localhost:8080/led-display-administration").path("matrices")
						.request(APPLICATION_JSON).put(Entity.json(selectedMatrix));
				if (putMatrixResponse.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
					final Response getAllMatricesResponse = jaxRsClient
							.target("http://localhost:8080/led-display-administration").path("matrices")
							.request(APPLICATION_JSON).get();
					final List<MatrixView> matricesUpdated = getAllMatricesResponse
							.readEntity(new GenericType<List<MatrixView>>() {
							});

					final MatricesGui administrationGuiUpdated = new MatricesGui(matricesUpdated);
					// TODO: think how to update this properly
					this.matricesGui = administrationGuiUpdated;
					setCenter(administrationGuiUpdated);
				}
			}

		});
	}

	private class MatricesGui extends BorderPane {
		private final TreeView<TreeItemModel> treeView;

		public MatricesGui(final List<MatrixView> matrices) {
			this.treeView = new TreeView<>();

			final TreeItem<TreeItemModel> root = new TreeItem<>(new TreeItemModel("Matrices", null, null, null, null));
			root.setExpanded(true);
			treeView.setRoot(root);

			matrices.forEach(matrix -> {
				final MatrixGui matrixGui = new MatrixGui(matrix);

				final MatrixInfoGui matrixInfoGui = matrixGui.getMatrixInfoGui();
				final CompiledFramesGui compiledFramesGui = matrixGui.getCompiledFramesGui();
				final TreeItem<TreeItemModel> matrixTreeItem = new TreeItem<>(
						new TreeItemModel(matrix.getName(), matrixGui, matrixInfoGui, null, compiledFramesGui));
				matrixTreeItem.setExpanded(true);
				root.getChildren().add(matrixTreeItem);

				final List<List<OverlayGui>> scenesGui = matrixGui.getScenesGui();
				scenesGui.forEach(scene -> {
					final TreeItem<TreeItemModel> sceneTreeItem = new TreeItem<>(
							new TreeItemModel("Scene", matrixGui, matrixInfoGui, null, null));
					sceneTreeItem.setExpanded(true);
					matrixTreeItem.getChildren().add(sceneTreeItem);

					scene.forEach(overlay -> {
						sceneTreeItem.getChildren()
								.add(new TreeItem<>(new TreeItemModel(overlay.getClass().getSimpleName(), matrixGui,
										matrixInfoGui, (Node) overlay, null)));
					});
				});
			});

			treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
				final TreeItemModel value = newValue.getValue();

				if (currentlySelectedCompiledFrameGui != null) {
					currentlySelectedCompiledFrameGui.stopAnimation();
				}
				currentlySelectedCompiledFrameGui = value.getCompiledFramesGui();
				if (currentlySelectedCompiledFrameGui != null) {
					currentlySelectedCompiledFrameGui.startAnimation();
				}

				final List<Node> collect = Arrays
						.asList(value.getMatrixInfoGui(), value.getOverlayGui(), value.getCompiledFramesGui()).stream()
						.filter(node -> node != null).collect(toList());
				final VBox vBox = new VBox();
				vBox.getChildren().addAll(collect);
				setCenter(vBox);
			});

			final FlowPane buttonPane = new FlowPane(addMatrixButton, addOverlayStationaryButton,
					addOverlayRollHorizontallyButton);
			setLeft(new VBox(buttonPane, treeView));
		}

		public MatrixView getSelectedMatrixModel() {
			final MultipleSelectionModel<TreeItem<TreeItemModel>> selectionModel = treeView.getSelectionModel();
			final TreeItem<TreeItemModel> selectedItem = selectionModel.getSelectedItem();
			final TreeItemModel value = selectedItem.getValue();
			final MatrixGui matrixGui = value.getMatrixGui();
			if (matrixGui == null) {
				return null;
			}

			return matrixGui.getMatrixModel();
		}
	}

	@Value
	private class TreeItemModel {
		private final String label;
		private final MatrixGui matrixGui;
		private final MatrixInfoGui matrixInfoGui;
		private final Node overlayGui;
		private final CompiledFramesGui compiledFramesGui;

		@Override
		public String toString() {
			return label;
		}
	}
}