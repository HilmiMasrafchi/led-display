/**
 * 
 */
package me.hmasrafchi.leddisplay.jfx;

import com.google.inject.AbstractModule;

import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.framework.Configuration;

/**
 * @author hmasrafchi
 *
 */
public final class DefaultModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(Led.class).toProvider(ProviderLEDJFx.class);
		bind(Configuration.class).toInstance(Configuration.builder().boardColumnsCount(50).boardRowsCount(25).build());
	}
}