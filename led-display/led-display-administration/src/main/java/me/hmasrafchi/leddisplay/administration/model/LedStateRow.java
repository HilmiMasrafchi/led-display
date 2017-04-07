/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.model;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.hmasrafchi.leddisplay.administration.model.Led.State;

/**
 * @author michelin
 *
 */
@Data
@Entity
@EqualsAndHashCode(exclude = "id")
// TODO: make inner class of LedState
public class LedStateRow {
	@Id
	@GeneratedValue
	private Integer id;

	@ElementCollection
	@Enumerated(EnumType.STRING)
	@NotNull
	@Size(min = 1)
	private List<Led.State> ledStates;

	public int getColumnCount() {
		return ledStates.size();
	}

	public Led.State getStateAt(int columnIndex) {
		return ledStates.get(columnIndex);
	}

	LedStateRow(final List<State> ledStates) {
		this.ledStates = ledStates;
	}

	LedStateRow() {
	}
}