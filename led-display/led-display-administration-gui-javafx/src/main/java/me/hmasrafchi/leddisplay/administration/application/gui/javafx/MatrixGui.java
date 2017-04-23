/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx;

import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import me.hmasrafchi.leddisplay.administration.model.view.MatrixView;
import me.hmasrafchi.leddisplay.administration.model.view.OverlayRollHorizontallyView;
import me.hmasrafchi.leddisplay.administration.model.view.OverlayStationaryView;

/**
 * @author michelin
 *
 */
public final class MatrixGui extends VBox {
	public MatrixGui(final MatrixView matrix) {
		final Node matrixInfoGui = getMatrixInfoGui(matrix);
		getChildren().add(matrixInfoGui);

		final List<HBox> scenes = matrix.getScenes().stream().map(scene -> {
			final List<VBox> overlaysGuiList = scene.stream().map(overlay -> {
				if (overlay instanceof OverlayStationaryView) {
					final OverlayStationaryView overlayStationary = (OverlayStationaryView) overlay;
					return new OverlayStationaryGui(overlayStationary);
				}

				if (overlay instanceof OverlayRollHorizontallyView) {
					final OverlayRollHorizontallyView overlayRollHorizontally = (OverlayRollHorizontallyView) overlay;
					return new OverlayRollHorizontallyGui(overlayRollHorizontally);
				}

				return new VBox();
			}).collect(Collectors.toList());
			final HBox hBox = new HBox();
			hBox.getChildren().addAll(overlaysGuiList);
			return hBox;
		}).collect(Collectors.toList());
		getChildren().addAll(scenes);
	}

	private Node getMatrixInfoGui(final MatrixView matrix) {
		final Integer id = matrix.getId();
		final Label labelId = new Label("matrix id: " + id);

		final String name = matrix.getName();
		final Label labelName = new Label("name: " + name);

		final int rowCount = matrix.getRowCount();
		final Label rowCountLabel = new Label("row count: " + rowCount);

		final int columnCount = matrix.getColumnCount();
		final Label columnCountLabel = new Label("column count: " + columnCount);

		return new VBox(labelId, labelName, rowCountLabel, columnCountLabel);
	}
}