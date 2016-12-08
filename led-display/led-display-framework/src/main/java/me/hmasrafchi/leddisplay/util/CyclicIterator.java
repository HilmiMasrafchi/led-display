/**
 * 
 */
package me.hmasrafchi.leddisplay.util;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author michelin
 *
 */
public final class CyclicIterator<T> {
	private final Collection<? extends T> iterable;

	private Iterator<? extends T> iterator;

	public CyclicIterator(final Collection<? extends T> iterable) {
		this.iterable = iterable;
		this.iterator = iterable.iterator();
	}

	public T next() {
		if (iterable.isEmpty()) {
			return null;
		} else {
			if (iterator.hasNext()) {
				return iterator.next();
			} else {
				iterator = iterable.iterator();
				return iterator.next();
			}
		}
	}
}