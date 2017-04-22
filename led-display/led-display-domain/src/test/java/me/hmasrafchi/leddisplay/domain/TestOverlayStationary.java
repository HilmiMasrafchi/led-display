/**
 * 
 */
package me.hmasrafchi.leddisplay.domain;

import static java.util.Arrays.asList;
import static me.hmasrafchi.leddisplay.domain.Led.State.OFF;
import static me.hmasrafchi.leddisplay.domain.Led.State.ON;
import static me.hmasrafchi.leddisplay.domain.Led.State.TRANSPARENT;
import static me.hmasrafchi.leddisplay.domain.Led.State.UNRECOGNIZED;
import static me.hmasrafchi.leddisplay.domain.RgbColor.RED;
import static me.hmasrafchi.leddisplay.domain.RgbColor.YELLOW;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import me.hmasrafchi.leddisplay.domain.Led;
import me.hmasrafchi.leddisplay.domain.Overlay;
import me.hmasrafchi.leddisplay.domain.OverlayStationary;
import me.hmasrafchi.leddisplay.domain.RgbColor;

/**
 * @author michelin
 *
 */
@RunWith(MockitoJUnitRunner.class)
public final class TestOverlayStationary {
	private static final List<List<Led.State>> STATES = asList( //
			asList(ON, ON, ON, ON, ON), //
			asList(TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT), //
			asList(ON, TRANSPARENT, TRANSPARENT, TRANSPARENT, ON), //
			asList(OFF, TRANSPARENT, TRANSPARENT, TRANSPARENT, OFF), //
			asList(ON, TRANSPARENT, TRANSPARENT, TRANSPARENT, ON), //
			asList(ON, ON, ON, ON, ON), //
			asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED));

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

		assertThat(actualLed, equalTo(expectedLed));
	}

	@Test
	public void onLedVisited_shouldReturnLedWithOffColorWhenOffState() {
		final Led actualLed = overlayStationary.onLedVisited(led, 3, 0);
		final Led expectedLed = new Led(OFF_COLOR);

		assertThat(actualLed, equalTo(expectedLed));
	}

	@Test
	public void onLedVisited_shouldReturnDefaultLedWhenUnrecognizedState() {
		final Led actualLed = overlayStationary.onLedVisited(led, 6, 0);
		final Led expectedLed = new Led();

		assertThat(actualLed, equalTo(expectedLed));
	}

	@Test
	public void onLedVisited_shouldReturnDefaultLedWhenTransparentState() {
		final Led actualLed = overlayStationary.onLedVisited(led, 1, 1);
		final Led expectedLed = new Led();

		assertThat(actualLed, equalTo(expectedLed));
	}

	@Test
	public void onLedVisited_shouldReturnDefaultLedWhenStateIsNotRecognized() {
		final Led actualLed = overlayStationary.onLedVisited(led, 0, 6);
		final Led expectedLed = new Led();

		assertThat(actualLed, equalTo(expectedLed));
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
		final Led.State actualState = overlayStationary.getStateAt(0, -1);
		assertThat(actualState, equalTo(TRANSPARENT));
	}

	@Test
	public void getStateAt_shouldReturnTransparentStateIfColumnIndexIsBiggerThanMaximumColumnSize() {
		final Led.State actualState = overlayStationary.getStateAt(0, 5);
		assertThat(actualState, equalTo(TRANSPARENT));
	}

	@Test
	public void getStateAt_shouldReturnTransparentStateIfRowIndexIsNegative() {
		final Led.State actualState = overlayStationary.getStateAt(-1, 0);
		assertThat(actualState, equalTo(TRANSPARENT));
	}

	@Test
	public void getStateAt_shouldReturnTransparentStateForRowIndexBiggerThanMaximumRowSize() {
		final Led.State actualState = overlayStationary.getStateAt(7, 0);
		assertThat(actualState, equalTo(TRANSPARENT));
	}

	@Test
	public void getStateAt_shouldReturnStateAtSpecifiedIndex() {
		final Led.State actualState1 = overlayStationary.getStateAt(0, 0);
		assertThat(actualState1, equalTo(ON));

		final Led.State actualState2 = overlayStationary.getStateAt(5, 0);
		assertThat(actualState2, equalTo(ON));

		final Led.State actualState3 = overlayStationary.getStateAt(0, 4);
		assertThat(actualState3, equalTo(ON));

		final Led.State actualState4 = overlayStationary.getStateAt(5, 4);
		assertThat(actualState4, equalTo(ON));
	}

	@Test
	public void onMatrixIterationEnded_shouldDoNothing() {
		overlayStationary.onMatrixIterationEnded();
	}
}