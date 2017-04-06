/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.model;

import static java.util.Arrays.asList;
import static me.hmasrafchi.leddisplay.api.RgbColor.RED;
import static me.hmasrafchi.leddisplay.api.RgbColor.YELLOW;
import static me.hmasrafchi.leddisplay.api.LedState.OFF;
import static me.hmasrafchi.leddisplay.api.LedState.ON;
import static me.hmasrafchi.leddisplay.api.LedState.TRANSPARENT;
import static me.hmasrafchi.leddisplay.api.LedState.UNRECOGNIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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
public final class TestOverlayRollHorizontallyIntegration {
	private static final TwoDimensionalListRectangular<LedState> STATES = new TwoDimensionalListRectangular<>(asList( //
			asList(ON, ON, ON, ON, ON, ON, ON), //
			asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON), //
			asList(ON, ON, ON, ON, ON, ON, ON), //
			asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED)));

	@Rule
	public final ExpectedException expectedException = ExpectedException.none();

	private static final RgbColor ON_COLOR = RED;
	private static final RgbColor OFF_COLOR = YELLOW;

	private Overlay overlayRollHorizontally;

	@Before
	public void init() {
		this.overlayRollHorizontally = new OverlayRollHorizontally(STATES, ON_COLOR, OFF_COLOR, 3, 0);
	}

	@Test
	public void ledsShouldRollHorizontally() {
		final Frame frame1 = getExpectedFrame(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame2 = getExpectedFrame(asList( //
				asList(new Led(), new Led(), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame3 = getExpectedFrame(asList( //
				asList(new Led(), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(ON_COLOR), new Led(OFF_COLOR)), //
				asList(new Led(), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame4 = getExpectedFrame(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(OFF_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame5 = getExpectedFrame(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(OFF_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame6 = getExpectedFrame(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame7 = getExpectedFrame(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame8 = getExpectedFrame(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame9 = getExpectedFrame(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame10 = getExpectedFrame(asList( //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame11 = getExpectedFrame(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final CompiledFrames expectedCompiledFrames = new CompiledFrames(
				asList(frame1, frame2, frame3, frame4, frame5, frame6, frame7, frame8, frame9, frame10, frame11));

		final Matrix matrix = new MatrixDefault();
		final CompiledFrames actualCompile = matrix.compile(overlayRollHorizontally, 6, 3);

		Assert.assertThat(actualCompile, is(equalTo(expectedCompiledFrames)));
	}

	private Frame getExpectedFrame(final List<List<Led>> expectedLeds) {
		return new Frame(new TwoDimensionalListRectangular<>(expectedLeds));
	}

	@Test
	public void ledsShouldRollHorizontallyWithYPositionPositive() {
		final Frame frame1 = getExpectedFrame(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame2 = getExpectedFrame(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame3 = getExpectedFrame(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(ON_COLOR), new Led(OFF_COLOR)), //
				asList(new Led(), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame4 = getExpectedFrame(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(OFF_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame5 = getExpectedFrame(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(OFF_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame6 = getExpectedFrame(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame7 = getExpectedFrame(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame8 = getExpectedFrame(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame9 = getExpectedFrame(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame10 = getExpectedFrame(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame11 = getExpectedFrame(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final CompiledFrames expectedCompiledFrames = new CompiledFrames(
				asList(frame1, frame2, frame3, frame4, frame5, frame6, frame7, frame8, frame9, frame10, frame11));

		final Matrix matrix = new MatrixDefault();
		this.overlayRollHorizontally = new OverlayRollHorizontally(STATES, ON_COLOR, OFF_COLOR, 3, 1);
		final CompiledFrames actualCompile = matrix.compile(overlayRollHorizontally, 6, 3);

		Assert.assertThat(actualCompile, is(equalTo(expectedCompiledFrames)));
	}

	@Test
	public void ledsShouldRollHorizontallyWithYPositionNegative() {
		final Frame frame1 = getExpectedFrame(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame2 = getExpectedFrame(asList( //
				asList(new Led(), new Led(), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame3 = getExpectedFrame(asList( //
				asList(new Led(), new Led(ON_COLOR), new Led(OFF_COLOR)), //
				asList(new Led(), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame4 = getExpectedFrame(asList( //
				asList(new Led(ON_COLOR), new Led(OFF_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame5 = getExpectedFrame(asList( //
				asList(new Led(OFF_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame6 = getExpectedFrame(asList( //
				asList(new Led(ON_COLOR), new Led(), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame7 = getExpectedFrame(asList( //
				asList(new Led(), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame8 = getExpectedFrame(asList( //
				asList(new Led(ON_COLOR), new Led(), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame9 = getExpectedFrame(asList( //
				asList(new Led(), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame10 = getExpectedFrame(asList( //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame11 = getExpectedFrame(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final CompiledFrames expectedCompiledFrames = new CompiledFrames(
				asList(frame1, frame2, frame3, frame4, frame5, frame6, frame7, frame8, frame9, frame10, frame11));

		final Matrix matrix = new MatrixDefault();
		this.overlayRollHorizontally = new OverlayRollHorizontally(STATES, ON_COLOR, OFF_COLOR, 3, -1);
		final CompiledFrames actualCompile = matrix.compile(overlayRollHorizontally, 6, 3);

		Assert.assertThat(actualCompile, is(equalTo(expectedCompiledFrames)));
	}

	@Test
	public void ledsShouldRollHorizontallyWithBeginMarkBiggerThanMatrixWidth() {
		final Frame frame0 = getExpectedFrame(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame1 = getExpectedFrame(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame2 = getExpectedFrame(asList( //
				asList(new Led(), new Led(), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame3 = getExpectedFrame(asList( //
				asList(new Led(), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(ON_COLOR), new Led(OFF_COLOR)), //
				asList(new Led(), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame4 = getExpectedFrame(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(OFF_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame5 = getExpectedFrame(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(OFF_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame6 = getExpectedFrame(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame7 = getExpectedFrame(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame8 = getExpectedFrame(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame9 = getExpectedFrame(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame10 = getExpectedFrame(asList( //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame11 = getExpectedFrame(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final CompiledFrames expectedCompiledFrames = new CompiledFrames(asList(frame0, frame1, frame2, frame3, frame4,
				frame5, frame6, frame7, frame8, frame9, frame10, frame11));

		final Matrix matrix = new MatrixDefault();
		this.overlayRollHorizontally = new OverlayRollHorizontally(STATES, ON_COLOR, OFF_COLOR, 4, 0);
		final CompiledFrames actualCompile = matrix.compile(overlayRollHorizontally, 6, 3);

		Assert.assertThat(actualCompile, is(equalTo(expectedCompiledFrames)));
	}

	@Test
	public void ledsShouldRollHorizontallyWithBeginMarkOfZero() {
		final Frame frame1 = getExpectedFrame(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(OFF_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame2 = getExpectedFrame(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(OFF_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame3 = getExpectedFrame(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame4 = getExpectedFrame(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame5 = getExpectedFrame(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame6 = getExpectedFrame(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame7 = getExpectedFrame(asList( //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame8 = getExpectedFrame(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final CompiledFrames expectedCompiledFrames = new CompiledFrames(
				asList(frame1, frame2, frame3, frame4, frame5, frame6, frame7, frame8));

		final Matrix matrix = new MatrixDefault();
		this.overlayRollHorizontally = new OverlayRollHorizontally(STATES, ON_COLOR, OFF_COLOR, 0, 0);
		final CompiledFrames actualCompile = matrix.compile(overlayRollHorizontally, 6, 3);

		Assert.assertThat(actualCompile, is(equalTo(expectedCompiledFrames)));
	}
}