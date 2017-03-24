/**
 * 
 */
package me.hmasrafchi.leddisplay.rest.persist;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

/**
 * @author michelin
 *
 */
@Data
@Entity
public class LedStateRow {
	@Id
	@GeneratedValue
	Integer id;

	@ElementCollection(fetch = FetchType.EAGER)
	@Enumerated(EnumType.STRING)
	List<LedState> ledStates;

	public LedStateRow() {
	}

	public LedStateRow(final List<LedState> ledStates) {
		this.ledStates = ledStates;
	}
}