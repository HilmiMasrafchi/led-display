/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.model;

import static java.util.Arrays.asList;
import static me.hmasrafchi.leddisplay.administration.model.Led.State.OFF;
import static me.hmasrafchi.leddisplay.administration.model.Led.State.ON;
import static me.hmasrafchi.leddisplay.administration.model.Led.State.TRANSPARENT;
import static me.hmasrafchi.leddisplay.administration.model.Led.State.UNRECOGNIZED;
import static me.hmasrafchi.leddisplay.administration.model.RgbColor.RED;
import static me.hmasrafchi.leddisplay.administration.model.RgbColor.YELLOW;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import me.hmasrafchi.leddisplay.administration.CompiledFrames;
import me.hmasrafchi.leddisplay.administration.Frame;

/**
 * @author michelin
 *
 */
@RunWith(MockitoJUnitRunner.class)
public final class TestOverlayRollHorizontallyUnit {
	private static final List<LedStateRow> STATES = Arrays.asList( //
			new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
			new LedStateRow(asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON)), //
			new LedStateRow(asList(ON, ON, ON, ON, ON, ON, ON)), //
			new LedStateRow(asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
					UNRECOGNIZED)));

	@Rule
	public final ExpectedException expectedException = ExpectedException.none();

	@Mock
	private Led led;

	private static final RgbColor ON_COLOR = RED;
	private static final RgbColor OFF_COLOR = YELLOW;

	private Overlay overlayRollHorizontally;

	@Before
	public void init() {
		this.overlayRollHorizontally = new OverlayRollHorizontally(STATES, ON_COLOR, OFF_COLOR, 5, 0);
	}

	@Test
	public void constructor_shouldThrowNullPointExceptionIfStatesAreNull() {
		expectedException.expect(NullPointerException.class);
		this.overlayRollHorizontally = new OverlayRollHorizontally(null, ON_COLOR, OFF_COLOR, 5, 1);
	}

	@Test
	public void constructor_shouldThrowNullPointerExceptionIfOnColorIsNull() {
		expectedException.expect(NullPointerException.class);
		this.overlayRollHorizontally = new OverlayRollHorizontally(STATES, null, OFF_COLOR, 5, 1);
	}

	@Test
	public void constructor_shouldThrowNullPointerExceptionIfOffColorIsNull() {
		expectedException.expect(NullPointerException.class);
		this.overlayRollHorizontally = new OverlayRollHorizontally(STATES, ON_COLOR, null, 5, 1);
	}

	@Test
	public void onLedVisited_shouldReturnLedWithOnColorWhenOnState() {
		overlayRollHorizontally.onMatrixIterationEnded();
		final Led actualLed = overlayRollHorizontally.onLedVisited(led, 0, 4);

		final Led expectedLed = new Led(ON_COLOR);

		assertThat(actualLed, equalTo(expectedLed));
	}

	@Test
	public void onLedVisited_shouldReturnLedWithOffColorWhenOffState() {
		overlayRollHorizontally.onMatrixIterationEnded();
		overlayRollHorizontally.onMatrixIterationEnded();
		final Led actualLed = overlayRollHorizontally.onLedVisited(led, 1, 4);

		final Led expectedLed = new Led(OFF_COLOR);

		assertThat(actualLed, equalTo(expectedLed));
	}

	@Test
	public void onLedVisited_shouldReturnDefaultLedWhenTransparentState() {
		overlayRollHorizontally.onMatrixIterationEnded();
		overlayRollHorizontally.onMatrixIterationEnded();
		overlayRollHorizontally.onMatrixIterationEnded();
		overlayRollHorizontally.onMatrixIterationEnded();
		final Led actualLed = overlayRollHorizontally.onLedVisited(led, 1, 4);

		final Led expectedLed = new Led();

		assertThat(actualLed, equalTo(expectedLed));
	}

	@Test
	public void onLedVisited_shouldReturnDefaultLedWhenStateIsNotRecognized() {
		overlayRollHorizontally.onMatrixIterationEnded();
		final Led actualLed = overlayRollHorizontally.onLedVisited(led, 3, 4);

		final Led expectedLed = new Led();

		assertThat(actualLed, equalTo(expectedLed));
	}

	@Test
	public void isExhausted_shouldReturnTrueWhenExhausted() {
		for (int i = 1; i <= 13; i++) {
			assertFalse(overlayRollHorizontally.isExhausted());
			overlayRollHorizontally.onMatrixIterationEnded();
		}

		assertTrue(overlayRollHorizontally.isExhausted());
	}

	@Test
	public void getStateAt_shouldReturnTransparentStateIfColumnIndexLessThanCurrentMark() {
		overlayRollHorizontally.onMatrixIterationEnded();
		final Led.State actualState = overlayRollHorizontally.getStateAt(0, 3);
		assertThat(actualState, equalTo(TRANSPARENT));

		assertThat(overlayRollHorizontally.getStateAt(0, 4), equalTo(ON));
	}

	@Test
	public void getStateAt_shouldReturnTransparentStateIfColumnIndexIsBiggerThanCurrentMarkPlusStatesWidth() {
		overlayRollHorizontally.onMatrixIterationEnded();
		final Led.State actualState = overlayRollHorizontally.getStateAt(0, 11);
		assertThat(actualState, equalTo(TRANSPARENT));

		assertThat(overlayRollHorizontally.getStateAt(0, 10), equalTo(ON));
	}

	@Test
	public void getStateAt_shouldReturnTransparentStateIfRowIndexIsLessThanYPosition() {
		this.overlayRollHorizontally = new OverlayRollHorizontally(STATES, ON_COLOR, OFF_COLOR, 5, 1);
		overlayRollHorizontally.onMatrixIterationEnded();

		final Led.State actualState = overlayRollHorizontally.getStateAt(0, 4);
		assertThat(actualState, equalTo(TRANSPARENT));

		assertThat(overlayRollHorizontally.getStateAt(1, 4), equalTo(ON));
	}

	@Test
	public void getStateAt_shouldReturnTransparentStateIfRowIndexIsBiggerThanYPositionPlusStatesHeight() {
		overlayRollHorizontally.onMatrixIterationEnded();
		final Led.State actualState = overlayRollHorizontally.getStateAt(4, 4);
		assertThat(actualState, equalTo(TRANSPARENT));

		assertThat(overlayRollHorizontally.getStateAt(2, 4), equalTo(ON));
	}

	@Test
	public void getCompiledFrames_shouldReturnRollingFrames() {
		final Frame frame1 = new Frame(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame2 = new Frame(asList( //
				asList(new Led(), new Led(), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame3 = new Frame(asList( //
				asList(new Led(), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(ON_COLOR), new Led(OFF_COLOR)), //
				asList(new Led(), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame4 = new Frame(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(OFF_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame5 = new Frame(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(OFF_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame6 = new Frame(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame7 = new Frame(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame8 = new Frame(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame9 = new Frame(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame10 = new Frame(asList( //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame11 = new Frame(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final CompiledFrames expectedCompiledFrames = new CompiledFrames(
				asList(frame1, frame2, frame3, frame4, frame5, frame6, frame7, frame8, frame9, frame10, frame11));

		final OverlayRollHorizontally overlayRollHorizontally = new OverlayRollHorizontally(STATES, ON_COLOR, OFF_COLOR,
				3, 0);
		final CompiledFrames actualCompile = overlayRollHorizontally.getCompiledFrames(6, 3);

		Assert.assertThat(actualCompile, equalTo(expectedCompiledFrames));
	}

	@Test
	public void getCompiledFrames_shouldReturnRollingFramesWithYPositionPositive() {
		final Frame frame1 = new Frame(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame2 = new Frame(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame3 = new Frame(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(ON_COLOR), new Led(OFF_COLOR)), //
				asList(new Led(), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame4 = new Frame(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(OFF_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame5 = new Frame(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(OFF_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame6 = new Frame(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame7 = new Frame(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame8 = new Frame(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame9 = new Frame(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame10 = new Frame(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame11 = new Frame(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final CompiledFrames expectedCompiledFrames = new CompiledFrames(
				asList(frame1, frame2, frame3, frame4, frame5, frame6, frame7, frame8, frame9, frame10, frame11));

		final OverlayRollHorizontally overlayRollHorizontally = new OverlayRollHorizontally(STATES, ON_COLOR, OFF_COLOR,
				3, 1);
		final CompiledFrames actualCompile = overlayRollHorizontally.getCompiledFrames(6, 3);

		Assert.assertThat(actualCompile, is(equalTo(expectedCompiledFrames)));
	}

	@Test
	public void getCompiledFrames_shouldReturnRollingFramesWithYPositionNegative() {
		final Frame frame1 = new Frame(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame2 = new Frame(asList( //
				asList(new Led(), new Led(), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame3 = new Frame(asList( //
				asList(new Led(), new Led(ON_COLOR), new Led(OFF_COLOR)), //
				asList(new Led(), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame4 = new Frame(asList( //
				asList(new Led(ON_COLOR), new Led(OFF_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame5 = new Frame(asList( //
				asList(new Led(OFF_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame6 = new Frame(asList( //
				asList(new Led(ON_COLOR), new Led(), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame7 = new Frame(asList( //
				asList(new Led(), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame8 = new Frame(asList( //
				asList(new Led(ON_COLOR), new Led(), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame9 = new Frame(asList( //
				asList(new Led(), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame10 = new Frame(asList( //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame11 = new Frame(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final CompiledFrames expectedCompiledFrames = new CompiledFrames(
				asList(frame1, frame2, frame3, frame4, frame5, frame6, frame7, frame8, frame9, frame10, frame11));

		final OverlayRollHorizontally overlayRollHorizontally = new OverlayRollHorizontally(STATES, ON_COLOR, OFF_COLOR,
				3, -1);
		final CompiledFrames actualCompile = overlayRollHorizontally.getCompiledFrames(6, 3);

		Assert.assertThat(actualCompile, is(equalTo(expectedCompiledFrames)));
	}

	@Test
	public void getCompiledFrames_shouldReturnRollingFramesWithBeginMarkBiggerThanMatrixWidth() {
		final Frame frame0 = new Frame(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame1 = new Frame(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame2 = new Frame(asList( //
				asList(new Led(), new Led(), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame3 = new Frame(asList( //
				asList(new Led(), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(ON_COLOR), new Led(OFF_COLOR)), //
				asList(new Led(), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame4 = new Frame(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(OFF_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame5 = new Frame(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(OFF_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame6 = new Frame(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame7 = new Frame(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame8 = new Frame(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame9 = new Frame(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame10 = new Frame(asList( //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame11 = new Frame(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final CompiledFrames expectedCompiledFrames = new CompiledFrames(asList(frame0, frame1, frame2, frame3, frame4,
				frame5, frame6, frame7, frame8, frame9, frame10, frame11));

		final OverlayRollHorizontally overlayRollHorizontally = new OverlayRollHorizontally(STATES, ON_COLOR, OFF_COLOR,
				4, 0);
		final CompiledFrames actualCompile = overlayRollHorizontally.getCompiledFrames(6, 3);

		Assert.assertThat(actualCompile, is(equalTo(expectedCompiledFrames)));
	}

	@Test
	public void getCompiledFrames_shouldReturnRollingFramesWithBeginMarkOfZero() {
		final Frame frame1 = new Frame(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(OFF_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame2 = new Frame(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(OFF_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame3 = new Frame(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame4 = new Frame(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame5 = new Frame(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(), new Led(ON_COLOR)), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame6 = new Frame(asList( //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(), new Led(ON_COLOR), new Led()), //
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame7 = new Frame(asList( //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(ON_COLOR), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final Frame frame8 = new Frame(asList( //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led()), //
				asList(new Led(), new Led(), new Led())));

		final CompiledFrames expectedCompiledFrames = new CompiledFrames(
				asList(frame1, frame2, frame3, frame4, frame5, frame6, frame7, frame8));

		final OverlayRollHorizontally overlayRollHorizontally = new OverlayRollHorizontally(STATES, ON_COLOR, OFF_COLOR,
				0, 0);
		final CompiledFrames actualCompile = overlayRollHorizontally.getCompiledFrames(6, 3);

		Assert.assertThat(actualCompile, is(equalTo(expectedCompiledFrames)));
	}
}