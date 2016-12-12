/**
 * 
 */
package me.hmasrafchi.leddisplay.jfx.gui;

import javafx.geometry.Insets;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import me.hmasrafchi.leddisplay.jfx.ColorUtils;
import me.hmasrafchi.leddisplay.model.Led.RgbColor;
import me.hmasrafchi.leddisplay.model.scene.overlay.Overlay;
import me.hmasrafchi.leddisplay.model.scene.overlay.OverlayStationary;

/**
 * @author michelin
 *
 */
final class OverlayStationaryGUI extends VBox implements Model<Overlay> {
	private final OverlayBaseGUI overlayBaseGui;
	private final ColorPicker onColorPicker;
	private final ColorPicker offColorPicker;

	OverlayStationaryGUI(final OverlayStationary overlayStationary) {
		this.overlayBaseGui = new OverlayBaseGUI(overlayStationary.getStates());

		final RgbColor onColor = overlayStationary.getOnColor();
		final Color jfxOnColor = ColorUtils.toJavaFxColor(onColor);
		final Label onColorLabel = new Label("On color: ");
		this.onColorPicker = new ColorPicker(jfxOnColor);

		final RgbColor offColor = overlayStationary.getOffColor();
		final Color jfxoffColor = ColorUtils.toJavaFxColor(offColor);
		final Label offColorLabel = new Label("Off color: ");
		this.offColorPicker = new ColorPicker(jfxoffColor);

		final HBox hbox = new HBox(onColorLabel, onColorPicker, offColorLabel, offColorPicker);
		hbox.setPadding(new Insets(10));

		getChildren().addAll(hbox, overlayBaseGui);
	}

	@Override
	public Overlay getModel() {
		return new OverlayStationary(overlayBaseGui.getStates(), ColorUtils.toLedRgbColor(onColorPicker.getValue()),
				ColorUtils.toLedRgbColor(offColorPicker.getValue()));
	}
}