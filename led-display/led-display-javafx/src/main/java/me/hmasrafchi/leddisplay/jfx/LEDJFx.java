/**
 * 
 */
package me.hmasrafchi.leddisplay.jfx;

import javafx.geometry.VPos;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import me.hmasrafchi.leddisplay.model.Led;

/**
 * @author michelin
 *
 */
public final class LEDJFx extends Text implements Led {
	public LEDJFx() {
		setTextOrigin(VPos.TOP);
		setBoundsType(TextBoundsType.VISUAL);
		reset();
	}

	@Override
	public void setCoordinateX(double x) {
		setX(x);
	}

	@Override
	public void setCoordinateY(double y) {
		setY(y);
	}

	@Override
	public double getHeight() {
		return getLayoutBounds().getHeight();
	}

	@Override
	public double getWidth() {
		return getLayoutBounds().getWidth();
	}

	@Override
	public void setOpacityLevels(double opacity) {
		setOpacity(opacity);
	}

	@Override
	public void setRgbColor(final RgbColor rgbColor) {
		setFill(Color.rgb(rgbColor.getR(), rgbColor.getG(), rgbColor.getB()));
	}

	@Override
	public void reset() {
		setOpacity(1);
		setRgbColor(RgbColor.BLACK);
	}

	@Override
	public RgbColor getRgbColor() {
		final Color c = (Color) getFill();
		return new RgbColor((int) c.getRed() * 255, (int) c.getGreen() * 255, (int) c.getBlue() * 255);
	}

	@Override
	public void setTextFontSize(final double ledTextFontSize) {
		setFont(Font.font(ledTextFontSize));
	}
}