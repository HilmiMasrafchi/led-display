/**
 * 
 */
package me.hmasrafchi.leddisplay.administration;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;

/**
 * @author michelin
 *
 */
@Entity
@Inheritance
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, visible = true)
@JsonSubTypes({ @JsonSubTypes.Type(value = SceneRollHorizontally.class, name = "roll"),
		@JsonSubTypes.Type(value = SceneRandomColor.class, name = "random"),
		@JsonSubTypes.Type(value = SceneComposite.class, name = "composite") })
class Scene {
	@Id
	@GeneratedValue
	private Integer id;
}