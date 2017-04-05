/**
 * 
 */
package me.hmasrafchi.leddisplay.administration;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author michelin
 *
 */
@Data
@Entity
@EqualsAndHashCode(exclude = "id")
class LedStateRow {
	@Id
	@GeneratedValue
	private Integer id;

	@ElementCollection
	@Enumerated(EnumType.STRING)
	private List<LedState> ledStates;

	int getColumnCount() {
		return ledStates.size();
	}

	LedState getStateAt(int columnIndex) {
		return ledStates.get(columnIndex);
	}
}