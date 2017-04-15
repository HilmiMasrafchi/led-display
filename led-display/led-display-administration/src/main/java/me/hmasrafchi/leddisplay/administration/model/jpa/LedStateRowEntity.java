/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.model.jpa;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OrderColumn;

import lombok.Data;

/**
 * @author michelin
 *
 */
@Data
@Entity
// TODO: make inner class of LedState
public class LedStateRowEntity {
	@Id
	@GeneratedValue
	private Integer id;

	@ElementCollection
	@Enumerated(EnumType.STRING)
	@OrderColumn
	private List<LedStateEntity> ledStates;
}