/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx;

import static java.util.Arrays.asList;
import static java.util.EnumSet.of;
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
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Dialog;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
		return of(PLUS_SIGN);
	}

	@Override
	void onPlusSignAction() {
		super.onPlusSignAction();

		final Integer matrixRowCount = matrixGui.getMatrixInfoGui().getMatrixRowCount();
		final Integer matrixColumnCount = matrixGui.getMatrixInfoGui().getMatrixColumnCount();
		final OverlayStationaryGui overlayStationaryGui = getOverlayStationaryGui(matrixRowCount, matrixColumnCount);
		final OverlayRollHorizontallyGui overlayRollHorizontallyGui = getOverlayRollHorizontally(matrixRowCount,
				matrixColumnCount);

		final Tab tabStationary = new Tab("Stationary");
		tabStationary.setUserData(overlayStationaryGui);
		tabStationary.setContent(new ScrollPane(overlayStationaryGui));

		final Tab tabRoll = new Tab("Roll horizontally");
		tabRoll.setUserData(overlayRollHorizontallyGui);
		tabRoll.setContent(new ScrollPane(overlayRollHorizontallyGui));

		final TabPane tabPane = new TabPane(tabStationary, tabRoll);

		final Dialog<OverlayView> dialog = new ControlButtonDialog<>(tabPane, buttonType -> {
			if (buttonType.getButtonData().equals(ButtonBar.ButtonData.CANCEL_CLOSE)) {
				return null;
			}

			final Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
			return ((OverlayGui) selectedTab.getUserData()).getOverlayModel();
		});

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

	private OverlayRollHorizontallyGui getOverlayRollHorizontally(final Integer matrixRowCount,
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

	private OverlayStationaryGui getOverlayStationaryGui(final Integer matrixRowCount,
			final Integer matrixColumnCount) {
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

	private List<List<LedStateView>> generateListOfDefaultLedStates(final int rowCount, final int columnCount) {
		return IntStream.range(0, rowCount).mapToObj(rowIndex -> {
			return IntStream.range(0, columnCount).mapToObj(columnIndex -> {
				return LedStateView.TRANSPARENT;
			}).collect(Collectors.toList());
		}).collect(Collectors.toList());
	}
}

class SceneTreeItemModel extends TreeItemModel {
	private final GuiContext matrixGui;

	SceneTreeItemModel(final GuiContext matrixGui) {
		super("<Scene>");
		this.matrixGui = matrixGui;
	}

	@Override
	Collection<Node> getGuis() {
		return asList(matrixGui.getMatrixInfoGui());
	}

	@Override
	EnumSet<TreeViewControlButtonIcons> getAllowedControlButtonIcons() {
		return EnumSet.noneOf(TreeViewControlButtonIcons.class);
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
		return EnumSet.noneOf(TreeViewControlButtonIcons.class);
	}
}