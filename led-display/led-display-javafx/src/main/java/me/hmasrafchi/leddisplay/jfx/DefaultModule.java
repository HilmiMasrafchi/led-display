/**
 * 
 */
package me.hmasrafchi.leddisplay.jfx;

import java.util.Arrays;
import java.util.Collection;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

import me.hmasrafchi.leddisplay.framework.Configuration;
import me.hmasrafchi.leddisplay.framework.Led;
import me.hmasrafchi.leddisplay.framework.Scene;
import me.hmasrafchi.leddisplay.framework.scene.RandomColorLedScene;

/**
 * @author hmasrafchi
 *
 */
public final class DefaultModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(Led.class).toProvider(ProviderLEDJFx.class);
		bind(Configuration.class).toInstance(Configuration.builder().boardColumnsCount(75).boardRowsCount(50).build());
		bind(new TypeLiteral<Collection<? extends Scene>>() {
		}).toInstance(Arrays.asList(new RandomColorLedScene()));
	}
}