/**
 * 
 */
package me.hmasrafchi.leddisplay.consumer;

import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author michelin
 *
 */
@Data
@ToString
@Embeddable
@EqualsAndHashCode
public class Led {
	private String text;
	private RgbColor rgbColor;
}