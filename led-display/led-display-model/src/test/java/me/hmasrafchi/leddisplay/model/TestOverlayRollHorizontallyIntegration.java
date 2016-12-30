/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import static java.util.Arrays.asList;
import static me.hmasrafchi.leddisplay.model.Overlay.State.OFF;
import static me.hmasrafchi.leddisplay.model.Overlay.State.ON;
import static me.hmasrafchi.leddisplay.model.Overlay.State.TRANSPARENT;
import static me.hmasrafchi.leddisplay.model.Overlay.State.UNRECOGNIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import me.hmasrafchi.leddisplay.model.Led.RgbColor;
import me.hmasrafchi.leddisplay.model.Overlay.State;
import me.hmasrafchi.leddisplay.util.TwoDimensionalListRectangular;

/**
 * @author michelin
 *
 */
public final class TestOverlayRollHorizontallyIntegration {
	private static final TwoDimensionalListRectangular<State> STATES = new TwoDimensionalListRectangular<>(asList( //
			asList(ON, ON, ON, ON, ON, ON, ON), //
			asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON), //
			asList(ON, ON, ON, ON, ON, ON, ON), //
			asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED)));

	@Rule
	public final ExpectedException expectedException = ExpectedException.none();

	private static final RgbColor ON_COLOR = RgbColor.RED;
	private static final RgbColor OFF_COLOR = RgbColor.YELLOW;

	private Overlay overlayRollHorizontally;

	@Before
	public void init() {
		this.overlayRollHorizontally = new OverlayRollHorizontally(STATES, ON_COLOR, OFF_COLOR, 3, 0);
	}

	@Test
	public void ledsShouldRollHorizontally() {
		final TwoDimensionalListRectangular<Led> frame1 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame2 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(), new Led(), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame3 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(ON_COLOR), new Led(OFF_COLOR)), //
				asList(new Led(), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame4 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(OFF_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame5 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(OFF_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame6 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame7 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame8 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame9 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame10 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame11 = new TwoDimensionalListRectangular<>(asList( //
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

	@Test
	public void ledsShouldRollHorizontallyWithYPositionPositive() {
		final TwoDimensionalListRectangular<Led> frame1 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame2 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame3 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(ON_COLOR), new Led(OFF_COLOR)), //
				asList(new Led(), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame4 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(OFF_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame5 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(OFF_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame6 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame7 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame8 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame9 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame10 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame11 = new TwoDimensionalListRectangular<>(asList( //
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
		final TwoDimensionalListRectangular<Led> frame1 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame2 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(), new Led(), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame3 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(), new Led(ON_COLOR), new Led(OFF_COLOR)), //
				asList(new Led(), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame4 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(ON_COLOR), new Led(OFF_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame5 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(OFF_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame6 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(ON_COLOR), new Led(), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame7 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame8 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(ON_COLOR), new Led(), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame9 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame10 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame11 = new TwoDimensionalListRectangular<>(asList( //
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
		final TwoDimensionalListRectangular<Led> frame0 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame1 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame2 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(), new Led(), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame3 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(ON_COLOR), new Led(OFF_COLOR)), //
				asList(new Led(), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame4 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(OFF_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame5 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(OFF_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame6 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame7 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame8 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame9 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame10 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame11 = new TwoDimensionalListRectangular<>(asList( //
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
		final TwoDimensionalListRectangular<Led> frame1 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(OFF_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame2 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(OFF_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame3 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame4 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame5 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame6 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame7 = new TwoDimensionalListRectangular<>(asList( //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final TwoDimensionalListRectangular<Led> frame8 = new TwoDimensionalListRectangular<>(asList( //
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