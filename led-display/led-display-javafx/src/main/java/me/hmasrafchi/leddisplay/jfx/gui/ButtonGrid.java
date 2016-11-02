/**
 * 
 */
package me.hmasrafchi.leddisplay.jfx.gui;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;

/**
 * @author michelin
 *
 */
public final class ButtonGrid extends BorderPane {
	private final static Insets INSETS = new Insets(10);
	private final static int BUTTONS_MIN_SIZE = 40;

	private final EventHandler<ActionEvent> eventHandler;

	@Getter
	private List<List<Button>> stateButtons;
	private GridPane gridPane;

	public ButtonGrid(final int columnsCount, final int rowsCount) {
		this(columnsCount, rowsCount, null);
	}

	public ButtonGrid(final int columnsCount, final int rowsCount, final EventHandler<ActionEvent> eventHandler) {
		this.stateButtons = new ArrayList<>(rowsCount);
		this.eventHandler = eventHandler;

		setTop(createControlPanel(columnsCount, rowsCount));
		setCenter(createGrid(columnsCount, rowsCount));
		setLeft(getControlPanel());

		setPadding(INSETS);
	}

	private Node createGrid(final int columnsCount, final int rowsCount) {
		this.gridPane = new GridPane();
		this.stateButtons = new ArrayList<>();

		for (int i = 0; i < rowsCount; i++) {
			final List<Button> currentRow = new ArrayList<>();
			for (int j = 0; j < columnsCount; j++) {
				final Button currentButton = createButton();
				currentButton.setOnAction(eventHandler);
				currentRow.add(currentButton);
			}
			this.stateButtons.add(currentRow);
			gridPane.addRow(i, currentRow.stream().toArray(Button[]::new));
		}

		return new ScrollPane(gridPane);
	}

	private Node createControlPanel(final int columnsCount, final int rowsCount) {
		final int preferredColumnCount = 3;

		final TextField columnsTextField = new TextField();
		columnsTextField.setPrefColumnCount(preferredColumnCount);
		columnsTextField.setPromptText("Columns");
		columnsTextField.setText(String.valueOf(columnsCount));

		final TextField rowsTextField = new TextField();
		rowsTextField.setPrefColumnCount(preferredColumnCount);
		rowsTextField.setPromptText("Rows");
		rowsTextField.setText(String.valueOf(rowsCount));

		final Button createNewStates = new Button("Create");
		createNewStates.setOnAction(event -> {
			try {
				final int desiredColumnsCount = Integer.parseInt(columnsTextField.getText());
				final int desiredRowsCount = Integer.parseInt(rowsTextField.getText());
				final Node createGrid = createGrid(desiredColumnsCount, desiredRowsCount);
				setCenter(createGrid);
			} catch (NumberFormatException nfe) {
			}
		});

		final HBox createNewButtonsPane = new HBox(INSETS.getTop());
		createNewButtonsPane.getChildren().addAll(columnsTextField, rowsTextField, createNewStates);

		return createNewButtonsPane;
	}

	private Button createButton() {
		final Button currentButton = new Button();
		currentButton.setMinSize(BUTTONS_MIN_SIZE, BUTTONS_MIN_SIZE);
		return currentButton;
	}

	private Node getControlPanel() {
		final VBox controlPanel = new VBox(INSETS.getTop());
		final Button addColumnButton = new Button("→c");
		addColumnButton.setMinSize(40, 40);
		addColumnButton.setOnAction(event -> {
			addColumn();
		});
		final Button addRowButton = new Button("↓r");
		addRowButton.setMinSize(40, 40);
		addRowButton.setOnAction(event -> {
			addRow();
		});
		controlPanel.getChildren().addAll(addColumnButton, addRowButton);

		controlPanel.setPadding(INSETS);
		return controlPanel;
	}

	public List<Button> addRow() {
		final int columnsCount = stateButtons.get(0).size();
		final List<Button> newButtons = new ArrayList<>();
		for (int i = 0; i < columnsCount; i++) {
			final Button button = createButton();
			button.setOnAction(eventHandler);
			newButtons.add(button);
		}
		gridPane.addRow(stateButtons.size(), newButtons.stream().toArray(Button[]::new));

		stateButtons.add(newButtons);
		return newButtons;
	}

	public List<Button> addColumn() {
		final List<Button> newButtons = new ArrayList<>();

		final int columnIndex = stateButtons.get(0).size();
		for (int i = 0; i < stateButtons.size(); i++) {
			final Button button = createButton();
			button.setOnAction(eventHandler);
			gridPane.add(button, columnIndex, i);
			newButtons.add(button);

			stateButtons.get(i).add(button);
		}

		return newButtons;
	}

	public int getColumnsCount() {
		return this.stateButtons.get(0).size();
	}

	public int getRowsCount() {
		return stateButtons.size();
	}
}