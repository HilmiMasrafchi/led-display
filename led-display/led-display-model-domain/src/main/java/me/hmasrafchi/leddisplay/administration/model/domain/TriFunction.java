/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.model.domain;

/**
 * @author michelin
 *
 */
@FunctionalInterface
public interface TriFunction<T, U, V, R> {
	R accept(T t, U u, V v);
}