/**
 * 
 */
package me.hmasrafchi.leddisplay.jfx;

import javax.inject.Provider;

import me.hmasrafchi.leddisplay.model.api.Led;

/**
 * @author michelin
 *
 */
public final class ProviderLEDJFx implements Provider<Led> {
	@Override
	public Led get() {
		return new LEDJFx();
	}
}