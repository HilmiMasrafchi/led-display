/**
 * 
 */
package me.hmasrafchi.leddisplay.jfx.gui;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import me.hmasrafchi.leddisplay.model.overlay.Overlay;
import me.hmasrafchi.leddisplay.util.TwoDimensionalListRectangular;

/**
 * @author michelin
 *
 */
class OverlayBaseGUI extends BorderPane {
	private final static Insets INSETS = new Insets(5);

	private final static BiMap<Overlay.State, String> STATE_TO_BUTTONTEXT_MAPPING = HashBiMap.create();
	static {
		STATE_TO_BUTTONTEXT_MAPPING.put(Overlay.State.ON, "X");
		STATE_TO_BUTTONTEXT_MAPPING.put(Overlay.State.OFF, "O");
		STATE_TO_BUTTONTEXT_MAPPING.put(Overlay.State.TRANSPARENT, "");
	}

	private ButtonGrid buttonGrid;

	OverlayBaseGUI(final TwoDimensionalListRectangular<Overlay.State> states) {
		this.buttonGrid = getStatesButtonGrid(states);
		setButtonTextBasedOnState(buttonGrid, states, STATE_TO_BUTTONTEXT_MAPPING);

		setPadding(INSETS);
		setCenter();
	}

	private void setCenter() {
		final ScrollPane scrollPane = new ScrollPane(buttonGrid);
		setCenter(scrollPane);
	}

	private ButtonGrid getStatesButtonGrid(final TwoDimensionalListRectangular<Overlay.State> states) {
		return new ButtonGrid(states.getColumnCount(), states.getRowCount(),
				event -> buttonActionHandler((Button) event.getSource(), STATE_TO_BUTTONTEXT_MAPPING));
	}

	private void setButtonTextBasedOnState(final ButtonGrid buttonGrid,
			final TwoDimensionalListRectangular<Overlay.State> states,
			final BiMap<Overlay.State, String> stateToButtonTextMapping) {
		for (int i = 0; i < states.getRowCount(); i++) {
			for (int j = 0; j < states.getColumnCount(); j++) {
				final Overlay.State state = states.getValueAt(j, i);
				final Button button = buttonGrid.getStateButtons().get(i).get(j);
				button.setText(stateToButtonTextMapping.get(state));
			}
		}
	}

	private void buttonActionHandler(final Button button, final BiMap<Overlay.State, String> stateToButtonTextMapping) {
		final List<String> buttonsText = stateToButtonTextMapping.values().stream().collect(Collectors.toList());
		final String currentButtonText = button.getText();
		final Overlay.State currentState = STATE_TO_BUTTONTEXT_MAPPING.inverse().get(currentButtonText);
		final int currentIndex = buttonsText.indexOf(STATE_TO_BUTTONTEXT_MAPPING.get(currentState));
		final int newIndex = (currentIndex + 1) % buttonsText.size();
		final String buttonNewText = buttonsText.get(newIndex);
		button.setText(buttonNewText);
	}

	List<List<Overlay.State>> getStates() {
		return buttonGrid.getStateButtons().stream().map(row -> row.stream().map(button -> {
			final String buttonText = button.getText();
			return STATE_TO_BUTTONTEXT_MAPPING.inverse().get(buttonText);
		}).collect(Collectors.toList())).collect(Collectors.toList());
	}
}