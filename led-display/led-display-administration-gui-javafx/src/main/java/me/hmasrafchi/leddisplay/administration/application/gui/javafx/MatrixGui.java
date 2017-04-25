/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import lombok.Getter;
import me.hmasrafchi.leddisplay.administration.model.view.LedView;
import me.hmasrafchi.leddisplay.administration.model.view.MatrixView;
import me.hmasrafchi.leddisplay.administration.model.view.OverlayRollHorizontallyView;
import me.hmasrafchi.leddisplay.administration.model.view.OverlayStationaryView;
import me.hmasrafchi.leddisplay.administration.model.view.RgbColorView;

/**
 * @author michelin
 *
 */
public final class MatrixGui extends VBox {
	private static final int LED_WIDTH = 40;
	private static final int LED_HEIGHT = 40;

	@Getter
	private final AnimationTimer animationTimer;

	public MatrixGui(final MatrixView matrix) {
		getChildren().add(getMatrixInfoGui(matrix));
		getChildren().add(getScenesGui(matrix));
		final Canvas canvas = getCompiledFramesGui(matrix);
		getChildren().add(canvas);

		final GraphicsContext graphics = canvas.getGraphicsContext2D();
		this.animationTimer = new AnimationTimer() {
			private Iterator<List<List<LedView>>> frameIterator = matrix.getCompiledFrames().iterator();

			@Override
			public void handle(long now) {
				if (!frameIterator.hasNext()) {
					this.frameIterator = matrix.getCompiledFrames().iterator();
				}

				int xOffset = 0;
				int yOffset = 0;
				final List<List<LedView>> frame = frameIterator.next();
				for (final List<LedView> ledRow : frame) {
					for (final LedView led : ledRow) {
						final RgbColorView rgbColor = led.getRgbColor();
						final Color rgb = Color.rgb(rgbColor.getR(), rgbColor.getG(), rgbColor.getB());
						graphics.setFill(rgb);
						graphics.fillOval(xOffset++ * LED_WIDTH, yOffset * LED_HEIGHT, LED_WIDTH, LED_HEIGHT);
					}
					xOffset = 0;
					yOffset++;
				}
			}
		};

		this.animationTimer.start();
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

	private Node getScenesGui(final MatrixView matrix) {
		final TabPane tabPane = new TabPane();

		final List<Tab> scenes = matrix.getScenes().stream().map(scene -> {
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

			final HBox hbox = new HBox();
			hbox.getChildren().addAll(overlaysGuiList);

			final Tab tab = new Tab();
			tab.setText("SCENE");
			tab.setContent(new ScrollPane(hbox));

			return tab;
		}).collect(Collectors.toList());
		tabPane.getTabs().addAll(scenes);

		return tabPane;
	}

	private Canvas getCompiledFramesGui(final MatrixView matrix) {
		final int matrixRowCount = matrix.getRowCount();
		final int matrixColumnCount = matrix.getColumnCount();

		return new Canvas(matrixRowCount * LED_WIDTH + 100, matrixColumnCount * LED_HEIGHT + 200);
	}
}