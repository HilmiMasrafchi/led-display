/**
 * 
 */
package me.hmasrafchi.leddisplay.consumer.data.jpa;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
public class LedRowJpa {
	@Id
	@GeneratedValue
	private BigInteger id;

	@OrderColumn
	@ElementCollection(fetch = FetchType.EAGER)
	private List<LedJpa> leds;

	public LedRowJpa(final List<LedJpa> leds) {
		this.leds = leds;
	}

	LedRowJpa() {
	}
}