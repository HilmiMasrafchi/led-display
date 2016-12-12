/**
 * 
 */
package me.hmasrafchi.leddisplay.jfx;

import lombok.Builder;
import lombok.Getter;

/**
 * @author michelin
 *
 */
@Getter
@Builder
public final class Configuration {
	private final int matrixRowsCount;
	private final int matrixColumnsCount;

	private final int canvasYPosition;

	private final int delayBetweenFrames;

	private final double ledTextFontSize;

	private final int matrixLedHorizontalGap;
	private final int matrixLedVerticalGap;
}