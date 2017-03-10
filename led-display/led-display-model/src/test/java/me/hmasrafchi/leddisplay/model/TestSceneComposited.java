/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import static java.util.Arrays.asList;
import static me.hmasrafchi.leddisplay.api.RgbColor.BLACK;
import static me.hmasrafchi.leddisplay.api.RgbColor.BLUE;
import static me.hmasrafchi.leddisplay.api.RgbColor.GREEN;
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
public final class TestSceneComposited {
	private static final int MATRIX_COLUMNS_COUNT = 5;
	private static final int MATRIX_ROWS_COUNT = 6;

	private static final List<List<LedState>> STATES_ROLL1 = asList( //
			asList(ON, ON, ON, ON, ON, ON, ON), //
			asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON), //
			asList(ON, ON, ON, ON, ON, ON, ON));

	private static final List<List<LedState>> STATES_ROLL2 = asList( //
			asList(ON, ON, ON), //
			asList(ON, OFF, ON), //
			asList(ON, ON, ON));

	private Matrix matrix;

	@Before
	public void init() {
		this.matrix = new MatrixDefault();
	}

	@Test
	public void ledsShouldBeOverlayed() {
		final Scene scene1 = new OverlayRollHorizontally(new TwoDimensionalListRectangular<>(STATES_ROLL1), GREEN, BLUE,
				MATRIX_COLUMNS_COUNT, 0);
		final Scene scene2 = new OverlayRollHorizontally(new TwoDimensionalListRectangular<>(STATES_ROLL2), GREEN, BLUE,
				MATRIX_COLUMNS_COUNT, 1);
		final Scene sceneComposited = new SceneComposited(asList(scene1, scene2));

		// scene 1
		final Frame frame1 = getExpectedFrame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame2 = getExpectedFrame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame3 = getExpectedFrame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(BLUE)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame4 = getExpectedFrame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(BLUE), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame5 = getExpectedFrame(asList( //
				asList(new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(GREEN), new Led(BLUE), new Led(GREEN), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame6 = getExpectedFrame(asList( //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(GREEN), new Led(BLUE), new Led(GREEN), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame7 = getExpectedFrame(asList( //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLUE), new Led(GREEN), new Led(BLACK), new Led(GREEN), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame8 = getExpectedFrame(asList( //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(GREEN), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame9 = getExpectedFrame(asList( //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(GREEN), new Led(BLACK), new Led(GREEN), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame10 = getExpectedFrame(asList( //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(GREEN), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame11 = getExpectedFrame(asList( //
				asList(new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame12 = getExpectedFrame(asList( //
				asList(new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame13 = getExpectedFrame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		// scene 2
		final Frame frame14 = getExpectedFrame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame15 = getExpectedFrame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame16 = getExpectedFrame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(BLUE)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame17 = getExpectedFrame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(BLUE), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame18 = getExpectedFrame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(GREEN), new Led(BLUE), new Led(GREEN), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame19 = getExpectedFrame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(BLUE), new Led(GREEN), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame20 = getExpectedFrame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLUE), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame21 = getExpectedFrame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame22 = getExpectedFrame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final CompiledFrames expectedCompiledFrames = new CompiledFrames(asList(frame1, frame2, frame3, frame4, frame5,
				frame6, frame7, frame8, frame9, frame10, frame11, frame12, frame13, frame14, frame15, frame16, frame17,
				frame18, frame19, frame20, frame21, frame22));

		final CompiledFrames actualCompiledFrames = matrix.compile(sceneComposited, MATRIX_ROWS_COUNT,
				MATRIX_COLUMNS_COUNT);

		assertThat(actualCompiledFrames, is(equalTo(expectedCompiledFrames)));
	}

	private Frame getExpectedFrame(final List<List<Led>> expectedLeds) {
		return new Frame(new TwoDimensionalListRectangular<>(expectedLeds));
	}
}