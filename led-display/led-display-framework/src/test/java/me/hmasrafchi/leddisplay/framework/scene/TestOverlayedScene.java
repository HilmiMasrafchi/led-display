/// **
// *
// */
// package me.hmasrafchi.leddisplay.framework.scene;
//
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.List;
//
// import org.hamcrest.CoreMatchers;
// import org.junit.Assert;
// import org.junit.Test;
//
// import me.hmasrafchi.leddisplay.api.Led;
// import me.hmasrafchi.leddisplay.api.Led.RgbColor;
// import me.hmasrafchi.leddisplay.framework.Matrix;
// import me.hmasrafchi.leddisplay.framework.generator.GeneratorLed;
// import me.hmasrafchi.leddisplay.framework.scene.overlay.Overlay;
// import me.hmasrafchi.leddisplay.framework.scene.overlay.Overlay.State;
// import
/// me.hmasrafchi.leddisplay.framework.scene.overlay.OverlayRollHorizontal;
// import me.hmasrafchi.leddisplay.framework.scene.overlay.OverlayStationary;
// import me.hmasrafchi.leddisplay.framework.scene.overlay.OverlayedScene;
//
/// **
// * @author michelin
// *
// */
// public final class TestOverlayedScene {
// @Test
// public void test() {
// final int matrixWidth = 5;
// final Matrix matrix = getMatrix(matrixWidth, 6);
//
// final List<List<State>> statesRoll = Arrays.asList(
// Arrays.asList(Overlay.State.ON, Overlay.State.ON, Overlay.State.ON,
/// Overlay.State.ON, Overlay.State.ON,
// Overlay.State.ON, Overlay.State.ON),
// Arrays.asList(Overlay.State.ON, Overlay.State.TRANSPARENT, Overlay.State.ON,
/// Overlay.State.TRANSPARENT,
// Overlay.State.ON, Overlay.State.TRANSPARENT, Overlay.State.ON),
// Arrays.asList(Overlay.State.ON, Overlay.State.ON, Overlay.State.ON,
/// Overlay.State.ON, Overlay.State.ON,
// Overlay.State.ON, Overlay.State.ON));
// RgbColor expectedRollColor = RgbColor.GREEN;
// final Overlay overlayRoll = new OverlayRollHorizontal(statesRoll,
/// expectedRollColor, 1, matrixWidth);
//
// final List<List<State>> statesStationary = Arrays.asList(
// Arrays.asList(Overlay.State.ON, Overlay.State.ON, Overlay.State.ON,
/// Overlay.State.ON, Overlay.State.ON),
// Arrays.asList(Overlay.State.OFF, Overlay.State.TRANSPARENT,
/// Overlay.State.TRANSPARENT,
// Overlay.State.TRANSPARENT, Overlay.State.OFF),
// Arrays.asList(Overlay.State.ON, Overlay.State.TRANSPARENT,
/// Overlay.State.TRANSPARENT,
// Overlay.State.TRANSPARENT, Overlay.State.ON),
// Arrays.asList(Overlay.State.OFF, Overlay.State.TRANSPARENT,
/// Overlay.State.TRANSPARENT,
// Overlay.State.TRANSPARENT, Overlay.State.OFF),
// Arrays.asList(Overlay.State.ON, Overlay.State.TRANSPARENT,
/// Overlay.State.TRANSPARENT,
// Overlay.State.TRANSPARENT, Overlay.State.ON),
// Arrays.asList(Overlay.State.ON, Overlay.State.ON, Overlay.State.ON,
/// Overlay.State.ON,
// Overlay.State.ON));
// final RgbColor expectedStationaryForegroundColor = RgbColor.RED;
// final RgbColor expectedStationaryBackgroundColor = RgbColor.YELLOW;
// final Overlay overlayStationary = new OverlayStationary(statesStationary,
/// expectedStationaryForegroundColor,
// expectedStationaryBackgroundColor);
//
// final List<Overlay> overlays = Arrays.asList(overlayRoll, overlayStationary);
// final Scene scene = new OverlayedScene(overlays);
//
// sceneNextFrame(scene, matrix);
// final List<List<RgbColor>> expectedColors1 = Arrays.asList(
// Arrays.asList(RgbColor.RED, RgbColor.RED, RgbColor.RED, RgbColor.RED,
/// RgbColor.RED),
// Arrays.asList(RgbColor.YELLOW, RgbColor.BLACK, RgbColor.BLACK,
/// RgbColor.BLACK, RgbColor.YELLOW),
// Arrays.asList(RgbColor.RED, RgbColor.BLACK, RgbColor.BLACK, RgbColor.BLACK,
/// RgbColor.RED),
// Arrays.asList(RgbColor.YELLOW, RgbColor.BLACK, RgbColor.BLACK,
/// RgbColor.BLACK, RgbColor.YELLOW),
// Arrays.asList(RgbColor.RED, RgbColor.BLACK, RgbColor.BLACK, RgbColor.BLACK,
/// RgbColor.RED),
// Arrays.asList(RgbColor.RED, RgbColor.RED, RgbColor.RED, RgbColor.RED,
/// RgbColor.RED));
// final List<List<RgbColor>> actualColors1 = getActualColors(matrix);
// Assert.assertThat(actualColors1, CoreMatchers.is(expectedColors1));
//
// sceneNextFrame(scene, matrix);
// final List<List<RgbColor>> expectedColors2 = Arrays.asList(
// Arrays.asList(RgbColor.RED, RgbColor.RED, RgbColor.RED, RgbColor.RED,
/// RgbColor.RED),
// Arrays.asList(RgbColor.YELLOW, RgbColor.BLACK, RgbColor.BLACK,
/// RgbColor.BLACK, RgbColor.YELLOW),
// Arrays.asList(RgbColor.RED, RgbColor.BLACK, RgbColor.BLACK, RgbColor.BLACK,
/// RgbColor.RED),
// Arrays.asList(RgbColor.YELLOW, RgbColor.BLACK, RgbColor.BLACK,
/// RgbColor.BLACK, RgbColor.YELLOW),
// Arrays.asList(RgbColor.RED, RgbColor.BLACK, RgbColor.BLACK, RgbColor.BLACK,
/// RgbColor.RED),
// Arrays.asList(RgbColor.RED, RgbColor.RED, RgbColor.RED, RgbColor.RED,
/// RgbColor.RED));
// final List<List<RgbColor>> actualColors2 = getActualColors(matrix);
// Assert.assertThat(actualColors2, CoreMatchers.is(expectedColors2));
//
// sceneNextFrame(scene, matrix);
// final List<List<RgbColor>> expectedColors3 = Arrays.asList(
// Arrays.asList(RgbColor.RED, RgbColor.RED, RgbColor.RED, RgbColor.RED,
/// RgbColor.RED),
// Arrays.asList(RgbColor.YELLOW, RgbColor.BLACK, RgbColor.BLACK,
/// RgbColor.GREEN, RgbColor.YELLOW),
// Arrays.asList(RgbColor.RED, RgbColor.BLACK, RgbColor.BLACK, RgbColor.GREEN,
/// RgbColor.RED),
// Arrays.asList(RgbColor.YELLOW, RgbColor.BLACK, RgbColor.BLACK,
/// RgbColor.GREEN, RgbColor.YELLOW),
// Arrays.asList(RgbColor.RED, RgbColor.BLACK, RgbColor.BLACK, RgbColor.BLACK,
/// RgbColor.RED),
// Arrays.asList(RgbColor.RED, RgbColor.RED, RgbColor.RED, RgbColor.RED,
/// RgbColor.RED));
// final List<List<RgbColor>> actualColors3 = getActualColors(matrix);
// Assert.assertThat(actualColors3, CoreMatchers.is(expectedColors3));
//
// sceneNextFrame(scene, matrix);
// final List<List<RgbColor>> expectedColors4 = Arrays.asList(
// Arrays.asList(RgbColor.RED, RgbColor.RED, RgbColor.RED, RgbColor.RED,
/// RgbColor.RED),
// Arrays.asList(RgbColor.YELLOW, RgbColor.BLACK, RgbColor.GREEN,
/// RgbColor.GREEN, RgbColor.YELLOW),
// Arrays.asList(RgbColor.RED, RgbColor.BLACK, RgbColor.GREEN, RgbColor.BLACK,
/// RgbColor.RED),
// Arrays.asList(RgbColor.YELLOW, RgbColor.BLACK, RgbColor.GREEN,
/// RgbColor.GREEN, RgbColor.YELLOW),
// Arrays.asList(RgbColor.RED, RgbColor.BLACK, RgbColor.BLACK, RgbColor.BLACK,
/// RgbColor.RED),
// Arrays.asList(RgbColor.RED, RgbColor.RED, RgbColor.RED, RgbColor.RED,
/// RgbColor.RED));
// final List<List<RgbColor>> actualColors4 = getActualColors(matrix);
// Assert.assertThat(actualColors4, CoreMatchers.is(expectedColors4));
//
// sceneNextFrame(scene, matrix);
// final List<List<RgbColor>> expectedColors5 = Arrays.asList(
// Arrays.asList(RgbColor.RED, RgbColor.RED, RgbColor.RED, RgbColor.RED,
/// RgbColor.RED),
// Arrays.asList(RgbColor.YELLOW, RgbColor.GREEN, RgbColor.GREEN,
/// RgbColor.GREEN, RgbColor.YELLOW),
// Arrays.asList(RgbColor.RED, RgbColor.GREEN, RgbColor.BLACK, RgbColor.GREEN,
/// RgbColor.RED),
// Arrays.asList(RgbColor.YELLOW, RgbColor.GREEN, RgbColor.GREEN,
/// RgbColor.GREEN, RgbColor.YELLOW),
// Arrays.asList(RgbColor.RED, RgbColor.BLACK, RgbColor.BLACK, RgbColor.BLACK,
/// RgbColor.RED),
// Arrays.asList(RgbColor.RED, RgbColor.RED, RgbColor.RED, RgbColor.RED,
/// RgbColor.RED));
// final List<List<RgbColor>> actualColors5 = getActualColors(matrix);
// Assert.assertThat(actualColors5, CoreMatchers.is(expectedColors5));
//
// sceneNextFrame(scene, matrix);
// final List<List<RgbColor>> expectedColors6 = Arrays.asList(
// Arrays.asList(RgbColor.RED, RgbColor.RED, RgbColor.RED, RgbColor.RED,
/// RgbColor.RED),
// Arrays.asList(RgbColor.YELLOW, RgbColor.GREEN, RgbColor.GREEN,
/// RgbColor.GREEN, RgbColor.YELLOW),
// Arrays.asList(RgbColor.RED, RgbColor.BLACK, RgbColor.GREEN, RgbColor.BLACK,
/// RgbColor.RED),
// Arrays.asList(RgbColor.YELLOW, RgbColor.GREEN, RgbColor.GREEN,
/// RgbColor.GREEN, RgbColor.YELLOW),
// Arrays.asList(RgbColor.RED, RgbColor.BLACK, RgbColor.BLACK, RgbColor.BLACK,
/// RgbColor.RED),
// Arrays.asList(RgbColor.RED, RgbColor.RED, RgbColor.RED, RgbColor.RED,
/// RgbColor.RED));
// final List<List<RgbColor>> actualColors6 = getActualColors(matrix);
// Assert.assertThat(actualColors6, CoreMatchers.is(expectedColors6));
//
// sceneNextFrame(scene, matrix);
// final List<List<RgbColor>> expectedColors7 = Arrays.asList(
// Arrays.asList(RgbColor.RED, RgbColor.RED, RgbColor.RED, RgbColor.RED,
/// RgbColor.RED),
// Arrays.asList(RgbColor.YELLOW, RgbColor.GREEN, RgbColor.GREEN,
/// RgbColor.GREEN, RgbColor.YELLOW),
// Arrays.asList(RgbColor.RED, RgbColor.GREEN, RgbColor.BLACK, RgbColor.GREEN,
/// RgbColor.RED),
// Arrays.asList(RgbColor.YELLOW, RgbColor.GREEN, RgbColor.GREEN,
/// RgbColor.GREEN, RgbColor.YELLOW),
// Arrays.asList(RgbColor.RED, RgbColor.BLACK, RgbColor.BLACK, RgbColor.BLACK,
/// RgbColor.RED),
// Arrays.asList(RgbColor.RED, RgbColor.RED, RgbColor.RED, RgbColor.RED,
/// RgbColor.RED));
// final List<List<RgbColor>> actualColors7 = getActualColors(matrix);
// Assert.assertThat(actualColors7, CoreMatchers.is(expectedColors7));
//
// sceneNextFrame(scene, matrix);
// final List<List<RgbColor>> expectedColors8 = Arrays.asList(
// Arrays.asList(RgbColor.RED, RgbColor.RED, RgbColor.RED, RgbColor.RED,
/// RgbColor.RED),
// Arrays.asList(RgbColor.YELLOW, RgbColor.GREEN, RgbColor.GREEN,
/// RgbColor.GREEN, RgbColor.YELLOW),
// Arrays.asList(RgbColor.RED, RgbColor.BLACK, RgbColor.GREEN, RgbColor.BLACK,
/// RgbColor.RED),
// Arrays.asList(RgbColor.YELLOW, RgbColor.GREEN, RgbColor.GREEN,
/// RgbColor.GREEN, RgbColor.YELLOW),
// Arrays.asList(RgbColor.RED, RgbColor.BLACK, RgbColor.BLACK, RgbColor.BLACK,
/// RgbColor.RED),
// Arrays.asList(RgbColor.RED, RgbColor.RED, RgbColor.RED, RgbColor.RED,
/// RgbColor.RED));
// final List<List<RgbColor>> actualColors8 = getActualColors(matrix);
// Assert.assertThat(actualColors8, CoreMatchers.is(expectedColors8));
//
// sceneNextFrame(scene, matrix);
// final List<List<RgbColor>> expectedColors9 = Arrays.asList(
// Arrays.asList(RgbColor.RED, RgbColor.RED, RgbColor.RED, RgbColor.RED,
/// RgbColor.RED),
// Arrays.asList(RgbColor.YELLOW, RgbColor.GREEN, RgbColor.GREEN,
/// RgbColor.GREEN, RgbColor.YELLOW),
// Arrays.asList(RgbColor.RED, RgbColor.GREEN, RgbColor.BLACK, RgbColor.GREEN,
/// RgbColor.RED),
// Arrays.asList(RgbColor.YELLOW, RgbColor.GREEN, RgbColor.GREEN,
/// RgbColor.GREEN, RgbColor.YELLOW),
// Arrays.asList(RgbColor.RED, RgbColor.BLACK, RgbColor.BLACK, RgbColor.BLACK,
/// RgbColor.RED),
// Arrays.asList(RgbColor.RED, RgbColor.RED, RgbColor.RED, RgbColor.RED,
/// RgbColor.RED));
// final List<List<RgbColor>> actualColors9 = getActualColors(matrix);
// Assert.assertThat(actualColors9, CoreMatchers.is(expectedColors9));
//
// sceneNextFrame(scene, matrix);
// final List<List<RgbColor>> expectedColors10 = Arrays.asList(
// Arrays.asList(RgbColor.RED, RgbColor.RED, RgbColor.RED, RgbColor.RED,
/// RgbColor.RED),
// Arrays.asList(RgbColor.YELLOW, RgbColor.GREEN, RgbColor.GREEN,
/// RgbColor.BLACK, RgbColor.YELLOW),
// Arrays.asList(RgbColor.RED, RgbColor.BLACK, RgbColor.GREEN, RgbColor.BLACK,
/// RgbColor.RED),
// Arrays.asList(RgbColor.YELLOW, RgbColor.GREEN, RgbColor.GREEN,
/// RgbColor.BLACK, RgbColor.YELLOW),
// Arrays.asList(RgbColor.RED, RgbColor.BLACK, RgbColor.BLACK, RgbColor.BLACK,
/// RgbColor.RED),
// Arrays.asList(RgbColor.RED, RgbColor.RED, RgbColor.RED, RgbColor.RED,
/// RgbColor.RED));
// final List<List<RgbColor>> actualColors10 = getActualColors(matrix);
// Assert.assertThat(actualColors10, CoreMatchers.is(expectedColors10));
//
// sceneNextFrame(scene, matrix);
// final List<List<RgbColor>> expectedColors11 = Arrays.asList(
// Arrays.asList(RgbColor.RED, RgbColor.RED, RgbColor.RED, RgbColor.RED,
/// RgbColor.RED),
// Arrays.asList(RgbColor.YELLOW, RgbColor.GREEN, RgbColor.BLACK,
/// RgbColor.BLACK, RgbColor.YELLOW),
// Arrays.asList(RgbColor.RED, RgbColor.GREEN, RgbColor.BLACK, RgbColor.BLACK,
/// RgbColor.RED),
// Arrays.asList(RgbColor.YELLOW, RgbColor.GREEN, RgbColor.BLACK,
/// RgbColor.BLACK, RgbColor.YELLOW),
// Arrays.asList(RgbColor.RED, RgbColor.BLACK, RgbColor.BLACK, RgbColor.BLACK,
/// RgbColor.RED),
// Arrays.asList(RgbColor.RED, RgbColor.RED, RgbColor.RED, RgbColor.RED,
/// RgbColor.RED));
// final List<List<RgbColor>> actualColors11 = getActualColors(matrix);
// Assert.assertThat(actualColors11, CoreMatchers.is(expectedColors11));
//
// sceneNextFrame(scene, matrix);
// final List<List<RgbColor>> expectedColors12 = Arrays.asList(
// Arrays.asList(RgbColor.RED, RgbColor.RED, RgbColor.RED, RgbColor.RED,
/// RgbColor.RED),
// Arrays.asList(RgbColor.YELLOW, RgbColor.BLACK, RgbColor.BLACK,
/// RgbColor.BLACK, RgbColor.YELLOW),
// Arrays.asList(RgbColor.RED, RgbColor.BLACK, RgbColor.BLACK, RgbColor.BLACK,
/// RgbColor.RED),
// Arrays.asList(RgbColor.YELLOW, RgbColor.BLACK, RgbColor.BLACK,
/// RgbColor.BLACK, RgbColor.YELLOW),
// Arrays.asList(RgbColor.RED, RgbColor.BLACK, RgbColor.BLACK, RgbColor.BLACK,
/// RgbColor.RED),
// Arrays.asList(RgbColor.RED, RgbColor.RED, RgbColor.RED, RgbColor.RED,
/// RgbColor.RED));
// final List<List<RgbColor>> actualColors12 = getActualColors(matrix);
// Assert.assertThat(actualColors12, CoreMatchers.is(expectedColors12));
//
// sceneNextFrame(scene, matrix);
// final List<List<RgbColor>> expectedColors13 = Arrays.asList(
// Arrays.asList(RgbColor.RED, RgbColor.RED, RgbColor.RED, RgbColor.RED,
/// RgbColor.RED),
// Arrays.asList(RgbColor.YELLOW, RgbColor.BLACK, RgbColor.BLACK,
/// RgbColor.BLACK, RgbColor.YELLOW),
// Arrays.asList(RgbColor.RED, RgbColor.BLACK, RgbColor.BLACK, RgbColor.BLACK,
/// RgbColor.RED),
// Arrays.asList(RgbColor.YELLOW, RgbColor.BLACK, RgbColor.BLACK,
/// RgbColor.BLACK, RgbColor.YELLOW),
// Arrays.asList(RgbColor.RED, RgbColor.BLACK, RgbColor.BLACK, RgbColor.BLACK,
/// RgbColor.RED),
// Arrays.asList(RgbColor.RED, RgbColor.RED, RgbColor.RED, RgbColor.RED,
/// RgbColor.RED));
// final List<List<RgbColor>> actualColors13 = getActualColors(matrix);
// Assert.assertThat(actualColors13, CoreMatchers.is(expectedColors13));
//
// sceneNextFrame(scene, matrix);
// final List<List<RgbColor>> expectedColors14 = Arrays.asList(
// Arrays.asList(RgbColor.RED, RgbColor.RED, RgbColor.RED, RgbColor.RED,
/// RgbColor.RED),
// Arrays.asList(RgbColor.YELLOW, RgbColor.BLACK, RgbColor.BLACK,
/// RgbColor.BLACK, RgbColor.YELLOW),
// Arrays.asList(RgbColor.RED, RgbColor.BLACK, RgbColor.BLACK, RgbColor.BLACK,
/// RgbColor.RED),
// Arrays.asList(RgbColor.YELLOW, RgbColor.BLACK, RgbColor.BLACK,
/// RgbColor.BLACK, RgbColor.YELLOW),
// Arrays.asList(RgbColor.RED, RgbColor.BLACK, RgbColor.BLACK, RgbColor.BLACK,
/// RgbColor.RED),
// Arrays.asList(RgbColor.RED, RgbColor.RED, RgbColor.RED, RgbColor.RED,
/// RgbColor.RED));
// final List<List<RgbColor>> actualColors14 = getActualColors(matrix);
// Assert.assertThat(actualColors14, CoreMatchers.is(expectedColors14));
//
// sceneNextFrame(scene, matrix);
// final List<List<RgbColor>> expectedColors15 = Arrays.asList(
// Arrays.asList(RgbColor.RED, RgbColor.RED, RgbColor.RED, RgbColor.RED,
/// RgbColor.RED),
// Arrays.asList(RgbColor.YELLOW, RgbColor.BLACK, RgbColor.BLACK,
/// RgbColor.GREEN, RgbColor.YELLOW),
// Arrays.asList(RgbColor.RED, RgbColor.BLACK, RgbColor.BLACK, RgbColor.GREEN,
/// RgbColor.RED),
// Arrays.asList(RgbColor.YELLOW, RgbColor.BLACK, RgbColor.BLACK,
/// RgbColor.GREEN, RgbColor.YELLOW),
// Arrays.asList(RgbColor.RED, RgbColor.BLACK, RgbColor.BLACK, RgbColor.BLACK,
/// RgbColor.RED),
// Arrays.asList(RgbColor.RED, RgbColor.RED, RgbColor.RED, RgbColor.RED,
/// RgbColor.RED));
// final List<List<RgbColor>> actualColors15 = getActualColors(matrix);
// Assert.assertThat(actualColors15, CoreMatchers.is(expectedColors15));
// }
//
// private final List<List<RgbColor>> getActualColors(final Matrix matrix) {
// final List<List<RgbColor>> colors = new ArrayList<>();
// for (int i = 0; i < matrix.getRowsCount(); i++) {
// final List<RgbColor> row = new ArrayList<>();
// for (int j = 0; j < matrix.getColumnsCount(); j++) {
// final Led led = matrix.getLedAt(j, i);
// row.add(led.getRgbColor());
// }
//
// colors.add(row);
// }
//
// return colors;
// }
//
// private Matrix getMatrix(final int width, final int height) {
// final GeneratorLed ledGenerator = new GeneratorLed() {
// @Override
// public Led next() {
// return new DummyLed();
// }
//
// @Override
// public double getLedMaximumHeight() {
// return 10;
// }
//
// @Override
// public double getLedMaximumWidth() {
// return 10;
// }
// };
//
// return new Matrix(ledGenerator, width, height);
// }
//
// private void sceneNextFrame(final Scene scene, final Matrix matrix) {
// if (!scene.hasNextFrame()) {
// scene.reset(matrix);
// }
//
// scene.nextFrame(matrix);
// }
// }
//
// class DummyLed implements Led {
// private RgbColor color;
//
// public DummyLed() {
// reset();
// }
//
// @Override
// public void setCoordinateX(double x) {
// }
//
// @Override
// public void setCoordinateY(double y) {
// }
//
// @Override
// public double getHeight() {
// return 0;
// }
//
// @Override
// public double getWidth() {
// return 0;
// }
//
// @Override
// public void setOpacityLevels(double opacity) {
// }
//
// @Override
// public void setRgbColor(RgbColor rgbColor) {
// this.color = rgbColor;
// }
//
// @Override
// public void reset() {
// this.color = RgbColor.BLACK;
// }
//
// @Override
// public RgbColor getRgbColor() {
// return color;
// }
//
// @Override
// public void setText(String text) {
// }
//
// @Override
// public void setTextFontSize(double ledTextFontSize) {
// }
// }