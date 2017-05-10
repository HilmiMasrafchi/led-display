/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx;

import static java.util.Arrays.asList;
import static java.util.EnumSet.of;
import static me.hmasrafchi.leddisplay.administration.application.gui.javafx.TreeViewControlButtonIcons.MINUS_SIGN;
import static me.hmasrafchi.leddisplay.administration.application.gui.javafx.TreeViewControlButtonIcons.PLUS_SIGN;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.hmasrafchi.leddisplay.administration.application.AdministrationApp;
import me.hmasrafchi.leddisplay.model.view.CreateMatrixCommand;
import me.hmasrafchi.leddisplay.model.view.LedStateView;
import me.hmasrafchi.leddisplay.model.view.MatrixView;
import me.hmasrafchi.leddisplay.model.view.OverlayRollHorizontallyView;
import me.hmasrafchi.leddisplay.model.view.OverlayStationaryView;
import me.hmasrafchi.leddisplay.model.view.OverlayView;
import me.hmasrafchi.leddisplay.model.view.RgbColorView;

/**
 * @author michelin
 *
 */
@RequiredArgsConstructor
abstract class TreeItemModel {
	@Getter
	final String label;

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

	final void onMinusSignAction() {
		AdministrationApp.showProgressBar();

		final Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure want to delete selected item?");
		alert.setHeaderText(null);
		Optional<ButtonType> showAndWait = alert.showAndWait();
		showAndWait.ifPresent(buttonType -> {
			if (buttonType.getButtonData().equals(ButtonBar.ButtonData.CANCEL_CLOSE)) {
				AdministrationApp.hideProgressBar();
			}

			if (buttonType.getButtonData().equals(ButtonBar.ButtonData.OK_DONE)) {
				onMinusSignActionOK();
			}
		});
	}

	void onMinusSignActionOK() {

	}

	Dialog<OverlayView> getOverlaysDialog(final Integer matrixRowCount, final Integer matrixColumnCount) {
		final TabPane tabPane = getOverlaysTabPane(matrixRowCount, matrixColumnCount);
		final Dialog<OverlayView> dialog = new ControlButtonDialog<>(tabPane, buttonType -> {
			if (buttonType.getButtonData().equals(ButtonBar.ButtonData.CANCEL_CLOSE)) {
				return null;
			}

			final Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
			return ((OverlayGui) selectedTab.getUserData()).getOverlayModel();
		});
		return dialog;
	}

	TabPane getOverlaysTabPane(final Integer matrixRowCount, final Integer matrixColumnCount) {
		final OverlayStationaryGui overlayStationaryGui = getOverlayStationaryGui(matrixRowCount, matrixColumnCount);
		final OverlayRollHorizontallyGui overlayRollHorizontallyGui = getOverlayRollHorizontally(matrixRowCount,
				matrixColumnCount);

		final Tab tabStationary = new Tab("Stationary");
		tabStationary.setUserData(overlayStationaryGui);
		tabStationary.setContent(new ScrollPane(overlayStationaryGui));

		final Tab tabRoll = new Tab("Roll horizontally");
		tabRoll.setUserData(overlayRollHorizontallyGui);
		tabRoll.setContent(new ScrollPane(overlayRollHorizontallyGui));

		return new TabPane(tabStationary, tabRoll);
	}

	OverlayRollHorizontallyGui getOverlayRollHorizontally(final Integer matrixRowCount,
			final Integer matrixColumnCount) {
		final List<List<LedStateView>> ledStateRoll = generateListOfDefaultLedStates(matrixRowCount, matrixColumnCount);
		final RgbColorView onColorRoll = new RgbColorView(255, 0, 0);
		final RgbColorView offColorRoll = new RgbColorView(0, 255, 0);
		final int beginIndexMark = matrixColumnCount;
		final int yposition = 0;
		final OverlayRollHorizontallyView overlayRollHorizontallyView = new OverlayRollHorizontallyView(ledStateRoll,
				onColorRoll, offColorRoll, beginIndexMark, yposition);
		final OverlayRollHorizontallyGui overlayRollHorizontallyGui = new OverlayRollHorizontallyGui(
				overlayRollHorizontallyView);
		return overlayRollHorizontallyGui;
	}

	OverlayStationaryGui getOverlayStationaryGui(final Integer matrixRowCount, final Integer matrixColumnCount) {
		final List<List<LedStateView>> ledStatesStationary = generateListOfDefaultLedStates(matrixRowCount,
				matrixColumnCount);
		final RgbColorView onColorStationary = new RgbColorView(255, 0, 0);
		final RgbColorView offColorStationary = new RgbColorView(0, 255, 0);
		final int duration = 1;
		final OverlayStationaryView overlayStationaryView = new OverlayStationaryView(ledStatesStationary,
				onColorStationary, offColorStationary, duration);
		final OverlayStationaryGui overlayStationaryGui = new OverlayStationaryGui(overlayStationaryView);
		return overlayStationaryGui;
	}

	List<List<LedStateView>> generateListOfDefaultLedStates(final int rowCount, final int columnCount) {
		return IntStream.range(0, rowCount).mapToObj(rowIndex -> {
			return IntStream.range(0, columnCount).mapToObj(columnIndex -> {
				return LedStateView.TRANSPARENT;
			}).collect(Collectors.toList());
		}).collect(Collectors.toList());
	}

	void onUpdateAction() {
		AdministrationApp.showProgressBar();
	}
}

class MatricesTreeItemModel extends TreeItemModel {
	MatricesTreeItemModel() {
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

		final Dialog<CreateMatrixCommand> dialog = new ControlButtonDialog<>(matrixInfoGui, buttonType -> {
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
	private final GuiContext matrixGui;

	MatrixTreeItemModel(final String label, final GuiContext matrixGui) {
		super(label);
		this.matrixGui = matrixGui;
	}

	@Override
	void startAnimation() {
		if (matrixGui.getCompiledFramesGui() != null) {
			matrixGui.getCompiledFramesGui().startAnimation();
		}
	}

	@Override
	void stopAnimation() {
		if (matrixGui.getCompiledFramesGui() != null) {
			matrixGui.getCompiledFramesGui().stopAnimation();
		}
	}

	@Override
	Collection<Node> getGuis() {
		return asList(matrixGui.getMatrixInfoGui(), matrixGui.getCompiledFramesGui());
	}

	@Override
	EnumSet<TreeViewControlButtonIcons> getAllowedControlButtonIcons() {
		return of(PLUS_SIGN, MINUS_SIGN);
	}

	@Override
	void onPlusSignAction() {
		super.onPlusSignAction();

		final Integer matrixRowCount = matrixGui.getMatrixInfoGui().getMatrixRowCount();
		final Integer matrixColumnCount = matrixGui.getMatrixInfoGui().getMatrixColumnCount();
		final Dialog<OverlayView> dialog = getOverlaysDialog(matrixRowCount, matrixColumnCount);

		final Optional<OverlayView> overlayViewOptional = dialog.showAndWait();
		if (overlayViewOptional.isPresent()) {
			final OverlayView overlayView = overlayViewOptional.get();
			final MatrixView matrix = matrixGui.getMatrixModel();

			matrix.appendNewSceneAndAppendOverlayToIt(overlayView);

			RestClient.updateMatrix(matrix);
			AdministrationApp.refreshGui();
		}

		AdministrationApp.hideProgressBar();
	}

	@Override
	void onMinusSignActionOK() {
		super.onMinusSignActionOK();

		final Integer matrixId = matrixGui.getMatrixInfoGui().getMatrixId();
		RestClient.deleteMatrix(matrixId);
		AdministrationApp.refreshGui();
	}

	@Override
	void onUpdateAction() {
		super.onUpdateAction();

		final MatrixView matrixModel = matrixGui.getMatrixModel();
		RestClient.updateMatrix(matrixModel);
		AdministrationApp.refreshGui();
	}
}

class SceneTreeItemModel extends TreeItemModel {
	private final GuiContext matrixGui;
	private final List<OverlayGui> scene;

	SceneTreeItemModel(final GuiContext matrixGui, final List<OverlayGui> scene) {
		super("<Scene>");
		this.matrixGui = matrixGui;
		this.scene = scene;
	}

	@Override
	Collection<Node> getGuis() {
		return asList(matrixGui.getMatrixInfoGui());
	}

	@Override
	EnumSet<TreeViewControlButtonIcons> getAllowedControlButtonIcons() {
		return of(PLUS_SIGN, MINUS_SIGN);
	}

	@Override
	void onPlusSignAction() {
		super.onPlusSignAction();

		final Integer matrixRowCount = matrixGui.getMatrixInfoGui().getMatrixRowCount();
		final Integer matrixColumnCount = matrixGui.getMatrixInfoGui().getMatrixColumnCount();
		final Dialog<OverlayView> dialog = getOverlaysDialog(matrixRowCount, matrixColumnCount);

		final Optional<OverlayView> overlayViewOptional = dialog.showAndWait();
		if (overlayViewOptional.isPresent()) {
			final OverlayView overlayView = overlayViewOptional.get();
			final MatrixView matrix = matrixGui.getMatrixModel();

			final int sceneIndex = matrixGui.getScenesGui().indexOf(scene);
			matrix.appendNewOverlayToScene(sceneIndex, overlayView);

			RestClient.updateMatrix(matrix);
			AdministrationApp.refreshGui();
		}

		AdministrationApp.hideProgressBar();
	}

	@Override
	void onMinusSignActionOK() {
		matrixGui.getScenesGui().remove(scene);

		final MatrixView matrixModel = matrixGui.getMatrixModel();
		RestClient.updateMatrix(matrixModel);
		AdministrationApp.refreshGui();
	}

	@Override
	void onUpdateAction() {
		super.onUpdateAction();

		final MatrixView matrixModel = matrixGui.getMatrixModel();
		RestClient.updateMatrix(matrixModel);
		AdministrationApp.refreshGui();
	}
}

class OverlayTreeItemModel extends TreeItemModel {
	private final GuiContext matrixGui;
	private final OverlayGui overlayGui;

	OverlayTreeItemModel(final String label, final GuiContext matrixGui, final OverlayGui overlayGui) {
		super(label);
		this.matrixGui = matrixGui;
		this.overlayGui = overlayGui;
	}

	@Override
	Collection<Node> getGuis() {
		return asList(matrixGui.getMatrixInfoGui(), (Node) overlayGui);
	}

	@Override
	EnumSet<TreeViewControlButtonIcons> getAllowedControlButtonIcons() {
		return of(MINUS_SIGN);
	}

	@Override
	void onMinusSignActionOK() {
		matrixGui.getScenesGui().forEach(scene -> {
			scene.remove(overlayGui);
		});

		final MatrixView matrixModel = matrixGui.getMatrixModel();
		RestClient.updateMatrix(matrixModel);
		AdministrationApp.refreshGui();
	}

	@Override
	void onUpdateAction() {
		super.onUpdateAction();

		final MatrixView matrixModel = matrixGui.getMatrixModel();
		RestClient.updateMatrix(matrixModel);
		AdministrationApp.refreshGui();
	}
}