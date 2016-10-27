/**
 * 
 */
package me.hmasrafchi.leddisplay.framework.generator;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.api.Led.RgbColor;

/**
 * @author michelin
 *
 */
public final class GeneratorLedUniformText implements GeneratorLed {
	private final Provider<Led> provider;
	private final String text;
	private final double ledTextFontSize;

	@Inject
	public GeneratorLedUniformText(final Provider<Led> provider, @Named("generatorLedUniformText") final String text,
			@Named("ledTextFontSize") final double ledTextFontSize) {
		this.provider = provider;
		this.text = text;
		this.ledTextFontSize = ledTextFontSize;
	}

	@Override
	public Led next() {
		final Led led = provider.get();
		led.setText(text);
		led.setTextFontSize(ledTextFontSize);
		led.setRgbColor(RgbColor.BLACK);

		return led;
	}

	@Override
	public double getLedMaximumWidth() {
		return next().getWidth();
	}

	@Override
	public double getLedMaximumHeight() {
		return next().getHeight();
	}
}