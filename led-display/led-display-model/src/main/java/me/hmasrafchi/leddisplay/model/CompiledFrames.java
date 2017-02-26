/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import java.util.List;
import java.util.ListIterator;

import lombok.Value;
import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.util.TwoDimensionalListRectangular;

/**
 * @author michelin
 *
 */
@Value
public final class CompiledFrames {
	private final List<TwoDimensionalListRectangular<Led>> frames;

	public ListIterator<TwoDimensionalListRectangular<Led>> listIterator() {
		return frames.listIterator();
	}
}