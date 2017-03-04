/**
 * 
 */
package me.hmasrafchi.leddisplay.rest.persist;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;

/**
 * @author michelin
 *
 */
@Entity
@Inheritance
public abstract class Overlay {
	@Id
	@GeneratedValue
	Integer id;
}