/**
 * 
 */
package me.hmasrafchi.leddisplay.consumer.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @author michelin
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, visible = true)
@JsonSubTypes({ @JsonSubTypes.Type(value = SceneRollHorizontally.class, name = "roll"),
		@JsonSubTypes.Type(value = SceneComposite.class, name = "composite") })
interface Scene {
	Led onLedVisited(Led led, int ledRowIndex, int ledColumnIndex);

	void onMatrixIterationEnded();

	boolean isExhausted();
}