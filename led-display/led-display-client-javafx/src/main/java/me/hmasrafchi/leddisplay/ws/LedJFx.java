/**
 * 
 */
package me.hmasrafchi.leddisplay.ws;

import javafx.geometry.VPos;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

/**
 * @author michelin
 *
 */
public final class LedJFx extends Text {
	public LedJFx() {
		setTextOrigin(VPos.TOP);
		setBoundsType(TextBoundsType.VISUAL);
		setText("█");
		setFont(Font.font(80));
	}
}