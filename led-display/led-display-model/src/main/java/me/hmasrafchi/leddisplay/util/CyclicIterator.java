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
	private final Collection<? extends T> collection;

	private Iterator<? extends T> iterator;

	private T currentElement;

	public CyclicIterator(final Collection<? extends T> collection) {
		Preconditions.checkNotNull(collection);
		Preconditions.checkArgument(!collection.isEmpty());

		this.collection = collection;
		this.iterator = collection.iterator();
		this.currentElement = iterator.next();
	}

	public T next() {
		if (!iterator.hasNext()) {
			iterator = collection.iterator();
		}
		this.currentElement = iterator.next();
		return this.currentElement;
	}

	public T getCurrentElement() {
		return this.currentElement;
	}
}