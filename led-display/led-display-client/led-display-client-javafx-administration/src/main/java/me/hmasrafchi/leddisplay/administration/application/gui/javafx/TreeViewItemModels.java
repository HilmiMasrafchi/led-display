/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx;

import static java.util.Arrays.asList;
import static java.util.EnumSet.of;
import static me.hmasrafchi.leddisplay.administration.application.gui.javafx.TreeViewControlButtonIcons.MINUS_SIGN;
import static me.hmasrafchi.leddisplay.administration.application.gui.javafx.TreeViewControlButtonIcons.PLUS_SIGN;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.Response;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import lombok.Getter;
import me.hmasrafchi.leddisplay.administration.application.JavaFxGui;
import me.hmasrafchi.leddisplay.administration.application.RestClient;
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
abstract class TreeItemModel {
	@Getter
	final String label;
	final Button updateButton;

	TreeItemModel(final String label) {
		this.label = label;
		this.updateButton = new Button("Update");
		this.updateButton.setOnAction(event -> onUpdateAction());
	}

	Collection<Node> getNonNullGuis() {
		return getGuis().stream().filter(node -> node != null).collect(Collectors.toList());
	}

	final InvocationCallback<Response> defaultInvocationCallback = new InvocationCallback<Response>() {
		@Override
		public void completed(final Response response) {
			JavaFxGui.refreshGui();
		}

		@Override
		public void failed(final Throwable throwable) {
		}
	};

	abstract EnumSet<TreeViewControlButtonIcons> getAllowedControlButtonIcons();

	abstract Collection<Node> getGuis();

	void startAnimation() {
	}

	void stopAnimation() {
	}

	void onPlusSignAction() {
		JavaFxGui.showProgressBar();
	}

	final void onMinusSignAction() {
		JavaFxGui.showProgressBar();

		final Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure want to delete selected item?");
		alert.setHeaderText(null);
		final Optional<ButtonType> showAndWait = alert.showAndWait();
		showAndWait.ifPresent(buttonType -> {
			if (buttonType.getButtonData().equals(ButtonBar.ButtonData.CANCEL_CLOSE)) {
				JavaFxGui.hideProgressBar();
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
		JavaFxGui.showProgressBar();
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
			return buttonType.getButtonData().equals(ButtonBar.ButtonData.CANCEL_CLOSE) ? null
					: new CreateMatrixCommand(matrixInfoGui.getMatrixName(), matrixInfoGui.getMatrixRowCount(),
							matrixInfoGui.getMatrixColumnCount(), new ArrayList<>());
		});

		final Optional<CreateMatrixCommand> result = dialog.showAndWait();
		result.ifPresent(createMatrixCommand -> {
			RestClient.createMatrix(createMatrixCommand, defaultInvocationCallback);
		});
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
		return asList(matrixGui.getMatrixInfoGui(), matrixGui.getCompiledFramesGui(), updateButton);
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

			RestClient.updateMatrix(matrix, defaultInvocationCallback);
		}
	}

	@Override
	void onMinusSignActionOK() {
		super.onMinusSignActionOK();

		final BigInteger matrixId = matrixGui.getMatrixInfoGui().getMatrixId();
		RestClient.deleteMatrix(matrixId, defaultInvocationCallback);
	}

	@Override
	void onUpdateAction() {
		super.onUpdateAction();

		final MatrixView matrixModel = matrixGui.getMatrixModel();
		RestClient.updateMatrix(matrixModel, defaultInvocationCallback);
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
		return asList(matrixGui.getMatrixInfoGui(), updateButton);
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
			RestClient.updateMatrix(matrix, defaultInvocationCallback);
		}
	}

	@Override
	void onMinusSignActionOK() {
		matrixGui.getScenesGui().remove(scene);

		final MatrixView matrixModel = matrixGui.getMatrixModel();
		RestClient.updateMatrix(matrixModel, defaultInvocationCallback);
	}

	@Override
	void onUpdateAction() {
		super.onUpdateAction();

		final MatrixView matrixModel = matrixGui.getMatrixModel();
		RestClient.updateMatrix(matrixModel, defaultInvocationCallback);
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
		return asList(matrixGui.getMatrixInfoGui(), (Node) overlayGui, updateButton);
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
		RestClient.updateMatrix(matrixModel, defaultInvocationCallback);
	}

	@Override
	void onUpdateAction() {
		super.onUpdateAction();

		final MatrixView matrixModel = matrixGui.getMatrixModel();
		RestClient.updateMatrix(matrixModel, defaultInvocationCallback);
	}
}