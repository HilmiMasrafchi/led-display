/**
 * 
 */
package me.hmasrafchi.leddisplay.model.view;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @author michelin
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({ @JsonSubTypes.Type(value = OverlayStationaryView.class, name = "stationary"),
		@JsonSubTypes.Type(value = OverlayRollHorizontallyView.class, name = "roll") })
public interface OverlayView {

}