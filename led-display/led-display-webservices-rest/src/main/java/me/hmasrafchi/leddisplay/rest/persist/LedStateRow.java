/**
 * 
 */
package me.hmasrafchi.leddisplay.rest.persist;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

/**
 * @author michelin
 *
 */
@Data
@Entity
public class LedStateRow {
	@Id
	@GeneratedValue
	Integer id;

	@ElementCollection
	@Enumerated(EnumType.STRING)
	List<LedState> ledStates;
}