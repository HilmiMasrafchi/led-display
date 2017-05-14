/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import me.hmasrafchi.leddisplay.model.view.LedStateView;

/**
 * @author michelin
 *
 */
final class LedStateComponent extends BorderPane {
	private static final String TRANSPARENT_STATE_LABEL = " ";
	private static final String ON_STATE_LABEL = "●";
	private static final String OFF_STATE_LABEL = "◌";
	private static final String UNRECOGNIZED_STATE_LABEL = "x";

	private static final int BUTTON_MIN_DIMENSION = 30;

	private static Map<LedStateView, String> LEDSTATE_TO_TEXT_MAPPING = new LinkedHashMap<>();
	static {
		LEDSTATE_TO_TEXT_MAPPING.put(LedStateView.TRANSPARENT, TRANSPARENT_STATE_LABEL);
		LEDSTATE_TO_TEXT_MAPPING.put(LedStateView.ON, ON_STATE_LABEL);
		LEDSTATE_TO_TEXT_MAPPING.put(LedStateView.OFF, OFF_STATE_LABEL);
		LEDSTATE_TO_TEXT_MAPPING.put(LedStateView.UNRECOGNIZED, UNRECOGNIZED_STATE_LABEL);
	}

	private final List<List<LedStateView>> states;

	public LedStateComponent(final List<List<LedStateView>> states) {
		this.states = states;

		setTop(getTopNode());
		initCenterPane();
	}

	private Node getTopNode() {
		final Button removeColumnButton = getControlButton("←");
		removeColumnButton.setOnAction(event -> {
			if (states.size() >= 1 && states.get(0).size() == 1) {
				return;
			}

			states.stream().forEach(statesRow -> {
				statesRow.remove(statesRow.size() - 1);
			});
			initCenterPane();
		});

		final Button addColumnButton = getControlButton("→");
		addColumnButton.setOnAction(event -> {
			states.stream().forEach(statesRow -> {
				statesRow.add(LedStateView.TRANSPARENT);
			});
			initCenterPane();
		});

		final Button removeRowButton = getControlButton("↑");
		removeRowButton.setOnAction(event -> {
			if (states.size() == 1) {
				return;
			}

			states.remove(states.size() - 1);
			initCenterPane();
		});

		final Button addRowButton = getControlButton("↓");
		addRowButton.setOnAction(event -> {
			final int rowSize = states.get(0).size();
			final List<LedStateView> newStateRow = new ArrayList<>(rowSize);
			IntStream.range(0, rowSize).forEach(i -> newStateRow.add(LedStateView.TRANSPARENT));
			states.add(newStateRow);

			initCenterPane();
		});

		return new HBox(removeColumnButton, addColumnButton, addRowButton, removeRowButton);
	}

	private Button getControlButton(final String text) {
		final Button button = new Button(text);
		button.setMinWidth(BUTTON_MIN_DIMENSION);
		button.setMinHeight(BUTTON_MIN_DIMENSION);

		return button;
	}

	private void initCenterPane() {
		final GridPane gridPane = new GridPane();

		IntStream.range(0, states.size()).forEach(rowIndex -> {
			final List<LedStateView> list = states.get(rowIndex);
			IntStream.range(0, list.size()).forEach(columnIndex -> {
				final String initialText = LEDSTATE_TO_TEXT_MAPPING.get(states.get(rowIndex).get(columnIndex));
				final LedStateButton ledStateButton = new LedStateButton(initialText);
				GridPane.setConstraints(ledStateButton, columnIndex, rowIndex);
				gridPane.getChildren().add(ledStateButton);
			});
		});

		setCenter(gridPane);
	}

	private class LedStateButton extends Button {
		private int currentIndex;

		public LedStateButton(final String initialText) {
			setMinHeight(BUTTON_MIN_DIMENSION);
			setMinWidth(BUTTON_MIN_DIMENSION);

			setText(initialText);

			final List<String> texts = new ArrayList<>(LEDSTATE_TO_TEXT_MAPPING.values());
			this.currentIndex = texts.indexOf(getText());
			this.setOnAction(event -> {
				this.currentIndex = ++this.currentIndex % texts.size();
				setText(texts.get(currentIndex));

				final int columnIndex = GridPane.getColumnIndex(this);
				final int rowIndex = GridPane.getRowIndex(this);

				final String newText = getText();
				final LedStateView ledState = getStateFromText(newText);
				states.get(rowIndex).set(columnIndex, ledState);
			});
		}

		private LedStateView getStateFromText(final String text) {
			for (Map.Entry<LedStateView, String> mapEntries : LEDSTATE_TO_TEXT_MAPPING.entrySet()) {
				if (mapEntries.getValue().equals(text)) {
					return mapEntries.getKey();
				}
			}

			throw new RuntimeException();
		}
	}

	public List<List<LedStateView>> getLedStatesModel() {
		return states;
	}
}