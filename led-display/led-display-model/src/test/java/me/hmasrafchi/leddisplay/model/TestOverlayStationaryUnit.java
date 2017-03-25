/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import static java.util.Arrays.asList;
import static me.hmasrafchi.leddisplay.api.LedState.OFF;
import static me.hmasrafchi.leddisplay.api.LedState.ON;
import static me.hmasrafchi.leddisplay.api.LedState.TRANSPARENT;
import static me.hmasrafchi.leddisplay.api.LedState.UNRECOGNIZED;
import static me.hmasrafchi.leddisplay.api.RgbColor.RED;
import static me.hmasrafchi.leddisplay.api.RgbColor.YELLOW;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.api.LedState;
import me.hmasrafchi.leddisplay.api.RgbColor;
import me.hmasrafchi.leddisplay.util.TwoDimensionalListRectangular;

/**
 * @author michelin
 *
 */
@RunWith(MockitoJUnitRunner.class)
public final class TestOverlayStationaryUnit {
	private static final TwoDimensionalListRectangular<LedState> STATES = new TwoDimensionalListRectangular<>(asList( //
			Arrays.asList(ON, ON, ON, ON, ON), //
			Arrays.asList(TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT), //
			Arrays.asList(ON, TRANSPARENT, TRANSPARENT, TRANSPARENT, ON), //
			Arrays.asList(OFF, TRANSPARENT, TRANSPARENT, TRANSPARENT, OFF), //
			Arrays.asList(ON, TRANSPARENT, TRANSPARENT, TRANSPARENT, ON), //
			Arrays.asList(ON, ON, ON, ON, ON), //
			Arrays.asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED)));

	@Rule
	public final ExpectedException expectedException = ExpectedException.none();

	@Mock
	private Led led;

	private static final RgbColor ON_COLOR = RED;
	private static final RgbColor OFF_COLOR = YELLOW;

	private Overlay overlayStationary;

	@Before
	public void init() {
		this.overlayStationary = new OverlayStationary(STATES, ON_COLOR, OFF_COLOR);
	}

	@Test
	public void constructor_shouldThrowNullPointExceptionIfStatesAreNull() {
		expectedException.expect(NullPointerException.class);
		this.overlayStationary = new OverlayStationary(null, ON_COLOR, OFF_COLOR);
	}

	@Test
	public void constructor_shouldThrowNullPointerExceptionIfOnColorIsNull() {
		expectedException.expect(NullPointerException.class);
		this.overlayStationary = new OverlayStationary(STATES, null, OFF_COLOR);
	}

	@Test
	public void constructor_shouldThrowNullPointerExceptionIfOffColorIsNull() {
		expectedException.expect(NullPointerException.class);
		this.overlayStationary = new OverlayStationary(STATES, ON_COLOR, null);
	}

	@Test
	public void constructor_shouldThrowIllegalArgumentExceptionIfDurationIsZero() {
		expectedException.expect(IllegalArgumentException.class);
		this.overlayStationary = new OverlayStationary(STATES, ON_COLOR, OFF_COLOR, 0);
	}

	@Test
	public void constructor_shouldThrowIllegalArgumentExceptionIfDurationIsNegative() {
		expectedException.expect(IllegalArgumentException.class);
		this.overlayStationary = new OverlayStationary(STATES, ON_COLOR, OFF_COLOR, -1);
	}

	@Test
	public void onLedVisited_shouldReturnLedWithOnColorWhenOnState() {
		final Led actualLed = overlayStationary.onLedVisited(led, 0, 0);
		final Led expectedLed = new Led(ON_COLOR);

		assertThat(actualLed, is(equalTo(expectedLed)));
	}

	@Test
	public void onLedVisited_shouldReturnLedWithOffColorWhenOffState() {
		final Led actualLed = overlayStationary.onLedVisited(led, 3, 0);
		final Led expectedLed = new Led(OFF_COLOR);

		assertThat(actualLed, is(equalTo(expectedLed)));
	}

	@Test
	public void onLedVisited_shouldReturnDefaultLedWhenUnrecognizedState() {
		final Led actualLed = overlayStationary.onLedVisited(led, 6, 0);
		final Led expectedLed = new Led();

		assertThat(actualLed, is(equalTo(expectedLed)));
	}

	@Test
	public void onLedVisited_shouldReturnDefaultLedWhenTransparentState() {
		final Led actualLed = overlayStationary.onLedVisited(led, 1, 1);
		final Led expectedLed = new Led();

		assertThat(actualLed, is(equalTo(expectedLed)));
	}

	@Test
	public void onLedVisited_shouldReturnDefaultLedWhenStateIsNotRecognized() {
		final Led actualLed = overlayStationary.onLedVisited(led, 0, 6);
		final Led expectedLed = new Led();

		assertThat(actualLed, is(equalTo(expectedLed)));
	}

	@Test
	public void isExhausted_shouldReturnTrueOnMatrixIterationEnded() {
		assertFalse(overlayStationary.isExhausted());
		assertFalse(overlayStationary.isExhausted());

		overlayStationary.onMatrixIterationEnded();

		assertTrue(overlayStationary.isExhausted());
		assertTrue(overlayStationary.isExhausted());
	}

	@Test
	public void isExhausted_shouldReturnTrueWithDurationBiggerThanOne() {
		overlayStationary = new OverlayStationary(STATES, ON_COLOR, OFF_COLOR, 2);

		assertFalse(overlayStationary.isExhausted());
		assertFalse(overlayStationary.isExhausted());

		overlayStationary.onMatrixIterationEnded();
		assertFalse(overlayStationary.isExhausted());
		assertFalse(overlayStationary.isExhausted());

		overlayStationary.onMatrixIterationEnded();
		assertTrue(overlayStationary.isExhausted());
		assertTrue(overlayStationary.isExhausted());
	}

	@Test
	public void getStateAt_shouldReturnTransparentStateIfColumnIndexIsNegative() {
		final LedState actualState = overlayStationary.getStateAt(0, -1);
		assertThat(actualState, is(equalTo(TRANSPARENT)));
	}

	@Test
	public void getStateAt_shouldReturnTransparentStateIfColumnIndexIsBiggerThanMaximumColumnSize() {
		final LedState actualState = overlayStationary.getStateAt(0, 5);
		assertThat(actualState, is(equalTo(TRANSPARENT)));
	}

	@Test
	public void getStateAt_shouldReturnTransparentStateIfRowIndexIsNegative() {
		final LedState actualState = overlayStationary.getStateAt(-1, 0);
		assertThat(actualState, is(equalTo(TRANSPARENT)));
	}

	@Test
	public void getStateAt_shouldReturnTransparentStateForRowIndexBiggerThanMaximumRowSize() {
		final LedState actualState = overlayStationary.getStateAt(7, 0);
		assertThat(actualState, is(equalTo(TRANSPARENT)));
	}

	@Test
	public void getStateAt_shouldReturnStateAtSpecifiedIndex() {
		final LedState actualState1 = overlayStationary.getStateAt(0, 0);
		assertThat(actualState1, is(equalTo(ON)));

		final LedState actualState2 = overlayStationary.getStateAt(5, 0);
		assertThat(actualState2, is(equalTo(ON)));

		final LedState actualState3 = overlayStationary.getStateAt(0, 4);
		assertThat(actualState3, is(equalTo(ON)));

		final LedState actualState4 = overlayStationary.getStateAt(5, 4);
		assertThat(actualState4, is(equalTo(ON)));
	}

	@Test
	public void onMatrixIterationEnded_shouldDoNothing() {
		overlayStationary.onMatrixIterationEnded();
	}
}