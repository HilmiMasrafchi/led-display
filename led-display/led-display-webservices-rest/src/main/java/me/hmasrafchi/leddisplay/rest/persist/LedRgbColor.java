/**
 * 
 */
package me.hmasrafchi.leddisplay.rest.persist;

import javax.persistence.Entity;
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
public class LedRgbColor {
	@Id
	@GeneratedValue
	Integer id;

	int r;
	int g;
	int b;

	public LedRgbColor() {
	}

	public LedRgbColor(final int r, final int g, final int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
}