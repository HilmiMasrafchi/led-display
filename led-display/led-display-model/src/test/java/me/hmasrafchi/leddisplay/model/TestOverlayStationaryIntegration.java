/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import static java.util.Arrays.asList;
import static me.hmasrafchi.leddisplay.api.RgbColor.RED;
import static me.hmasrafchi.leddisplay.api.RgbColor.YELLOW;
import static me.hmasrafchi.leddisplay.api.LedState.OFF;
import static me.hmasrafchi.leddisplay.api.LedState.ON;
import static me.hmasrafchi.leddisplay.api.LedState.TRANSPARENT;
import static me.hmasrafchi.leddisplay.api.LedState.UNRECOGNIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import me.hmasrafchi.leddisplay.api.CompiledFrames;
import me.hmasrafchi.leddisplay.api.Frame;
import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.api.RgbColor;
import me.hmasrafchi.leddisplay.api.LedState;
import me.hmasrafchi.leddisplay.api.Matrix;
import me.hmasrafchi.leddisplay.util.TwoDimensionalListRectangular;

/**
 * @author michelin
 *
 */
public final class TestOverlayStationaryIntegration {
	private static final TwoDimensionalListRectangular<LedState> STATES = new TwoDimensionalListRectangular<>(asList( //
			asList(ON, ON, ON, ON, ON), //
			asList(TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT), //
			asList(ON, TRANSPARENT, TRANSPARENT, TRANSPARENT, ON), //
			asList(OFF, TRANSPARENT, TRANSPARENT, TRANSPARENT, OFF), //
			asList(ON, TRANSPARENT, TRANSPARENT, TRANSPARENT, ON), //
			asList(ON, ON, ON, ON, ON), //
			asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED)));

	private static final RgbColor ON_COLOR = RED;
	private static final RgbColor OFF_COLOR = YELLOW;

	private Overlay overlayStationary;

	@Before
	public void init() {
		this.overlayStationary = new OverlayStationary(STATES, ON_COLOR, OFF_COLOR);
	}

	@Test
	public void ledsShouldStayStationary() {
		final List<List<Led>> expectedLedStates = asList(
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)),
				asList(new Led(), new Led(), new Led(), new Led(), new Led()),
				asList(new Led(ON_COLOR), new Led(), new Led(), new Led(), new Led(ON_COLOR)),
				asList(new Led(OFF_COLOR), new Led(), new Led(), new Led(), new Led(OFF_COLOR)),
				asList(new Led(ON_COLOR), new Led(), new Led(), new Led(), new Led(ON_COLOR)),
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)),
				asList(new Led(), new Led(), new Led(), new Led(), new Led()));
		final CompiledFrames expectedCompiledFrames = getExpectedCompiledFrames(expectedLedStates);
		final Matrix matrix = new MatrixDefault();

		final CompiledFrames actualCompiledFrames = matrix.compile(overlayStationary, 7, 5);

		assertThat(actualCompiledFrames, is(equalTo(expectedCompiledFrames)));
	}

	@Test
	public void ledsShouldStayStationaryEvenIfMatrixDimensionIsSmallerThanStates() {
		final List<List<Led>> expectedLedStates = asList(
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)),
				asList(new Led(), new Led(), new Led(), new Led(), new Led()));
		final CompiledFrames expectedCompiledFrames = getExpectedCompiledFrames(expectedLedStates);
		final Matrix matrix = new MatrixDefault();

		final CompiledFrames actualCompiledFrames = matrix.compile(overlayStationary, 2, 5);

		assertThat(actualCompiledFrames, is(equalTo(expectedCompiledFrames)));
	}

	@Test
	public void ledsShouldStayStationaryEvenIfMatrixDimensionIsBiggerThanStates() {
		final List<List<Led>> expectedLedStates = asList(
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR),
						new Led()),
				asList(new Led(), new Led(), new Led(), new Led(), new Led(), new Led()),
				asList(new Led(ON_COLOR), new Led(), new Led(), new Led(), new Led(ON_COLOR), new Led()),
				asList(new Led(OFF_COLOR), new Led(), new Led(), new Led(), new Led(OFF_COLOR), new Led()),
				asList(new Led(ON_COLOR), new Led(), new Led(), new Led(), new Led(ON_COLOR), new Led()),
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR),
						new Led()),
				asList(new Led(), new Led(), new Led(), new Led(), new Led(), new Led()),
				asList(new Led(), new Led(), new Led(), new Led(), new Led(), new Led()));
		final CompiledFrames expectedCompiledFrames = getExpectedCompiledFrames(expectedLedStates);
		final Matrix matrix = new MatrixDefault();

		final CompiledFrames actualCompiledFrames = matrix.compile(overlayStationary, 8, 6);

		assertThat(actualCompiledFrames, is(equalTo(expectedCompiledFrames)));
	}

	private CompiledFrames getExpectedCompiledFrames(final List<List<Led>> expectedLedStates) {
		final Frame frame = new Frame(new TwoDimensionalListRectangular<>(expectedLedStates));
		return new CompiledFrames(asList(frame));
	}
}