/**
 * 
 */
package me.hmasrafchi.leddisplay.model.domain;

/**
 * @author michelin
 *
 */
public final class DomainConstraintViolationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public DomainConstraintViolationException(final String message) {
		super(message);
	}

	public DomainConstraintViolationException(final String message, final Throwable throwable) {
		super(message, throwable);
	}
}