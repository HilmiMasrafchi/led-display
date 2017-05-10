/**
 * 
 */
package me.hmasrafchi.leddisplay.model.domain;

import static java.util.Arrays.asList;
import static java.util.Optional.of;
import static me.hmasrafchi.leddisplay.model.domain.Led.State.OFF;
import static me.hmasrafchi.leddisplay.model.domain.Led.State.ON;
import static me.hmasrafchi.leddisplay.model.domain.Led.State.TRANSPARENT;
import static me.hmasrafchi.leddisplay.model.domain.Led.State.UNRECOGNIZED;
import static me.hmasrafchi.leddisplay.model.domain.RgbColor.BLACK;
import static me.hmasrafchi.leddisplay.model.domain.RgbColor.BLUE;
import static me.hmasrafchi.leddisplay.model.domain.RgbColor.GREEN;
import static me.hmasrafchi.leddisplay.model.domain.RgbColor.RED;
import static me.hmasrafchi.leddisplay.model.domain.RgbColor.YELLOW;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import me.hmasrafchi.leddisplay.model.domain.CompiledFrames;
import me.hmasrafchi.leddisplay.model.domain.DomainConstraintViolationException;
import me.hmasrafchi.leddisplay.model.domain.Frame;
import me.hmasrafchi.leddisplay.model.domain.Led;
import me.hmasrafchi.leddisplay.model.domain.Matrix;
import me.hmasrafchi.leddisplay.model.domain.Overlay;
import me.hmasrafchi.leddisplay.model.domain.OverlayRollHorizontally;
import me.hmasrafchi.leddisplay.model.domain.OverlayStationary;
import me.hmasrafchi.leddisplay.model.domain.RgbColor;
import me.hmasrafchi.leddisplay.model.domain.Scene;
import me.hmasrafchi.leddisplay.model.domain.SceneComposite;
import me.hmasrafchi.leddisplay.model.domain.SceneOverlayed;

/**
 * @author michelin
 *
 */
public final class TestMatrix {
	@Rule
	public final ExpectedException expectedException = ExpectedException.none();

	@Test
	public void constructor_shouldThrowDomainContraintViolationExceptionIfRowCountIsNegative() {
		expectedException.expect(DomainConstraintViolationException.class);
		new Matrix(-1, 10, Optional.empty());
	}

	@Test
	public void constructor_shouldThrowDomainContraintViolationExceptionIfRowCountIsZero() {
		expectedException.expect(DomainConstraintViolationException.class);
		new Matrix(0, 10, Optional.empty());
	}

	@Test
	public void constructor_shouldThrowDomainContraintViolationExceptionIfColumnCountIsNegative() {
		expectedException.expect(DomainConstraintViolationException.class);
		new Matrix(10, -1, Optional.empty());
	}

	@Test
	public void constructor_shouldThrowDomainContraintViolationExceptionIfColumnCountIsZero() {
		expectedException.expect(DomainConstraintViolationException.class);
		new Matrix(10, 0, Optional.empty());
	}

	@Test
	public void constructor_shouldThrowNullPointerExceptionIfSceneIsNull() {
		expectedException.expect(NullPointerException.class);
		new Matrix(10, 10, null);
	}

	@Test
	public void getCompiledFrames_shouldOptionalEmptyIfSceneOptionalIsEmpty() {
		final Matrix matrix = new Matrix(10, 10, Optional.empty());
		assertThat(matrix.getCompiledFrames(), equalTo(Optional.empty()));
	}

	@Test
	public void getCompiledFrames_shouldReturnStationaryFrames() {
		final RgbColor ON_COLOR = RED;
		final RgbColor OFF_COLOR = YELLOW;

		final List<List<Led.State>> STATES = asList( //
				asList(ON, ON, ON, ON, ON), //
				asList(TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT), //
				asList(ON, TRANSPARENT, TRANSPARENT, TRANSPARENT, ON), //
				asList(OFF, TRANSPARENT, TRANSPARENT, TRANSPARENT, OFF), //
				asList(ON, TRANSPARENT, TRANSPARENT, TRANSPARENT, ON), //
				asList(ON, ON, ON, ON, ON), //
				asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED));

		final List<List<Led>> expectedLedStates = asList(
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)),
				asList(new Led(), new Led(), new Led(), new Led(), new Led()),
				asList(new Led(ON_COLOR), new Led(), new Led(), new Led(), new Led(ON_COLOR)),
				asList(new Led(OFF_COLOR), new Led(), new Led(), new Led(), new Led(OFF_COLOR)),
				asList(new Led(ON_COLOR), new Led(), new Led(), new Led(), new Led(ON_COLOR)),
				asList(new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR), new Led(ON_COLOR)),
				asList(new Led(), new Led(), new Led(), new Led(), new Led()));

		final Overlay overlayStationary = new OverlayStationary(STATES, ON_COLOR, OFF_COLOR);

		final CompiledFrames expectedCompiledFrames = new CompiledFrames(asList(new Frame(expectedLedStates)));

		final Matrix matrix = new Matrix(7, 5, of(overlayStationary));
		final CompiledFrames actualCompiledFrames = matrix.getCompiledFrames().get();

		assertThat(actualCompiledFrames, equalTo(expectedCompiledFrames));
	}

	@Test
	public void getCompiledFrames_shouldReturnRollingFrames() {
		final RgbColor ON_COLOR = RED;
		final RgbColor OFF_COLOR = YELLOW;

		final List<List<Led.State>> STATES = Arrays.asList( //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON), //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED));

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
		final Matrix matrix = new Matrix(6, 3, of(overlayRollHorizontally));
		final CompiledFrames actualCompile = matrix.getCompiledFrames().get();

		Assert.assertThat(actualCompile, equalTo(expectedCompiledFrames));
	}

	@Test
	public void getCompiledFrames_shouldReturnRollingFramesWithYPositionPositive() {
		final RgbColor ON_COLOR = RED;
		final RgbColor OFF_COLOR = YELLOW;

		final List<List<Led.State>> STATES = Arrays.asList( //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON), //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED));

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
		final Matrix matrix = new Matrix(6, 3, of(overlayRollHorizontally));
		final CompiledFrames actualCompile = matrix.getCompiledFrames().get();

		Assert.assertThat(actualCompile, is(equalTo(expectedCompiledFrames)));
	}

	@Test
	public void getCompiledFrames_shouldReturnRollingFramesWithYPositionNegative() {
		final RgbColor ON_COLOR = RED;
		final RgbColor OFF_COLOR = YELLOW;

		final List<List<Led.State>> STATES = Arrays.asList( //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON), //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED));

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
		final Matrix matrix = new Matrix(6, 3, of(overlayRollHorizontally));
		final CompiledFrames actualCompile = matrix.getCompiledFrames().get();

		Assert.assertThat(actualCompile, is(equalTo(expectedCompiledFrames)));
	}

	@Test
	public void getCompiledFrames_shouldReturnRollingFramesWithBeginMarkBiggerThanMatrixWidth() {
		final RgbColor ON_COLOR = RED;
		final RgbColor OFF_COLOR = YELLOW;

		final List<List<Led.State>> STATES = Arrays.asList( //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON), //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED));

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
		final Matrix matrix = new Matrix(6, 3, of(overlayRollHorizontally));
		final CompiledFrames actualCompile = matrix.getCompiledFrames().get();

		Assert.assertThat(actualCompile, is(equalTo(expectedCompiledFrames)));
	}

	@Test
	public void getCompiledFrames_shouldReturnRollingFramesWithBeginMarkOfZero() {
		final RgbColor ON_COLOR = RED;
		final RgbColor OFF_COLOR = YELLOW;

		final List<List<Led.State>> STATES = Arrays.asList( //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON), //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED, UNRECOGNIZED,
						UNRECOGNIZED));

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
		final Matrix matrix = new Matrix(6, 3, of(overlayRollHorizontally));
		final CompiledFrames actualCompile = matrix.getCompiledFrames().get();

		Assert.assertThat(actualCompile, is(equalTo(expectedCompiledFrames)));
	}

	@Test
	public void getCompiledFrames_shouldReturnCompositedScenes() {
		final int columnCount = 5;
		final int rowCount = 6;

		final List<List<Led.State>> STATES_ROLL1 = asList( //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON), //
				asList(ON, ON, ON, ON, ON, ON, ON));

		final List<List<Led.State>> STATES_ROLL2 = asList( //
				asList(ON, ON, ON), //
				asList(ON, OFF, ON), //
				asList(ON, ON, ON));

		final Scene scene1 = new OverlayRollHorizontally(STATES_ROLL1, GREEN, BLUE, columnCount, 0);
		final Scene scene2 = new OverlayRollHorizontally(STATES_ROLL2, GREEN, BLUE, columnCount, 1);
		final Scene sceneComposited = new SceneComposite(asList(scene1, scene2));

		// scene 1
		final Frame frame1 = new Frame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame2 = new Frame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame3 = new Frame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(BLUE)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame4 = new Frame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(BLUE), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame5 = new Frame(asList( //
				asList(new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(GREEN), new Led(BLUE), new Led(GREEN), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame6 = new Frame(asList( //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(GREEN), new Led(BLUE), new Led(GREEN), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame7 = new Frame(asList( //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLUE), new Led(GREEN), new Led(BLACK), new Led(GREEN), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame8 = new Frame(asList( //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(GREEN), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame9 = new Frame(asList( //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(GREEN), new Led(BLACK), new Led(GREEN), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame10 = new Frame(asList( //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(GREEN), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame11 = new Frame(asList( //
				asList(new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame12 = new Frame(asList( //
				asList(new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame13 = new Frame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		// scene 2
		final Frame frame14 = new Frame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame15 = new Frame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame16 = new Frame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(BLUE)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame17 = new Frame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(BLUE), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame18 = new Frame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(GREEN), new Led(BLUE), new Led(GREEN), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame19 = new Frame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(BLUE), new Led(GREEN), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame20 = new Frame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLUE), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame21 = new Frame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final Frame frame22 = new Frame(asList( //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK))));

		final CompiledFrames expectedCompiledFrames = new CompiledFrames(asList(frame1, frame2, frame3, frame4, frame5,
				frame6, frame7, frame8, frame9, frame10, frame11, frame12, frame13, frame14, frame15, frame16, frame17,
				frame18, frame19, frame20, frame21, frame22));

		final Matrix matrix = new Matrix(rowCount, columnCount, of(sceneComposited));
		final CompiledFrames actualCompiledFrames = matrix.getCompiledFrames().get();

		assertThat(actualCompiledFrames, equalTo(expectedCompiledFrames));
	}

	@Test
	public void getCompiledFrames_ledsShouldBeOverlayed() {
		final int columnCount = 5;
		final int rowCount = 6;
		final List<List<Led.State>> STATES_ROLL = asList( //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON), //
				asList(ON, ON, ON, ON, ON, ON, ON));

		final List<List<Led.State>> STATES_STATIONARY = asList( //
				asList(ON, ON, ON, ON, ON), //
				asList(TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT), //
				asList(ON, TRANSPARENT, TRANSPARENT, TRANSPARENT, ON), //
				asList(OFF, TRANSPARENT, TRANSPARENT, TRANSPARENT, OFF), //
				asList(ON, TRANSPARENT, TRANSPARENT, TRANSPARENT, ON), //
				asList(ON, ON, ON, ON, ON));

		final Overlay overlayRoll = new OverlayRollHorizontally(STATES_ROLL, GREEN, BLUE, columnCount, 1);
		final Overlay overlayStationary = new OverlayStationary(STATES_STATIONARY, RED, YELLOW);

		final Scene sceneOverlayed = new SceneOverlayed(asList(overlayRoll, overlayStationary));

		// 0nd frame
		final Frame frame0 = new Frame(
				asList(asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
						asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
						asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
						asList(new Led(YELLOW), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(YELLOW)), //
						asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
						asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 1nd frame
		final Frame frame1 = new Frame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(YELLOW), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 2rd frame
		final Frame frame2 = new Frame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(RED)), //
				asList(new Led(YELLOW), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 3th frame
		final Frame frame3 = new Frame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(BLACK), new Led(GREEN), new Led(BLUE), new Led(RED)), //
				asList(new Led(YELLOW), new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 4th frame
		final Frame frame4 = new Frame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(GREEN), new Led(BLUE), new Led(GREEN), new Led(RED)), //
				asList(new Led(YELLOW), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 5th frame
		final Frame frame5 = new Frame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(BLUE), new Led(GREEN), new Led(BLACK), new Led(RED)), //
				asList(new Led(YELLOW), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 6th frame
		final Frame frame6 = new Frame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(GREEN), new Led(BLACK), new Led(GREEN), new Led(RED)), //
				asList(new Led(YELLOW), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 7th frame
		final Frame frame7 = new Frame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(BLACK), new Led(GREEN), new Led(BLACK), new Led(RED)), //
				asList(new Led(YELLOW), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 8th frame
		final Frame frame8 = new Frame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK)), //
				asList(new Led(RED), new Led(GREEN), new Led(BLACK), new Led(GREEN), new Led(RED)), //
				asList(new Led(YELLOW), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 9th frame
		final Frame frame9 = new Frame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(RED), new Led(BLACK), new Led(GREEN), new Led(BLACK), new Led(RED)), //
				asList(new Led(YELLOW), new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 10th frame
		final Frame frame10 = new Frame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(RED), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(YELLOW), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 11th frame
		final Frame frame11 = new Frame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(YELLOW), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 12th frame
		final Frame frame12 = new Frame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(YELLOW), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		final CompiledFrames expectedCompiledFrames = new CompiledFrames(asList(frame0, frame1, frame2, frame3, frame4,
				frame5, frame6, frame7, frame8, frame9, frame10, frame11, frame12));

		final Matrix matrix = new Matrix(rowCount, columnCount, of(sceneOverlayed));
		final CompiledFrames actualCompiledFrames = matrix.getCompiledFrames().get();
		assertThat(actualCompiledFrames, equalTo(expectedCompiledFrames));
	}

	@Test
	public void getCompiledFrames_ledsShouldBeOverlayedInReverseOrder() {
		final int columnCount = 5;
		final int rowCount = 6;
		final List<List<Led.State>> STATES_ROLL = asList( //
				asList(ON, ON, ON, ON, ON, ON, ON), //
				asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON), //
				asList(ON, ON, ON, ON, ON, ON, ON));

		final List<List<Led.State>> STATES_STATIONARY = asList( //
				asList(ON, ON, ON, ON, ON), //
				asList(TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT), //
				asList(ON, TRANSPARENT, TRANSPARENT, TRANSPARENT, ON), //
				asList(OFF, TRANSPARENT, TRANSPARENT, TRANSPARENT, OFF), //
				asList(ON, TRANSPARENT, TRANSPARENT, TRANSPARENT, ON), //
				asList(ON, ON, ON, ON, ON));

		final Overlay overlayRoll = new OverlayRollHorizontally(STATES_ROLL, GREEN, BLUE, columnCount, 1);
		final Overlay overlayStationary = new OverlayStationary(STATES_STATIONARY, RED, YELLOW);

		final Scene sceneOverlayed = new SceneOverlayed(asList(overlayStationary, overlayRoll));

		// 0nd frame
		final Frame frame0 = new Frame(
				asList(asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
						asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
						asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
						asList(new Led(YELLOW), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(YELLOW)), //
						asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
						asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 1nd frame
		final Frame frame1 = new Frame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(YELLOW), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 2rd frame
		final Frame frame2 = new Frame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(BLUE)), //
				asList(new Led(YELLOW), new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 3th frame
		final Frame frame3 = new Frame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(BLACK), new Led(GREEN), new Led(BLUE), new Led(GREEN)), //
				asList(new Led(YELLOW), new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 4th frame
		final Frame frame4 = new Frame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(BLACK), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(GREEN), new Led(BLUE), new Led(GREEN), new Led(RED)), //
				asList(new Led(YELLOW), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 5th frame
		final Frame frame5 = new Frame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(GREEN), new Led(BLUE), new Led(GREEN), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 6th frame
		final Frame frame6 = new Frame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(BLUE), new Led(GREEN), new Led(BLACK), new Led(GREEN), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 7th frame
		final Frame frame7 = new Frame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(GREEN), new Led(BLACK), new Led(GREEN)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 8th frame
		final Frame frame8 = new Frame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK)), //
				asList(new Led(RED), new Led(GREEN), new Led(BLACK), new Led(GREEN), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 9th frame
		final Frame frame9 = new Frame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(GREEN), new Led(BLACK), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 10th frame
		final Frame frame10 = new Frame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(RED), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(GREEN), new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 11th frame
		final Frame frame11 = new Frame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(GREEN), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		// 12th frame
		final Frame frame12 = new Frame(asList( //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED)), //
				asList(new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(BLACK)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(YELLOW), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(YELLOW)), //
				asList(new Led(RED), new Led(BLACK), new Led(BLACK), new Led(BLACK), new Led(RED)), //
				asList(new Led(RED), new Led(RED), new Led(RED), new Led(RED), new Led(RED))));

		final CompiledFrames expectedCompiledFrames = new CompiledFrames(asList(frame0, frame1, frame2, frame3, frame4,
				frame5, frame6, frame7, frame8, frame9, frame10, frame11, frame12));

		final Matrix matrix = new Matrix(rowCount, columnCount, of(sceneOverlayed));
		final CompiledFrames actualCompiledFrames = matrix.getCompiledFrames().get();
		assertThat(actualCompiledFrames, equalTo(expectedCompiledFrames));
	}
}