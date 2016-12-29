/**
 * 
 */
package me.hmasrafchi.leddisplay.model.overlay;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import me.hmasrafchi.leddisplay.api.Led.RgbColor;
import me.hmasrafchi.leddisplay.model.overlay.Overlay.State;

/**
 * @author michelin
 *
 */
public final class TestOverlayStationary {
	private static final RgbColor OFF_COLOR = RgbColor.YELLOW;
	private static final RgbColor ON_COLOR = RgbColor.RED;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	private Overlay overlayStationary;

	@Before
	public void init() {
		final List<List<? extends State>> statesStationary = Arrays.asList(
				Arrays.asList(Overlay.State.ON, Overlay.State.ON, Overlay.State.ON, Overlay.State.ON, Overlay.State.ON),
				Arrays.asList(Overlay.State.TRANSPARENT, Overlay.State.TRANSPARENT, Overlay.State.TRANSPARENT,
						Overlay.State.TRANSPARENT, Overlay.State.TRANSPARENT),
				Arrays.asList(Overlay.State.ON, Overlay.State.TRANSPARENT, Overlay.State.TRANSPARENT,
						Overlay.State.TRANSPARENT, Overlay.State.ON),
				Arrays.asList(Overlay.State.OFF, Overlay.State.TRANSPARENT, Overlay.State.TRANSPARENT,
						Overlay.State.TRANSPARENT, Overlay.State.OFF),
				Arrays.asList(Overlay.State.ON, Overlay.State.TRANSPARENT, Overlay.State.TRANSPARENT,
						Overlay.State.TRANSPARENT, Overlay.State.ON),
				Arrays.asList(Overlay.State.ON, Overlay.State.ON, Overlay.State.ON, Overlay.State.ON,
						Overlay.State.ON));

		this.overlayStationary = new OverlayStationary(statesStationary, ON_COLOR, OFF_COLOR);
	}

	@Test
	public void constructor_shouldThrowNullPointerExceptionIfStatesCollectionIsNull() {
		this.expectedException.expect(NullPointerException.class);
		this.overlayStationary = new OverlayStationary(null, ON_COLOR, OFF_COLOR);
	}

	@Test
	public void constructor_shouldThrowNullPointerExceptionIfOnColorIsNull() {
		this.expectedException.expect(NullPointerException.class);
		this.overlayStationary = new OverlayStationary(Arrays.asList(Arrays.asList(State.OFF)), null, OFF_COLOR);
	}

	@Test
	public void constructor_shouldThrowNullPointerExceptionIfOffColorIsNull() {
		this.expectedException.expect(NullPointerException.class);
		this.overlayStationary = new OverlayStationary(Arrays.asList(Arrays.asList(State.OFF)), ON_COLOR, null);
	}

	@Test
	public void constructor_shouldThrowIllegalArgumentExceptionIfStatesCollectionIsEmpty() {
		this.expectedException.expect(IllegalArgumentException.class);
		this.overlayStationary = new OverlayStationary(Arrays.asList(Arrays.asList(State.OFF)), ON_COLOR, OFF_COLOR);
	}
}