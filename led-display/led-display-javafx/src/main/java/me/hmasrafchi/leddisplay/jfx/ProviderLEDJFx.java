/**
 * 
 */
package me.hmasrafchi.leddisplay.jfx;

import javax.inject.Provider;

import me.hmasrafchi.leddisplay.api.LED;

/**
 * @author michelin
 *
 */
public final class ProviderLEDJFx implements Provider<LED> {
	@Override
	public LED get() {
		return new LEDJFx();
	}
}