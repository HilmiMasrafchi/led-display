/**
 * 
 */
package me.hmasrafchi.leddisplay.consumer.model.jpa;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

/**
 * @author michelin
 *
 */
@Data
@Entity
public class LedRowJpa {
	@Id
	@GeneratedValue
	private Integer id;

	@ElementCollection
	private List<LedJpa> leds;

	public LedRowJpa(final List<LedJpa> leds) {
		this.leds = leds;
	}

	LedRowJpa() {
	}
}