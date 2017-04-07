/**
 * 
 */
package test.model;

import javax.persistence.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author michelin
 *
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@ToString
public class OverlayStationary extends Overlay {
	public OverlayStationary() {
	}
}