/**
 * 
 */
package me.hmasrafchi.leddisplay.framework;

import lombok.Builder;
import lombok.Getter;

/**
 * @author michelin
 *
 */
@Builder
@Getter
public final class Configuration {
	private final int boardRowsCount;
	private final int boardColumnsCount;
}