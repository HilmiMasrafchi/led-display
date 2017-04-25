/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import lombok.Getter;
import me.hmasrafchi.leddisplay.administration.application.gui.javafx.component.TextFieldWithLabel;
import me.hmasrafchi.leddisplay.administration.model.view.LedView;
import me.hmasrafchi.leddisplay.administration.model.view.MatrixView;
import me.hmasrafchi.leddisplay.administration.model.view.OverlayRollHorizontallyView;
import me.hmasrafchi.leddisplay.administration.model.view.OverlayStationaryView;
import me.hmasrafchi.leddisplay.administration.model.view.OverlayView;
import me.hmasrafchi.leddisplay.administration.model.view.RgbColorView;

/**
 * @author michelin
 *
 */
public final class MatrixGui extends VBox {
	private static final int LED_WIDTH = 30;
	private static final int LED_HEIGHT = 30;

	@Getter
	private Animation animation;
	private TextFieldWithLabel labelId;
	private TextFieldWithLabel labelName;
	private TextFieldWithLabel rowCountLabel;
	private TextFieldWithLabel columnCountLabel;
	private List<List<OverlayGui>> scenesGui;
	private GraphicsContext graphics;

	public MatrixGui(final MatrixView matrix) {
		final Node matrixInfoGui = getMatrixInfoGui(matrix);
		getChildren().add(matrixInfoGui);

		final Node scenesGui = getScenesGui(matrix);
		getChildren().add(scenesGui);

		final Node updateMatrixButton = getUpdateButton();
		getChildren().add(updateMatrixButton);

		final Canvas canvas = getCompiledFramesGui(matrix);
		getChildren().add(canvas);

		this.graphics = canvas.getGraphicsContext2D();
		final List<List<List<LedView>>> compiledFrames = matrix.getCompiledFrames();
		this.animation = getAnimation(graphics, compiledFrames);
	}

	private Node getUpdateButton() {
		final Button updateButton = new Button("Update");
		updateButton.setOnAction(event -> {
			final MatrixView matrixModel = getMatrixModel();
			final Client jaxRsClient = ClientBuilder.newClient();
			jaxRsClient.target("http://localhost:8080/led-display-administration").path("matrices")
					.request(MediaType.APPLICATION_JSON).put(Entity.json(matrixModel));

			this.animation.stop();
			final Response response = jaxRsClient.target("http://localhost:8080/led-display-administration")
					.path("matrices/" + matrixModel.getId()).request(MediaType.APPLICATION_JSON).get();
			final MatrixView readEntity = response.readEntity(MatrixView.class);
			this.animation = getAnimation(graphics, readEntity.getCompiledFrames());
			this.animation.play();
		});

		return updateButton;
	}

	private Node getMatrixInfoGui(final MatrixView matrix) {
		final Integer id = matrix.getId();
		this.labelId = new TextFieldWithLabel("matrix id: ", String.valueOf(id), true);

		final String name = matrix.getName();
		this.labelName = new TextFieldWithLabel("name: ", name);

		final int rowCount = matrix.getRowCount();
		this.rowCountLabel = new TextFieldWithLabel("row count: ", String.valueOf(rowCount));

		final int columnCount = matrix.getColumnCount();
		this.columnCountLabel = new TextFieldWithLabel("column count: ", String.valueOf(columnCount));

		return new VBox(this.labelId, this.labelName, this.rowCountLabel, this.columnCountLabel);
	}

	private Node getScenesGui(final MatrixView matrix) {
		final TabPane tabPane = new TabPane();
		this.scenesGui = new ArrayList<>();

		final List<Tab> scenes = matrix.getScenes().stream().map(scene -> {
			final List<OverlayGui> overlaysGui = new ArrayList<>();
			final List<VBox> overlaysGuiList = scene.stream().map(overlay -> {
				if (overlay instanceof OverlayStationaryView) {
					final OverlayStationaryView overlayStationary = (OverlayStationaryView) overlay;
					final OverlayStationaryGui overlayStationaryGui = new OverlayStationaryGui(overlayStationary);
					overlaysGui.add(overlayStationaryGui);
					return overlayStationaryGui;
				}

				if (overlay instanceof OverlayRollHorizontallyView) {
					final OverlayRollHorizontallyView overlayRollHorizontally = (OverlayRollHorizontallyView) overlay;
					final OverlayRollHorizontallyGui overlayRollHorizontallyGui = new OverlayRollHorizontallyGui(
							overlayRollHorizontally);
					overlaysGui.add(overlayRollHorizontallyGui);
					return overlayRollHorizontallyGui;
				}

				return new VBox();
			}).collect(Collectors.toList());
			scenesGui.add(overlaysGui);

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

	private Animation getAnimation(final GraphicsContext graphics, final List<List<List<LedView>>> compiledFrames) {
		final Timeline animation = new Timeline(new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>() {
			private Iterator<List<List<LedView>>> frameIterator = compiledFrames.iterator();

			@Override
			public void handle(ActionEvent event) {
				if (!frameIterator.hasNext()) {
					this.frameIterator = compiledFrames.iterator();
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
		}));
		animation.setCycleCount(Timeline.INDEFINITE);

		return animation;
	}

	public MatrixView getMatrixModel() {
		final Integer id = Integer.valueOf(this.labelId.getText());
		final String name = this.labelName.getText();
		final Integer rowCount = Integer.valueOf(this.rowCountLabel.getText());
		final Integer columnCount = Integer.valueOf(this.columnCountLabel.getText());
		final List<List<OverlayView>> scenes = scenesGui.stream().map(overlaysGui -> {
			return overlaysGui.stream().map(overlayGui -> {
				return overlayGui.getOverlayModel();
			}).collect(Collectors.toList());
		}).collect(Collectors.toList());
		return new MatrixView(id, name, rowCount, columnCount, scenes, null);
	}
}