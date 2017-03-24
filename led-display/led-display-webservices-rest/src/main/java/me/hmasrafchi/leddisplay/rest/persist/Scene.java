/**
 * 
 */
package me.hmasrafchi.leddisplay.rest.persist;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.Data;

/**
 * @author michelin
 *
 */
@Data
@Entity
public class Scene {
	@Id
	@GeneratedValue
	Integer id;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn
	List<Overlay> overlays;

	public List<Overlay> getOverlays() {
		if (overlays == null) {
			this.overlays = new ArrayList<>();
		}

		return this.overlays;
	}
}