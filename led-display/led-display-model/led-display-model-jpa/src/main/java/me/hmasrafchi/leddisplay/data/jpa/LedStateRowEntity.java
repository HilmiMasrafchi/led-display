/**
 * 
 */
package me.hmasrafchi.leddisplay.data.jpa;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OrderColumn;

import lombok.Data;

/**
 * @author michelin
 *
 */
@Data
@Entity
public class LedStateRowEntity {
	@Id
	@GeneratedValue
	private Integer id;

	@OrderColumn
	@ElementCollection
	private List<LedStateEntity> ledStates;

	LedStateRowEntity() {
	}

	public LedStateRowEntity(final List<LedStateEntity> ledStates) {
		this.ledStates = ledStates;
	}
}