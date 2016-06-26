/**
 * 
 */
package me.hmasrafchi.leddisplay.jfx;

import com.google.inject.AbstractModule;

import me.hmasrafchi.leddisplay.api.LED;

/**
 * @author hmasrafchi
 *
 */
public final class DefaultModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(LED.class).toProvider(ProviderLEDJFx.class);
	}
}