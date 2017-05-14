/**
 * 
 */
package me.hmasrafchi.leddisplay.model.domain;

import static java.util.Objects.requireNonNull;

/**
 * @author michelin
 *
 */
// TODO: refactor the code to populate constraint violation instead of
// immeadiately throwing an exception - use builder pattern
public final class Preconditions {
	public static <T> T checkNotNull(final T object) {
		return requireNonNull(object, "parameter can not be null");
	}

	public static <T> T checkNotNull(final T object, final String message) {
		return requireNonNull(object, message);
	}

	public static String checkStringNotBlank(final String string) {
		checkNotNull(string);
		if (string.trim().isEmpty()) {
			throw new DomainConstraintViolationException("parameter can not be null, empty or blank");
		}

		return string;
	}

	public static <T> T checkIf(final boolean argument, final T object) {
		return checkIf(object, argument, "");
	}

	public static <T> T checkIf(final T object, final boolean argument, final String errorMessage) {
		if (argument) {
			return object;
		}

		throw new DomainConstraintViolationException(errorMessage);
	}

	public static String checkStringNotBlank(final String string, final String message) {
		checkNotNull(string);
		if (string.trim().isEmpty()) {
			throw new DomainConstraintViolationException(message);
		}

		return string;
	}

	public static void checkArgument(final boolean argument) {
		checkArgument(argument, "");
	}

	public static void checkArgument(final boolean argument, final String message) {
		if (!argument) {
			throw new DomainConstraintViolationException(message);
		}
	}
}