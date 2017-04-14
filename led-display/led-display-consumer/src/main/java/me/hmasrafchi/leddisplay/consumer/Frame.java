/**
 * 
 */
package me.hmasrafchi.leddisplay.consumer;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

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
public class Frame {
	@Id
	@GeneratedValue
	private int id;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn
	@OrderColumn
	private List<FrameRow> frameData;

	public Frame() {
	}

	public Frame(List<FrameRow> frameData) {
		this.frameData = frameData;
	}
}