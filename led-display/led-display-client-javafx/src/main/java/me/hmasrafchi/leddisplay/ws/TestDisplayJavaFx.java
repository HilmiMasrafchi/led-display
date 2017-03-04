/**
 * 
 */
package me.hmasrafchi.leddisplay.ws;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * @author michelin
 *
 */
public final class TestDisplayJavaFx extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// final int matrixRowsCount = 6;
		// final int matrixColumnsCount = 5;
		//
		// final SceneFactory sceneFactory = new SceneFactory();
		//
		// // OverlayRollHorizontal
		// final List<List<LedState>> statesRoll = asList( //
		// asList(ON, ON, ON, ON, ON, ON, ON), //
		// asList(ON, OFF, ON, TRANSPARENT, ON, TRANSPARENT, ON), //
		// asList(ON, ON, ON, ON, ON, ON, ON));
		// final int yposition = 1;
		// final Overlay overlayRoll =
		// sceneFactory.getOverlayRollHorizontally(statesRoll, GREEN, BLUE,
		// matrixColumnsCount,
		// yposition);
		//
		// // OverlayStationary
		// final List<List<LedState>> statesStationary = asList(asList(ON, ON,
		// ON, ON, ON), //
		// asList(TRANSPARENT, TRANSPARENT, TRANSPARENT, TRANSPARENT,
		// TRANSPARENT), //
		// asList(ON, TRANSPARENT, TRANSPARENT, TRANSPARENT, ON), //
		// asList(OFF, TRANSPARENT, TRANSPARENT, TRANSPARENT, OFF), //
		// asList(ON, TRANSPARENT, TRANSPARENT, TRANSPARENT, ON), //
		// asList(ON, ON, ON, ON, ON));
		// final Overlay overlayStationary =
		// sceneFactory.getOverlayStationary(statesStationary, RED, YELLOW, 1);
		//
		// final me.hmasrafchi.leddisplay.api.Scene sceneOverlayed = new
		// SceneOverlayed(
		// asList(overlayRoll, overlayStationary));
		// final me.hmasrafchi.leddisplay.api.Scene sceneRandom = new
		// SceneRandomColor(asList(BLUE, RED));
		// final me.hmasrafchi.leddisplay.api.Scene sceneComposite = new
		// SceneComposited(
		// asList(sceneRandom, sceneOverlayed));
		// final Matrix matrix = new MatrixDefault();
		// final CompiledFrames compiledFrames = matrix.compile(sceneComposite,
		// matrixRowsCount, matrixColumnsCount);
		//
		// final GridPane gridPane = new GridPane();
		// gridPane.setPadding(new Insets(0, 0, 0, 0));
		// final Collection<TwoDimensionalListRectangular<Led>> frames = new
		// ArrayList<>();
		// final Iterator<TwoDimensionalListRectangular<Led>> iterator =
		// compiledFrames.listIterator();
		// while (iterator.hasNext()) {
		// frames.add(iterator.next());
		// }
		//
		// final int guiRowsCount = matrixRowsCount;
		// final int guiColumnsCount = matrixColumnsCount;
		// TwoDimensionalListRectangular<LedJFx> map =
		// TwoDimensionalListRectangular.create(LedJFx::new, guiRowsCount,
		// guiColumnsCount);
		// for (int i = 0; i < map.getRowCount(); i++) {
		// gridPane.addRow(i, map.getRowAt(i).stream().toArray(Node[]::new));
		// }
		//
		// primaryStage.setScene(new Scene(gridPane));
		//
		// final Timeline timeline = new Timeline();
		// timeline.getKeyFrames().add(new KeyFrame(Duration.millis(200), new
		// EventHandler<ActionEvent>() {
		// CyclicIterator<TwoDimensionalListRectangular<Led>> framesIterator =
		// new CyclicIterator<>(frames);
		//
		// @Override
		// public void handle(ActionEvent event) {
		// final TwoDimensionalListRectangular<Led> frame =
		// framesIterator.next();
		// for (int r = 0; r < frame.getRowCount(); r++) {
		// for (int c = 0; c < frame.getColumnCount(); c++) {
		// final LedJFx ledJFx = map.getRowAt(r).get(c);
		// final Led led = frame.getRowAt(r).get(c);
		//
		// final LedRgbColor rgbColor = led.getRgbColor();
		// ledJFx.setFill(Color.rgb(rgbColor.getR(), rgbColor.getG(),
		// rgbColor.getB()));
		// }
		// }
		// }
		// }));
		// timeline.setCycleCount(Timeline.INDEFINITE);
		// primaryStage.show();
		// timeline.play();
	}
}