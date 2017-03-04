/**
 * 
 */
package me.hmasrafchi.leddisplay.rest.persist;

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
public class LedRgbColor {
	@Id
	@GeneratedValue
	Integer id;

	int r;
	int g;
	int b;
}