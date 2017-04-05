/**
 * 
 */
package me.hmasrafchi.leddisplay.consumer.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import me.hmasrafchi.leddisplay.api.LedState;

/**
 * @author michelin
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class LedStateRow {
	private List<LedState> ledStates;

	int getColumnCount() {
		return ledStates.size();
	}

	LedState getStateAt(int columnIndex) {
		return ledStates.get(columnIndex);
	}
}