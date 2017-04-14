/**
 * 
 */
package me.hmasrafchi.leddisplay.consumer;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author michelin
 *
 */
@Data
@Entity
@ToString
@EqualsAndHashCode
public class FrameRow {
	@JsonIgnore
	@Id
	@GeneratedValue
	private int id;

	@ElementCollection
	private List<Led> data;

	public FrameRow() {
	}

	public FrameRow(List<Led> data) {
		this.data = data;
	}
}