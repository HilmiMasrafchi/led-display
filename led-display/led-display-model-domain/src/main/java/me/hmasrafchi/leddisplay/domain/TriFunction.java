/**
 * 
 */
package me.hmasrafchi.leddisplay.domain;

/**
 * @author michelin
 *
 */
@FunctionalInterface
public interface TriFunction<T, U, V, R> {
	R accept(T t, U u, V v);
}