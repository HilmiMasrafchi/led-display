/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.model;

import static java.util.Arrays.asList;
import static me.hmasrafchi.leddisplay.api.RgbColor.BLACK;
import static me.hmasrafchi.leddisplay.api.RgbColor.BLUE;
import static me.hmasrafchi.leddisplay.api.RgbColor.GREEN;
import static me.hmasrafchi.leddisplay.api.RgbColor.RED;
import static me.hmasrafchi.leddisplay.api.RgbColor.YELLOW;
import static me.hmasrafchi.leddisplay.api.LedState.OFF;
import static me.hmasrafchi.leddisplay.api.LedState.ON;
import static me.hmasrafchi.leddisplay.api.LedState.TRANSPARENT;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import me.hmasrafchi.leddisplay.api.CompiledFrames;
import me.hmasrafchi.leddisplay.api.Frame;
import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.api.LedState;
import me.hmasrafchi.leddisplay.api.Matrix;
import me.hmasrafchi.leddisplay.api.Scene;
import me.hmasrafchi.leddisplay.util.TwoDimensionalListRectangular;

/**
 * @author michelin
 *
 */
public final class TestSceneOverlayed {
	private static final int MATRIX_COLUMNS_COUNT = 5;
	private static final int MATRIX_ROWS_COUNT = 6;

	private static final List<List<LedState>> STATES_ROLL = asList( //
			asList(ON, ON, ON, ON, ON, ON, ON), //
			asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON), //
			asList(ON, ON, ON, ON, ON, ON, ON));

	private static final List<List<LedState>> STATES_STATIONARY = asList( //
			asList(ON, ON, ON, ON, ON), //
			asList(TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT), //
			asList(ON, TRANSPARENT, TRANSPARENT, TRANSPARENT, ON), //
			asList(OFF, TRANSPARENT, TRANSPARENT, TRANSPARENT, OFF), //
			asList(ON, TRANSPARENT, TRANSPARENT, TRANSPARENT, ON), //
			asList(ON, ON, ON, ON, ON));

	private Matrix matrix;

	@Before
	public void init() {
		this.matrix = new MatrixDefault();
	}

	@Test
	public void ledsShouldBeOverlayed() {
		final Overlay overlayRoll = new OverlayRollHorizontally(new TwoDimensionalListRectangular<>(STATES_ROLL), GREEN,
				BLUE, MATRIX_COLUMNS_COUNT, 1);
		final Overlay overlayStationary = new OverlayStationary(new TwoDimensionalListRectangular<>(STATES_STATIONARY),
				RED, YELLOW);

		final Scene sceneOverlayed = new SceneOverlayed(asList(overlayRoll, overlayStationary));

		// 0nd frame
		final Frame frame0 = getExpectedFrame(
				asList(asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
						asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
						asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
						asList(new Led(YELLOW), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(YELLOW)), //
						asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
						asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 1nd frame
		final Frame frame1 = getExpectedFrame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(YELLOW), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 2rd frame
		final Frame frame2 = getExpectedFrame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(RED)), //
				asList(new Led(YELLOW), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 3th frame
		final Frame frame3 = getExpectedFrame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(BLACK), new Led(GREEN), new Led(BLUE), new Led(RED)), //
				asList(new Led(YELLOW), new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 4th frame
		final Frame frame4 = getExpectedFrame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(GREEN), new Led(BLUE), new Led(GREEN), new Led(RED)), //
				asList(new Led(YELLOW), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 5th frame
		final Frame frame5 = getExpectedFrame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(BLUE), new Led(GREEN), new Led(BLACK), new Led(RED)), //
				asList(new Led(YELLOW), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 6th frame
		final Frame frame6 = getExpectedFrame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(GREEN), new Led(BLACK), new Led(GREEN), new Led(RED)), //
				asList(new Led(YELLOW), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 7th frame
		final Frame frame7 = getExpectedFrame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(BLACK), new Led(GREEN), new Led(BLACK), new Led(RED)), //
				asList(new Led(YELLOW), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 8th frame
		final Frame frame8 = getExpectedFrame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK)), //
				asList(new Led(RED), new Led(GREEN), new Led(BLACK), new Led(GREEN), new Led(RED)), //
				asList(new Led(YELLOW), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 9th frame
		final Frame frame9 = getExpectedFrame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(RED), new Led(BLACK), new Led(GREEN), new Led(BLACK), new Led(RED)), //
				asList(new Led(YELLOW), new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 10th frame
		final Frame frame10 = getExpectedFrame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(RED), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(YELLOW), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 11th frame
		final Frame frame11 = getExpectedFrame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(YELLOW), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 12th frame
		final Frame frame12 = getExpectedFrame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(YELLOW), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		final CompiledFrames expectedCompiledFrames = new CompiledFrames(asList(frame0, frame1, frame2, frame3, frame4,
				frame5, frame6, frame7, frame8, frame9, frame10, frame11, frame12));

		final CompiledFrames actualCompiledFrames = matrix.compile(sceneOverlayed, MATRIX_ROWS_COUNT,
				MATRIX_COLUMNS_COUNT);
		assertThat(actualCompiledFrames, is(equalTo(expectedCompiledFrames)));
	}

	@Test
	public void ledsShouldBeOverlayedInReverseOrder() {
		final Overlay overlayRoll = new OverlayRollHorizontally(new TwoDimensionalListRectangular<>(STATES_ROLL), GREEN,
				BLUE, MATRIX_COLUMNS_COUNT, 1);
		final Overlay overlayStationary = new OverlayStationary(new TwoDimensionalListRectangular<>(STATES_STATIONARY),
				RED, YELLOW);

		final Scene sceneOverlayed = new SceneOverlayed(asList(overlayStationary, overlayRoll));

		// 0nd frame
		final Frame frame0 = getExpectedFrame(
				asList(asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
						asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
						asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
						asList(new Led(YELLOW), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(YELLOW)), //
						asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
						asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 1nd frame
		final Frame frame1 = getExpectedFrame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(YELLOW), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 2rd frame
		final Frame frame2 = getExpectedFrame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(BLUE)), //
				asList(new Led(YELLOW), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 3th frame
		final Frame frame3 = getExpectedFrame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(BLACK), new Led(GREEN), new Led(BLUE), new Led(GREEN)), //
				asList(new Led(YELLOW), new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 4th frame
		final Frame frame4 = getExpectedFrame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(GREEN), new Led(BLUE), new Led(GREEN), new Led(RED)), //
				asList(new Led(YELLOW), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 5th frame
		final Frame frame5 = getExpectedFrame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(GREEN), new Led(BLUE), new Led(GREEN), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 6th frame
		final Frame frame6 = getExpectedFrame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLUE), new Led(GREEN), new Led(BLACK), new Led(GREEN), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 7th frame
		final Frame frame7 = getExpectedFrame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(GREEN), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 8th frame
		final Frame frame8 = getExpectedFrame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK)), //
				asList(new Led(RED), new Led(GREEN), new Led(BLACK), new Led(GREEN), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 9th frame
		final Frame frame9 = getExpectedFrame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(GREEN), new Led(BLACK), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 10th frame
		final Frame frame10 = getExpectedFrame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(RED), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 11th frame
		final Frame frame11 = getExpectedFrame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 12th frame
		final Frame frame12 = getExpectedFrame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(YELLOW), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		final CompiledFrames expectedCompiledFrames = new CompiledFrames(asList(frame0, frame1, frame2, frame3, frame4,
				frame5, frame6, frame7, frame8, frame9, frame10, frame11, frame12));

		final CompiledFrames actualCompiledFrames = matrix.compile(sceneOverlayed, MATRIX_ROWS_COUNT,
				MATRIX_COLUMNS_COUNT);
		assertThat(actualCompiledFrames, is(equalTo(expectedCompiledFrames)));
	}

	private Frame getExpectedFrame(final List<List<Led>> expectedLeds) {
		return new Frame(new TwoDimensionalListRectangular<>(expectedLeds));
	}
}