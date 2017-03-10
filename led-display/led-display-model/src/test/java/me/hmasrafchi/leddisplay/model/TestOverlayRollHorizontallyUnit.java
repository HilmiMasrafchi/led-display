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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.api.RgbColor;
import me.hmasrafchi.leddisplay.api.LedState;
import me.hmasrafchi.leddisplay.util.TwoDimensionalListRectangular;

/**
 * @author michelin
 *
 */
@RunWith(MockitoJUnitRunner.class)
public final class TestOverlayRollHorizontallyUnit {
	private static final TwoDimensionalListRectangular<LedState> STATES = new TwoDimensionalListRectangular<>(asList( //
			asList(ON, ON, ON, ON, ON, ON, ON), //
			asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON), //
			asList(ON, ON, ON, ON, ON, ON, ON), //
			asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED)));

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

		assertThat(actualLed, is(equalTo(expectedLed)));
	}

	@Test
	public void onLedVisited_shouldReturnLedWithOffColorWhenOffState() {
		overlayRollHorizontally.onMatrixIterationEnded();
		overlayRollHorizontally.onMatrixIterationEnded();
		final Led actualLed = overlayRollHorizontally.onLedVisited(led, 1, 4);

		final Led expectedLed = new Led(OFF_COLOR);

		assertThat(actualLed, is(equalTo(expectedLed)));
	}

	@Test
	public void onLedVisited_shouldReturnDefaultLedWhenTransparentState() {
		overlayRollHorizontally.onMatrixIterationEnded();
		overlayRollHorizontally.onMatrixIterationEnded();
		overlayRollHorizontally.onMatrixIterationEnded();
		overlayRollHorizontally.onMatrixIterationEnded();
		final Led actualLed = overlayRollHorizontally.onLedVisited(led, 1, 4);

		final Led expectedLed = new Led();

		assertThat(actualLed, is(equalTo(expectedLed)));
	}

	@Test
	public void onLedVisited_shouldReturnDefaultLedWhenStateIsNotRecognized() {
		overlayRollHorizontally.onMatrixIterationEnded();
		final Led actualLed = overlayRollHorizontally.onLedVisited(led, 3, 4);

		final Led expectedLed = new Led();

		assertThat(actualLed, is(equalTo(expectedLed)));
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
		final LedState actualState = overlayRollHorizontally.getStateAt(0, 3);
		assertThat(actualState, is(equalTo(TRANSPARENT)));

		assertThat(overlayRollHorizontally.getStateAt(0, 4), is(equalTo(ON)));
	}

	@Test
	public void getStateAt_shouldReturnTransparentStateIfColumnIndexIsBiggerThanCurrentMarkPlusStatesWidth() {
		overlayRollHorizontally.onMatrixIterationEnded();
		final LedState actualState = overlayRollHorizontally.getStateAt(0, 11);
		assertThat(actualState, is(equalTo(TRANSPARENT)));

		assertThat(overlayRollHorizontally.getStateAt(0, 10), is(equalTo(ON)));
	}

	@Test
	public void getStateAt_shouldReturnTransparentStateIfRowIndexIsLessThanYPosition() {
		this.overlayRollHorizontally = new OverlayRollHorizontally(STATES, ON_COLOR, OFF_COLOR, 5, 1);
		overlayRollHorizontally.onMatrixIterationEnded();

		final LedState actualState = overlayRollHorizontally.getStateAt(0, 4);
		assertThat(actualState, is(equalTo(TRANSPARENT)));

		assertThat(overlayRollHorizontally.getStateAt(1, 4), is(equalTo(ON)));
	}

	@Test
	public void getStateAt_shouldReturnTransparentStateIfRowIndexIsBiggerThanYPositionPlusStatesHeight() {
		overlayRollHorizontally.onMatrixIterationEnded();
		final LedState actualState = overlayRollHorizontally.getStateAt(4, 4);
		assertThat(actualState, is(equalTo(TRANSPARENT)));

		assertThat(overlayRollHorizontally.getStateAt(2, 4), is(equalTo(ON)));
	}
}