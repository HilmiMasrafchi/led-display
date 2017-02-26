/**
 * 
 */
package me.hmasrafchi.leddisplay.rest.persist;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import lombok.Data;
import me.hmasrafchi.leddisplay.api.Led;

/**
 * @author michelin
 *
 */
@Data
@Entity
@XmlRootElement
public class SceneFrames {
	@Id
	int id;

	@ManyToOne
	MatrixEntity ownerMatrix;

	List<List<List<Led>>> frames;

	@XmlTransient
	public MatrixEntity getOwnerMatrix() {
		return ownerMatrix;
	}
}