/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx.component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import me.hmasrafchi.leddisplay.administration.model.view.LedStateView;

/**
 * @author michelin
 *
 */
public final class LedStateComponent extends BorderPane {
	private static final int BUTTON_MIN_DIMENSION = 40;

	private static Map<LedStateView, String> LEDSTATE_TO_TEXT_MAPPING = new LinkedHashMap<>();
	static {
		LEDSTATE_TO_TEXT_MAPPING.put(LedStateView.TRANSPARENT, "");
		LEDSTATE_TO_TEXT_MAPPING.put(LedStateView.ON, "●");
		LEDSTATE_TO_TEXT_MAPPING.put(LedStateView.OFF, "◌");
		LEDSTATE_TO_TEXT_MAPPING.put(LedStateView.UNRECOGNIZED, "x");
	}

	private final List<List<LedStateView>> states;

	public LedStateComponent(final List<List<LedStateView>> states) {
		this.states = states.stream().map(stateRow -> new ArrayList<>(stateRow))
				.collect(Collectors.toCollection(ArrayList::new));

		setTop(getTopNode());
		initCenterPane();
	}

	private Node getTopNode() {
		final Button removeColumnButton = getControlButton("←");
		removeColumnButton.setOnAction(event -> {
			if (states.size() >= 1 && states.get(0).size() == 1) {
				return;
			}

			this.states.stream().forEach(statesRow -> {
				statesRow.remove(statesRow.size() - 1);
			});
			initCenterPane();
		});

		final Button addColumnButton = getControlButton("→");
		addColumnButton.setOnAction(event -> {
			this.states.stream().forEach(statesRow -> {
				statesRow.add(LedStateView.TRANSPARENT);
			});
			initCenterPane();
		});

		final Button removeRowButton = getControlButton("↑");
		removeRowButton.setOnAction(event -> {
			if (this.states.size() == 1) {
				return;
			}

			this.states.remove(this.states.size() - 1);
			initCenterPane();
		});

		final Button addRowButton = getControlButton("↓");
		addRowButton.setOnAction(event -> {
			final int rowSize = this.states.get(0).size();
			final List<LedStateView> newStateRow = new ArrayList<>(rowSize);
			IntStream.range(0, rowSize).forEach(i -> newStateRow.add(LedStateView.TRANSPARENT));
			this.states.add(newStateRow);

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
		states.forEach(stateRow -> {
			final Node[] statesRowArray = new Node[stateRow.size()];
			stateRow.stream().map(state -> {
				final String initialText = LEDSTATE_TO_TEXT_MAPPING.get(state);
				return new LedStateButton(initialText);
			}).collect(Collectors.toList()).toArray(statesRowArray);

			final int lastRowIndex = findGridPaneLastRowIndex(gridPane);
			gridPane.addRow(lastRowIndex + 1, statesRowArray);
		});

		setCenter(gridPane);
	}

	private int findGridPaneLastRowIndex(final GridPane pane) {
		return pane.getChildren().stream().mapToInt(n -> {
			Integer row = GridPane.getRowIndex(n);
			Integer rowSpan = GridPane.getRowSpan(n);
			return (row == null ? 0 : row) + (rowSpan == null ? 0 : rowSpan - 1);
		}).max().orElse(-1);
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
			});
		}
	}
}