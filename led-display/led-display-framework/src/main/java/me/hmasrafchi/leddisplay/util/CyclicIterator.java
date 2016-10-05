/**
 * 
 */
package me.hmasrafchi.leddisplay.util;

import java.util.Iterator;

/**
 * @author michelin
 *
 */
public final class CyclicIterator<T> {
	private final Iterable<T> iterable;

	private Iterator<T> iterator;

	public CyclicIterator(final Iterable<T> iterable) {
		this.iterable = iterable;
		this.iterator = iterable.iterator();
	}

	public T next() {
		if (iterator.hasNext()) {
			return iterator.next();
		} else {
			iterator = iterable.iterator();
			return iterator.next();
		}
	}
}