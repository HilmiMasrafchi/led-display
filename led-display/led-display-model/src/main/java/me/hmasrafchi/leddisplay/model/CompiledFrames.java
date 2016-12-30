/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import java.util.List;
import java.util.ListIterator;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.hmasrafchi.leddisplay.util.TwoDimensionalListRectangular;

/**
 * @author michelin
 *
 */
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public final class CompiledFrames {
	private final List<TwoDimensionalListRectangular<Led>> frames;

	public ListIterator<TwoDimensionalListRectangular<Led>> listIterator() {
		return frames.listIterator();
	}
}