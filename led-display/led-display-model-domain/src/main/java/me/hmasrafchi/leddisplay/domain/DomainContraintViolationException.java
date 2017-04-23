/**
 * 
 */
package me.hmasrafchi.leddisplay.domain;

/**
 * @author michelin
 *
 */
public final class DomainContraintViolationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public DomainContraintViolationException(final String message) {
		super(message);
	}
}