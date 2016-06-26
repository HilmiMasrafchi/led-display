/**
 * 
 */
package me.hmasrafchi.leddisplay.jfx;

import javafx.geometry.VPos;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import me.hmasrafchi.leddisplay.api.LED;

/**
 * @author michelin
 *
 */
public final class LEDJFx extends Text implements LED {
	public LEDJFx() {
		if (Math.random() > 0.5)
			setText("◌");
		else
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
}