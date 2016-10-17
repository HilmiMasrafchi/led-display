/**
 * 
 */
package me.hmasrafchi.leddisplay.framework.scene.canvas;

/**
 * @author michelin
 *
 */
public final class RightToLeftCanvasScene {
	// extends AbstractScene {
	// private final OverlayRoll overlayRoll;
	// private final OverlayStationary overlayStationary;
	// private final int canvasYPosition;
	// private final int matrixWidth;
	//
	// private int currentMarker;
	//
	// public RightToLeftCanvasScene(final OverlayRoll overlayRoll, final
	// OverlayStationary overlayStationary,
	// final int canvasYPosition, final int matrixWidth) {
	// this.overlayRoll = overlayRoll;
	// this.overlayStationary = overlayStationary;
	// this.canvasYPosition = canvasYPosition;
	// this.matrixWidth = matrixWidth;
	//
	// this.currentMarker = matrixWidth - 1;
	// }
	//
	// @Override
	// public boolean hasNext() {
	// return currentMarker >= -overlayRoll.getWidth() + 1;
	// }
	//
	// @Override
	// public void determineLedState(final Led currentLed, final int
	// currentColumnIndex, final int currentRowIndex) {
	// boolean isInX = currentColumnIndex >= currentMarker
	// && currentMarker + overlayRoll.getWidth() > currentColumnIndex;
	// boolean isInY = currentRowIndex >= canvasYPosition
	// && currentRowIndex < overlayRoll.getHeight() + canvasYPosition;
	// if (isInX && isInY) {
	// final CanvasElementState currentState =
	// overlayRoll.getStateAt(currentColumnIndex - currentMarker,
	// currentRowIndex - canvasYPosition);
	// final boolean isOn = currentState.equals(CanvasElementState.ON);
	// if (isOn) {
	// currentLed.setRgbColor(onLedColor);
	// } else {
	// currentLed.reset();
	// }
	// } else {
	// currentLed.reset();
	// }
	// }
	//
	// @Override
	// public void iterationEnded() {
	// currentMarker--;
	// if (currentMarker < -overlayRoll.getWidth()) {
	// currentMarker = matrixWidth - 1;
	// }
	// }
}