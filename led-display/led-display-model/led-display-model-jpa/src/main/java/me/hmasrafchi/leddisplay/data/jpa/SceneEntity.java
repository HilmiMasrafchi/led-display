/**
 * 
 */
package me.hmasrafchi.leddisplay.data.jpa;

import java.math.BigInteger;
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
public class SceneEntity {
	@Id
	@GeneratedValue
	private BigInteger id;

	@OrderColumn
	@JoinColumn
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<OverlayEntity> overlays;

	SceneEntity() {
	}

	public SceneEntity(final List<OverlayEntity> overlays) {
		this.overlays = overlays;
	}
}