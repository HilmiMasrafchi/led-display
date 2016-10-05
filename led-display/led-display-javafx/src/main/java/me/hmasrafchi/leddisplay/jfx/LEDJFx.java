/**
 * 
 */
package me.hmasrafchi.leddisplay.jfx;

import javafx.geometry.VPos;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import me.hmasrafchi.leddisplay.api.Led;

/**
 * @author michelin
 *
 */
public final class LEDJFx extends Text implements Led {
	public LEDJFx() {
		setText("●");
		setFont(Font.font(30));
		setTextOrigin(VPos.TOP);
		setBoundsType(TextBoundsType.VISUAL);
	}

	@Override
	public void setCoordinateX(double x) {
		setX(x);
	}

	@Override
	public double getCoordinateX() {
		return getX();
	}

	@Override
	public void setCoordinateY(double y) {
		setY(y);
	}

	@Override
	public double getCoordinateY() {
		return getY();
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
}