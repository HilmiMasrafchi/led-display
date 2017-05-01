/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static me.hmasrafchi.leddisplay.administration.application.gui.javafx.TreeViewControlButtonIcons.PLUS_SIGN;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Dialog;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
final class MatricesTreeView extends TreeView<TreeItemModel> {
	private TreeItemModel currentlySelectedTreeItemModel;

	MatricesTreeView(final List<MatrixView> matrices, final BorderPane parent) {
		setPrefWidth(400);

		setCellFactory(c -> new MatricesTreeViewCellFactory());

		final TreeItemModel matricesTreeItemModel = new MatricesTreeItemModel();
		final TreeItem<TreeItemModel> matricesTreeItem = new TreeItem<>(matricesTreeItemModel);
		matricesTreeItem.setExpanded(true);
		setRoot(matricesTreeItem);

		matrices.forEach(matrix -> {
			final MatrixGui matrixGui = new MatrixGui(matrix);

			final MatrixInfoGui matrixInfoGui = matrixGui.getMatrixInfoGui();
			final CompiledFramesGui compiledFramesGui = matrixGui.getCompiledFramesGui();

			final TreeItemModel matrixTreeItemModel = new MatrixTreeItemModel(matrix.getName(), matrixGui,
					matrixInfoGui, compiledFramesGui);
			final TreeItem<TreeItemModel> matrixTreeItem = new TreeItem<>(matrixTreeItemModel);

			matrixTreeItem.setExpanded(true);
			matricesTreeItem.getChildren().add(matrixTreeItem);

			final List<List<OverlayGui>> scenesGui = matrixGui.getScenesGui();
			scenesGui.forEach(scene -> {
				final TreeItemModel sceneItemModel = new SceneTreeItemModel(matrixGui, matrixInfoGui);
				final TreeItem<TreeItemModel> sceneTreeItem = new TreeItem<>(sceneItemModel);
				sceneTreeItem.setExpanded(true);
				matrixTreeItem.getChildren().add(sceneTreeItem);

				scene.forEach(overlay -> {
					final TreeItemModel overlayItemModel = new OverlayTreeItemModel(overlay.getClass().getSimpleName(),
							matrixGui, matrixInfoGui, overlay);
					sceneTreeItem.getChildren().add(new TreeItem<>(overlayItemModel));
				});
			});
		});

		getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (oldValue != null) {
				final TreeItemModel previouslySelectedTreeItemModel = oldValue.getValue();
				previouslySelectedTreeItemModel.stopAnimation();
			}

			if(newValue != null) {
				currentlySelectedTreeItemModel = newValue.getValue();
				currentlySelectedTreeItemModel.startAnimation();
			}

			final Collection<Node> guis = currentlySelectedTreeItemModel.getNonNullGuis();
			final VBox vBox = new VBox();
			vBox.getChildren().addAll(guis);
			parent.setCenter(vBox);
		});
	}

	public void stopAllAnimations() {
		currentlySelectedTreeItemModel.stopAnimation();
	}
}

@RequiredArgsConstructor
abstract class TreeItemModel {
	@Getter
	final String label;

	@Override
	public String toString() {
		return label;
	}

	Collection<Node> getNonNullGuis() {
		return getGuis().stream().filter(node -> node != null).collect(Collectors.toList());
	}

	abstract EnumSet<TreeViewControlButtonIcons> getAllowedControlButtonIcons();

	abstract Collection<Node> getGuis();

	void startAnimation() {
	}

	void stopAnimation() {
	}

	void onPlusSignAction() {
		AdministrationApp.showProgressBar();
	}
}

class MatricesTreeItemModel extends TreeItemModel {
	public MatricesTreeItemModel() {
		super("Matrices");
	}

	@Override
	Collection<Node> getGuis() {
		return Collections.emptyList();
	}

	@Override
	EnumSet<TreeViewControlButtonIcons> getAllowedControlButtonIcons() {
		return EnumSet.of(PLUS_SIGN);
	}

	@Override
	void onPlusSignAction() {
		super.onPlusSignAction();

		final MatrixView matrix = new MatrixView("Name", 5, 20);
		final MatrixInfoGui matrixInfoGui = new MatrixInfoGui(matrix);

		final Dialog<CreateMatrixCommand> dialog = new ControlButtonDialog<>(matrixInfoGui);
		dialog.setResultConverter(buttonType -> {
			if (buttonType.getButtonData().equals(ButtonBar.ButtonData.CANCEL_CLOSE)) {
				return null;
			}
			return new CreateMatrixCommand(matrixInfoGui.getMatrixName(), matrixInfoGui.getMatrixRowCount(),
					matrixInfoGui.getMatrixColumnCount(), new ArrayList<>());
		});

		final Optional<CreateMatrixCommand> result = dialog.showAndWait();
		if (result.isPresent()) {
			final CreateMatrixCommand createMatrixCommand = result.get();
			RestClient.createMatrix(createMatrixCommand);
			AdministrationApp.refreshGui();
		} else {
			AdministrationApp.hideProgressBar();
		}
	}
}

class MatrixTreeItemModel extends TreeItemModel {
	private final MatrixGui matrixGui;
	private final MatrixInfoGui matrixInfoGui;
	private final CompiledFramesGui compiledFramesGui;

	public MatrixTreeItemModel(final String label, final MatrixGui matrixGui, final MatrixInfoGui matrixInfoGui,
			final CompiledFramesGui compiledFramesGui) {
		super(label);
		this.matrixGui = matrixGui;
		this.matrixInfoGui = matrixInfoGui;
		this.compiledFramesGui = compiledFramesGui;
	}

	@Override
	void startAnimation() {
		if (compiledFramesGui != null) {
			compiledFramesGui.startAnimation();
		}
	}

	@Override
	void stopAnimation() {
		if (compiledFramesGui != null) {
			compiledFramesGui.stopAnimation();
		}
	}

	@Override
	Collection<Node> getGuis() {
		return Arrays.asList(matrixInfoGui, compiledFramesGui);
	}

	@Override
	EnumSet<TreeViewControlButtonIcons> getAllowedControlButtonIcons() {
		return EnumSet.of(PLUS_SIGN);
	}

	@Override
	void onPlusSignAction() {
		super.onPlusSignAction();

		final List<LedStateView> a = new ArrayList<>();
		a.add(LedStateView.ON);
		final List<List<LedStateView>> asd = new ArrayList<>();
		asd.add(a);
		final OverlayStationaryView overlayStationaryView = new OverlayStationaryView(asd, new RgbColorView(255, 0, 0),
				new RgbColorView(0, 255, 0), 1);
		final OverlayStationaryGui overlayStationaryGui = new OverlayStationaryGui(overlayStationaryView);

		final List<LedStateView> a2 = new ArrayList<>();
		a2.add(LedStateView.ON);
		final List<List<LedStateView>> asd1 = new ArrayList<>();
		asd1.add(a2);
		final OverlayRollHorizontallyView overlayRollHorizontallyView = new OverlayRollHorizontallyView(asd1,
				new RgbColorView(255, 0, 0), new RgbColorView(0, 255, 0), 5, 1);
		final OverlayRollHorizontallyGui overlayRollHorizontallyGui = new OverlayRollHorizontallyGui(
				overlayRollHorizontallyView);

		final Tab tab1 = new Tab("Stationary");
		tab1.setUserData(overlayStationaryGui);
		tab1.setContent(new ScrollPane(overlayStationaryGui));

		final Tab tab2 = new Tab("Roll horizontally");
		tab2.setUserData(overlayRollHorizontallyGui);
		tab2.setContent(new ScrollPane(overlayRollHorizontallyGui));

		final TabPane tabPane = new TabPane(tab1, tab2);

		final Dialog<OverlayView> dialog = new ControlButtonDialog<>(tabPane);
		dialog.getDialogPane().setContent(tabPane);

		dialog.setResultConverter(buttonType -> {
			if (buttonType.getButtonData().equals(ButtonBar.ButtonData.CANCEL_CLOSE)) {
				return null;
			}

			Tab selectedItem = tabPane.getSelectionModel().getSelectedItem();
			return ((OverlayGui) selectedItem.getUserData()).getOverlayModel();
		});

		final Optional<OverlayView> showAndWait = dialog.showAndWait();
		if (showAndWait.isPresent()) {
			final OverlayView overlayView = showAndWait.get();
			final MatrixView selectedMatrix = matrixGui.getMatrixModel();
			selectedMatrix.appendNewSceneAndAppendOverlayToIt(overlayView);
			final Client jaxRsClient = ClientBuilder.newClient();
			final Response postMatrixResponse = jaxRsClient.target("http://localhost:8080/led-display-administration")
					.path("matrices").request(APPLICATION_JSON).put(Entity.json(selectedMatrix));
			AdministrationApp.refreshGui();
		} else {
			AdministrationApp.hideProgressBar();
		}
	}
}

class SceneTreeItemModel extends TreeItemModel {
	private final MatrixGui matrixGui;
	private final MatrixInfoGui matrixInfoGui;

	public SceneTreeItemModel(final MatrixGui matrixGui, final MatrixInfoGui matrixInfoGui) {
		super("<Scene>");
		this.matrixGui = matrixGui;
		this.matrixInfoGui = matrixInfoGui;
	}

	@Override
	Collection<Node> getGuis() {
		return Arrays.asList(matrixInfoGui);
	}

	@Override
	EnumSet<TreeViewControlButtonIcons> getAllowedControlButtonIcons() {
		return EnumSet.noneOf(TreeViewControlButtonIcons.class);
	}
}

class OverlayTreeItemModel extends TreeItemModel {
	private final MatrixGui matrixGui;
	private final MatrixInfoGui matrixInfoGui;
	private final OverlayGui overlayGui;

	public OverlayTreeItemModel(final String label, final MatrixGui matrixGui, final MatrixInfoGui matrixInfoGui,
			final OverlayGui overlayGui) {
		super(label);
		this.matrixGui = matrixGui;
		this.matrixInfoGui = matrixInfoGui;
		this.overlayGui = overlayGui;
	}

	@Override
	Collection<Node> getGuis() {
		return Arrays.asList(matrixInfoGui, (Node) overlayGui);
	}

	@Override
	EnumSet<TreeViewControlButtonIcons> getAllowedControlButtonIcons() {
		return EnumSet.noneOf(TreeViewControlButtonIcons.class);
	}
}