/**
 * 
 */
package me.hmasrafchi.leddisplay.framework.scene;

/**
 * @author michelin
 *
 */
public final class TestRightToLeftCanvasScene {
	// private Scene scene;
	// private Matrix matrix;
	//
	// @Before
	// public void init() {
	// final OverlayRoll canvas = createSquareCanvas(3);
	// this.scene = new RightToLeftCanvasScene(Led.RgbColor.RED, canvas, 0, 6);
	//
	// this.matrix = createMatrix(6, 6);
	// }
	//
	// private OverlayRoll createSquareCanvas(final int sideLength) {
	// final List<List<CanvasElementState>> canvasElements = new
	// ArrayList<>(sideLength);
	// for (int i = 0; i < sideLength; i++) {
	// final List<CanvasElementState> canvasElementRow = new
	// ArrayList<>(sideLength);
	// for (int j = 0; j < sideLength; j++) {
	// canvasElementRow.add(CanvasElementState.ON);
	// }
	//
	// canvasElements.add(canvasElementRow);
	// }
	//
	// canvasElements.get(sideLength / 2).set(sideLength / 2,
	// CanvasElementState.OFF);
	//
	// return new OverlayRoll(canvasElements);
	// }
	//
	// @Test
	// public void test() {
	// scene.nextFrame(matrix);
	// }
	//
	// private Matrix createMatrix(final int columnsCount, final int rowsCount)
	// {
	// final Provider<Led> ledProvider = new Provider<Led>() {
	// @Override
	// public Led get() {
	// return new Led() {
	// @Override
	// public void setRgbColor(RgbColor rgbColor) {
	// }
	//
	// @Override
	// public void setOpacityLevels(double opacity) {
	// }
	//
	// @Override
	// public void setCoordinateY(double y) {
	// }
	//
	// @Override
	// public void setCoordinateX(double x) {
	// }
	//
	// @Override
	// public void reset() {
	// }
	//
	// @Override
	// public double getWidth() {
	// return 0;
	// }
	//
	// @Override
	// public double getHeight() {
	// return 0;
	// }
	//
	// @Override
	// public double getCoordinateY() {
	// return 0;
	// }
	//
	// @Override
	// public double getCoordinateX() {
	// return 0;
	// }
	// };
	// }
	// };
	//
	// return new Matrix(columnsCount, rowsCount, ledProvider);
	// }
}