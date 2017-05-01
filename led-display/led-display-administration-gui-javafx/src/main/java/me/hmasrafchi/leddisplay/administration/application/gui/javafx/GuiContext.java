/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import me.hmasrafchi.leddisplay.administration.model.view.MatrixView;
import me.hmasrafchi.leddisplay.administration.model.view.OverlayRollHorizontallyView;
import me.hmasrafchi.leddisplay.administration.model.view.OverlayStationaryView;
import me.hmasrafchi.leddisplay.administration.model.view.OverlayView;

/**
 * @author michelin
 *
 */
final class GuiContext {
	@Getter
	private final MatrixInfoGui matrixInfoGui;
	@Getter
	private final List<List<OverlayGui>> scenesGui;
	@Getter
	private final CompiledFramesGui compiledFramesGui;

	GuiContext(final MatrixView matrix) {
		this.matrixInfoGui = getMatrixInfoGui(matrix);
		this.scenesGui = getScenesGui(matrix);
		this.compiledFramesGui = getCompiledFramesGui(matrix);
	}

	private MatrixInfoGui getMatrixInfoGui(final MatrixView matrix) {
		return new MatrixInfoGui(matrix);
	}

	private List<List<OverlayGui>> getScenesGui(final MatrixView matrix) {
		return matrix.getScenes().stream().map(scene -> {
			return scene.stream().map(overlay -> {
				if (overlay instanceof OverlayStationaryView) {
					return new OverlayStationaryGui((OverlayStationaryView) overlay);
				}

				if (overlay instanceof OverlayRollHorizontallyView) {
					return new OverlayRollHorizontallyGui((OverlayRollHorizontallyView) overlay);
				}
				throw new RuntimeException();
			}).collect(Collectors.<OverlayGui>toList());
		}).collect(Collectors.toList());
	}

	private CompiledFramesGui getCompiledFramesGui(final MatrixView matrix) {
		return matrix.getCompiledFrames().isEmpty() ? null
				: new CompiledFramesGui(matrix.getRowCount(), matrix.getColumnCount(), matrix.getCompiledFrames());
	}

	public MatrixView getMatrixModel() {
		final Integer id = matrixInfoGui.getMatrixId();
		final String name = matrixInfoGui.getMatrixName();
		final Integer rowCount = matrixInfoGui.getMatrixRowCount();
		final Integer columnCount = matrixInfoGui.getMatrixColumnCount();
		final List<List<OverlayView>> scenes = scenesGui.stream().map(overlaysGui -> {
			return overlaysGui.stream().map(overlayGui -> {
				return overlayGui.getOverlayModel();
			}).collect(Collectors.toList());
		}).collect(Collectors.toList());

		return new MatrixView(id, name, rowCount, columnCount, scenes, null);
	}
}