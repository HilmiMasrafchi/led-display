/**
 * 
 */
package me.hmasrafchi.leddisplay.util;

/**
 * @author michelin
 *
 */
final class ModularArithmetic {
	private final int modulo;

	private int currentValue = -1;

	public ModularArithmetic(final int modulo) {
		this.modulo = modulo;
	}

	public int count() {
		return ++currentValue % modulo;
	}
}