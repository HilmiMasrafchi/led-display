/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

import static me.hmasrafchi.leddisplay.api.Led.RgbColor.BLACK;
import static me.hmasrafchi.leddisplay.api.Led.RgbColor.GREEN;
import static me.hmasrafchi.leddisplay.api.Led.RgbColor.RED;
import static me.hmasrafchi.leddisplay.api.Led.RgbColor.YELLOW;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.api.Led.RgbColor;
import me.hmasrafchi.leddisplay.model.overlay.Overlay;
import me.hmasrafchi.leddisplay.model.overlay.Overlay.State;
import me.hmasrafchi.leddisplay.model.overlay.OverlayRollHorizontal;
import me.hmasrafchi.leddisplay.model.overlay.OverlayStationary;

/**
 * @author michelin
 *
 */
public final class TestCompositeScene {
	private Matrix matrix;
	private Collection<? extends Scene> scenes;

	@Before
	public void init() {
		final int matrixColumnsCount = 5;
		final int matrixRowsCount = 6;

		this.scenes = getScenes(matrixColumnsCount);
		this.matrix = getMatrix(matrixColumnsCount, matrixRowsCount, scenes);
	}

	@Test
	public void test() {
		final int sceneRepeatCount = 10;
		for (int i = 0; i < sceneRepeatCount; i++) {
			// 1nd frame
			verifyNextFrame(Arrays.asList( //
					Arrays.asList(RED, RED, RED, RED, RED), //
					Arrays.asList(BLACK, BLACK, BLACK, BLACK, GREEN), //
					Arrays.asList(RED, BLACK, BLACK, BLACK, RED), //
					Arrays.asList(YELLOW, BLACK, BLACK, BLACK, YELLOW), //
					Arrays.asList(RED, BLACK, BLACK, BLACK, RED), //
					Arrays.asList(RED, RED, RED, RED, RED)));

			// 2rd frame
			verifyNextFrame(Arrays.asList( //
					Arrays.asList(RED, RED, RED, RED, RED), //
					Arrays.asList(BLACK, BLACK, BLACK, GREEN, GREEN), //
					Arrays.asList(RED, BLACK, BLACK, GREEN, RED), //
					Arrays.asList(YELLOW, BLACK, BLACK, GREEN, YELLOW), //
					Arrays.asList(RED, BLACK, BLACK, BLACK, RED), //
					Arrays.asList(RED, RED, RED, RED, RED)));

			// 3th frame
			verifyNextFrame(Arrays.asList( //
					Arrays.asList(RED, RED, RED, RED, RED), //
					Arrays.asList(BLACK, BLACK, GREEN, GREEN, GREEN), //
					Arrays.asList(RED, BLACK, GREEN, BLACK, RED), //
					Arrays.asList(YELLOW, BLACK, GREEN, GREEN, YELLOW), //
					Arrays.asList(RED, BLACK, BLACK, BLACK, RED), //
					Arrays.asList(RED, RED, RED, RED, RED)));

			// 4th frame
			verifyNextFrame(Arrays.asList( //
					Arrays.asList(RED, RED, RED, RED, RED), //
					Arrays.asList(BLACK, GREEN, GREEN, GREEN, GREEN), //
					Arrays.asList(RED, GREEN, BLACK, GREEN, RED), //
					Arrays.asList(YELLOW, GREEN, GREEN, GREEN, YELLOW), //
					Arrays.asList(RED, BLACK, BLACK, BLACK, RED), //
					Arrays.asList(RED, RED, RED, RED, RED)));

			// 5th frame
			verifyNextFrame(Arrays.asList( //
					Arrays.asList(RED, RED, RED, RED, RED), //
					Arrays.asList(GREEN, GREEN, GREEN, GREEN, GREEN), //
					Arrays.asList(RED, BLACK, GREEN, BLACK, RED), //
					Arrays.asList(YELLOW, GREEN, GREEN, GREEN, YELLOW), //
					Arrays.asList(RED, BLACK, BLACK, BLACK, RED), //
					Arrays.asList(RED, RED, RED, RED, RED)));

			// 6th frame
			verifyNextFrame(Arrays.asList( //
					Arrays.asList(RED, RED, RED, RED, RED), //
					Arrays.asList(GREEN, GREEN, GREEN, GREEN, GREEN), //
					Arrays.asList(RED, GREEN, BLACK, GREEN, RED), //
					Arrays.asList(YELLOW, GREEN, GREEN, GREEN, YELLOW), //
					Arrays.asList(RED, BLACK, BLACK, BLACK, RED), //
					Arrays.asList(RED, RED, RED, RED, RED)));

			// 7th frame
			verifyNextFrame(Arrays.asList( //
					Arrays.asList(RED, RED, RED, RED, RED), //
					Arrays.asList(GREEN, GREEN, GREEN, GREEN, GREEN), //
					Arrays.asList(RED, BLACK, GREEN, BLACK, RED), //
					Arrays.asList(YELLOW, GREEN, GREEN, GREEN, YELLOW), //
					Arrays.asList(RED, BLACK, BLACK, BLACK, RED), //
					Arrays.asList(RED, RED, RED, RED, RED)));

			// 8th frame
			verifyNextFrame(Arrays.asList( //
					Arrays.asList(RED, RED, RED, RED, RED), //
					Arrays.asList(GREEN, GREEN, GREEN, GREEN, BLACK), //
					Arrays.asList(RED, GREEN, BLACK, GREEN, RED), //
					Arrays.asList(YELLOW, GREEN, GREEN, GREEN, YELLOW), //
					Arrays.asList(RED, BLACK, BLACK, BLACK, RED), //
					Arrays.asList(RED, RED, RED, RED, RED)));

			// 9th frame
			verifyNextFrame(Arrays.asList( //
					Arrays.asList(RED, RED, RED, RED, RED), //
					Arrays.asList(GREEN, GREEN, GREEN, BLACK, BLACK), //
					Arrays.asList(RED, BLACK, GREEN, BLACK, RED), //
					Arrays.asList(YELLOW, GREEN, GREEN, BLACK, YELLOW), //
					Arrays.asList(RED, BLACK, BLACK, BLACK, RED), //
					Arrays.asList(RED, RED, RED, RED, RED)));

			// 10th frame
			verifyNextFrame(Arrays.asList( //
					Arrays.asList(RED, RED, RED, RED, RED), //
					Arrays.asList(GREEN, GREEN, BLACK, BLACK, BLACK), //
					Arrays.asList(RED, GREEN, BLACK, BLACK, RED), //
					Arrays.asList(YELLOW, GREEN, BLACK, BLACK, YELLOW), //
					Arrays.asList(RED, BLACK, BLACK, BLACK, RED), //
					Arrays.asList(RED, RED, RED, RED, RED)));

			// 11th frame
			verifyNextFrame(Arrays.asList( //
					Arrays.asList(RED, RED, RED, RED, RED), //
					Arrays.asList(GREEN, BLACK, BLACK, BLACK, BLACK), //
					Arrays.asList(RED, BLACK, BLACK, BLACK, RED), //
					Arrays.asList(YELLOW, BLACK, BLACK, BLACK, YELLOW), //
					Arrays.asList(RED, BLACK, BLACK, BLACK, RED), //
					Arrays.asList(RED, RED, RED, RED, RED)));

			// 12th frame
			verifyNextFrame(Arrays.asList( //
					Arrays.asList(RED, RED, RED, RED, RED), //
					Arrays.asList(BLACK, BLACK, BLACK, BLACK, BLACK), //
					Arrays.asList(RED, BLACK, BLACK, BLACK, RED), //
					Arrays.asList(YELLOW, BLACK, BLACK, BLACK, YELLOW), //
					Arrays.asList(RED, BLACK, BLACK, BLACK, RED), //
					Arrays.asList(RED, RED, RED, RED, RED)));
		}
	}

	private Matrix getMatrix(final int matrixColumnsCount, final int matrixRowsCount,
			final Collection<? extends Scene> scenes) {
		final List<List<? extends Led>> leds = new ArrayList<>();
		for (int i = 0; i < matrixRowsCount; i++) {
			final List<Led> row = new ArrayList<>();
			for (int j = 0; j < matrixColumnsCount; j++) {
				row.add(new DummyLed());
			}

			leds.add(row);
		}
		return new Matrix(leds, scenes);
	}

	private Collection<? extends Scene> getScenes(final int matrixColumnsCount) {
		// OverlayRollHorizontal
		final List<List<State>> statesRoll = Arrays.asList(
				Arrays.asList(Overlay.State.ON, Overlay.State.ON, Overlay.State.ON, Overlay.State.ON, Overlay.State.ON,
						Overlay.State.ON, Overlay.State.ON),
				Arrays.asList(Overlay.State.ON, Overlay.State.TRANSPARENT, Overlay.State.ON, Overlay.State.TRANSPARENT,
						Overlay.State.ON, Overlay.State.TRANSPARENT, Overlay.State.ON),
				Arrays.asList(Overlay.State.ON, Overlay.State.ON, Overlay.State.ON, Overlay.State.ON, Overlay.State.ON,
						Overlay.State.ON, Overlay.State.ON));
		RgbColor expectedRollColor = RgbColor.GREEN;
		final Overlay overlayRoll = new OverlayRollHorizontal(statesRoll, expectedRollColor, RgbColor.YELLOW, 1,
				matrixColumnsCount);

		// OverlayStationary
		final List<List<State>> statesStationary = Arrays.asList(
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
		final RgbColor stationaryForegroundColor = RgbColor.RED;
		final RgbColor stationaryBackgroundColor = RgbColor.YELLOW;
		final Overlay overlayStationary = new OverlayStationary(statesStationary, stationaryForegroundColor,
				stationaryBackgroundColor);

		final Scene overlayedScene = new SceneOverlayed(Arrays.asList(overlayRoll, overlayStationary));
		return Arrays.asList(overlayedScene);
	}

	private void verifyNextFrame(final List<List<RgbColor>> expectedColors) {
		this.matrix.nextFrame();
		final List<List<? extends Led>> expectedLeds1 = getExpectedLeds(expectedColors);
		Assert.assertThat(this.matrix, CoreMatchers.is(new Matrix(expectedLeds1, scenes)));
	}

	private List<List<? extends Led>> getExpectedLeds(final List<List<RgbColor>> colors) {
		final List<List<? extends Led>> leds = new ArrayList<>(colors.size());
		colors.stream().forEach(rowOfColor -> {
			final List<Led> rowOfLed = rowOfColor.stream().map(DummyLed::new).collect(Collectors.toList());
			leds.add(rowOfLed);
		});

		return leds;
	}

	List<? extends String> s = new ArrayList<String>();
	List<? extends List<? extends String>> asd = new ArrayList<ArrayList<String>>();
}

@EqualsAndHashCode
@AllArgsConstructor
@ToString
class DummyLed implements Led {
	private RgbColor color;

	DummyLed() {
		color = RgbColor.BLACK;
	}

	@Override
	public void setCoordinateX(double x) {
	}

	@Override
	public void setCoordinateY(double y) {
	}

	@Override
	public double getHeight() {
		return 0;
	}

	@Override
	public double getWidth() {
		return 0;
	}

	@Override
	public void setOpacityLevels(double opacity) {
	}

	@Override
	public void setRgbColor(RgbColor rgbColor) {
		this.color = rgbColor;
	}

	@Override
	public void reset() {
		this.color = RgbColor.BLACK;
	}

	@Override
	public RgbColor getRgbColor() {
		return color;
	}

	@Override
	public void setText(String text) {
	}

	@Override
	public void setTextFontSize(double ledTextFontSize) {
	}
}