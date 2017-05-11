/**
 * 
 */
package me.hmasrafchi.leddisplay.consumer.model.jpa;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

import lombok.Data;

/**
 * @author michelin
 *
 */
@Data
@Entity
public class FrameJpa {
	@Id
	@GeneratedValue
	private Integer id;

	@JoinColumn
	@OrderColumn
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<LedRowJpa> ledRows;

	public FrameJpa(final List<LedRowJpa> ledRows) {
		this.ledRows = ledRows;
	}

	FrameJpa() {

	}
}