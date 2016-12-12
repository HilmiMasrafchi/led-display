/**
 * 
 */
package me.hmasrafchi.leddisplay.jfx.gui;

import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import me.hmasrafchi.leddisplay.model.Led;
import me.hmasrafchi.leddisplay.model.Led.RgbColor;
import me.hmasrafchi.leddisplay.model.scene.RandomColorScene;
import me.hmasrafchi.leddisplay.model.scene.Scene;

/**
 * @author michelin
 *
 */
public final class SceneRandomColorGUI extends BorderPane implements Model<Scene> {
	private final static Insets INSETS = new Insets(5);
	private final static int CONTROL_PANEL_BUTTONS_MIN_SIZE = 40;

	private final List<ColorPicker> colorPickers;

	public SceneRandomColorGUI(final List<RgbColor> colors) {
		this.colorPickers = initializeGUI(colors);
	}

	private List<ColorPicker> initializeGUI(final List<Led.RgbColor> colors) {
		setPadding(INSETS);

		final List<ColorPicker> colorPickers = initializeColorPickers(colors);

		final ObservableList<ColorPicker> observableColorPickers = FXCollections.observableArrayList(colorPickers);
		final ListView<ColorPicker> listView = new ListView<>(observableColorPickers);
		BorderPane.setMargin(listView, INSETS);
		setCenter(listView);

		final Button addButton = new Button("+");
		addButton.setOnAction(event -> {
			final Color color = new Color(Math.random(), Math.random(), Math.random(), 1);
			observableColorPickers.add(new ColorPicker(color));
		});
		addButton.setMinSize(CONTROL_PANEL_BUTTONS_MIN_SIZE, CONTROL_PANEL_BUTTONS_MIN_SIZE);

		final Button removeButton = new Button("-");
		removeButton.setOnAction(event -> {
			final ColorPicker selectedItem = listView.getSelectionModel().getSelectedItem();
			observableColorPickers.remove(selectedItem);
		});
		removeButton.setMinSize(CONTROL_PANEL_BUTTONS_MIN_SIZE, CONTROL_PANEL_BUTTONS_MIN_SIZE);

		final VBox controlPanel = new VBox(10);
		controlPanel.getChildren().addAll(addButton, removeButton);
		BorderPane.setMargin(controlPanel, INSETS);
		setRight(controlPanel);

		return colorPickers;
	}

	private List<ColorPicker> initializeColorPickers(final List<RgbColor> colors) {
		return colors.stream().map(color -> {
			final double red = color.getR() / 255d;
			final double green = color.getG() / 255d;
			final double blue = color.getB() / 255d;
			return new ColorPicker(new Color(red, green, blue, 1));
		}).collect(Collectors.toList());
	}

	private List<Led.RgbColor> getColors() {
		return colorPickers.stream().map(colorPicker -> {
			final Color color = colorPicker.getValue();
			final int red = (int) (color.getRed() * 255);
			final int green = (int) (color.getGreen() * 255);
			final int blue = (int) (color.getBlue() * 255);

			return new Led.RgbColor(red, green, blue);
		}).collect(Collectors.toList());
	}

	@Override
	public Scene getModel() {
		return new RandomColorScene(getColors());
	}
}