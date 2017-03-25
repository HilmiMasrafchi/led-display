/**
 * 
 */
package me.hmasrafchi.leddisplay.rest.persist;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author michelin
 *
 */
@Entity
@Inheritance
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, visible = true)
@JsonSubTypes({ @JsonSubTypes.Type(value = OverlayRollHorizontally.class, name = "roll"),
		@JsonSubTypes.Type(value = OverlayStationary.class, name = "still") })
@Data
@EqualsAndHashCode(exclude = "id")
public abstract class Overlay {
	@Id
	@GeneratedValue
	Integer id;
}