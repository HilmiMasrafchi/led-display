/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx;

import static me.hmasrafchi.leddisplay.administration.application.gui.javafx.TreeViewControlButtonIcons.PLUS_SIGN;
import static me.hmasrafchi.leddisplay.administration.application.gui.javafx.TreeViewControlButtonIcons.PLUS_SIGN2;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.hmasrafchi.leddisplay.administration.model.view.MatrixView;

/**
 * @author michelin
 *
 */
final class MatricesTreeView extends TreeView<TreeItemModel> {
	private CompiledFramesGui currentlySelectedCompiledFrameGui;

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

			final TreeItemModel currentlySelectedTreeItemModel = newValue.getValue();
			currentlySelectedTreeItemModel.startAnimation();

			final Collection<Node> guis = currentlySelectedTreeItemModel.getNonNullGuis();
			final VBox vBox = new VBox();
			vBox.getChildren().addAll(guis);
			parent.setCenter(vBox);
		});
	}

	MatrixView getSelectedMatrixModel() {
		// final MultipleSelectionModel<TreeItem<TreeItemModel>> selectionModel
		// = getSelectionModel();
		// final TreeItem<TreeItemModel> selectedItem =
		// selectionModel.getSelectedItem();
		// final TreeItemModel value = selectedItem.getValue();
		// final MatrixGui matrixGui = value.getMatrixGui();
		// if (matrixGui == null) {
		// return null;
		// }
		// return matrixGui.getMatrixModel();

		return null;
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
		return EnumSet.of(PLUS_SIGN, PLUS_SIGN2);
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
		return EnumSet.noneOf(TreeViewControlButtonIcons.class);
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