/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx;

import java.util.Iterator;
import java.util.List;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import me.hmasrafchi.leddisplay.model.view.LedView;
import me.hmasrafchi.leddisplay.model.view.RgbColorView;

/**
 * @author michelin
 *
 */
final class CompiledFramesGui extends ScrollPane {
	private static final int LED_WIDTH = 20;
	private static final int LED_HEIGHT = 20;

	private final Animation animation;

	CompiledFramesGui(final int matrixRowCount, final int matrixColumnCount,
			final List<List<List<LedView>>> compiledFrames) {
		final Canvas canvas = new Canvas();
		canvas.setHeight(matrixRowCount * LED_HEIGHT);
		canvas.setWidth(matrixColumnCount * LED_WIDTH);
		setContent(canvas);
		this.animation = getAnimation(canvas.getGraphicsContext2D(), compiledFrames);
	}

	private Animation getAnimation(final GraphicsContext graphics, final List<List<List<LedView>>> compiledFrames) {
		final Timeline animation = new Timeline(new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>() {
			private Iterator<List<List<LedView>>> frameIterator = compiledFrames.iterator();

			@Override
			public void handle(ActionEvent event) {
				if (!frameIterator.hasNext()) {
					this.frameIterator = compiledFrames.iterator();
				}

				int xOffset = 0;
				int yOffset = 0;
				final List<List<LedView>> frame = frameIterator.next();
				for (final List<LedView> ledRow : frame) {
					for (final LedView led : ledRow) {
						final RgbColorView rgbColor = led.getRgbColor();
						final Color rgb = Color.rgb(rgbColor.getR(), rgbColor.getG(), rgbColor.getB());
						graphics.setFill(rgb);
						graphics.fillOval(xOffset++ * LED_WIDTH, yOffset * LED_HEIGHT, LED_WIDTH, LED_HEIGHT);
					}
					xOffset = 0;
					yOffset++;
				}
			}
		}));

		animation.setCycleCount(Timeline.INDEFINITE);
		return animation;
	}

	public void startAnimation() {
		if (animation != null) {
			this.animation.play();
		}
	}

	public void stopAnimation() {
		if (animation != null) {
			this.animation.stop();
		}
	}
}