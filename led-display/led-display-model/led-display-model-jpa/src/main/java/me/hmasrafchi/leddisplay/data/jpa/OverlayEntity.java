/**
 * 
 */
package me.hmasrafchi.leddisplay.data.jpa;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;

import lombok.Data;

/**
 * @author michelin
 *
 */
@Data
@Entity
@Inheritance
public abstract class OverlayEntity {
	@Id
	@GeneratedValue
	protected BigInteger id;
}