/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

import me.hmasrafchi.leddisplay.api.LedState;
import me.hmasrafchi.leddisplay.api.Scene;

/**
 * @author michelin
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.WRAPPER_OBJECT, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = OverlayStationary.class, name = "overlayStationary"),
		@JsonSubTypes.Type(value = OverlayRollHorizontally.class, name = "overlayRollHorizontally") })
public interface Overlay extends Scene {
	LedState getStateAt(int rowIndex, int columnIndex);
}