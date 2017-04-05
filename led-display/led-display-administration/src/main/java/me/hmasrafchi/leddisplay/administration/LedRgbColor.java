/**
 * 
 */
package me.hmasrafchi.leddisplay.administration;

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
class LedRgbColor {
	@Id
	@GeneratedValue
	private Integer id;

	private int r;
	private int g;
	private int b;
}