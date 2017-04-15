/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.model.view;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @author michelin
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({ @JsonSubTypes.Type(value = OverlayStationary.class, name = "stationary"),
		@JsonSubTypes.Type(value = OverlayRollHorizontally.class, name = "roll") })
public interface Overlay {

}