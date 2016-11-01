/**
 * 
 */
package me.hmasrafchi.leddisplay.framework.generator;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import com.google.common.base.Preconditions;

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
			@Named("generatorLedUniformTextSize") final double ledTextFontSize) {
		this.provider = Preconditions.checkNotNull(provider);

		Preconditions.checkNotNull(text);
		Preconditions.checkArgument(!text.trim().isEmpty());
		this.text = text;

		Preconditions.checkArgument(ledTextFontSize > 0);
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