/**
 * 
 */
package me.hmasrafchi.leddisplay.jfx;

import java.util.Arrays;
import java.util.Collection;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

import lombok.RequiredArgsConstructor;
import me.hmasrafchi.leddisplay.api.Board;
import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.framework.scene.RightToLeftCanvasScene;
import me.hmasrafchi.leddisplay.framework.scene.RandomColorLedScene;
import me.hmasrafchi.leddisplay.framework.scene.Scene;

/**
 * @author hmasrafchi
 *
 */
@RequiredArgsConstructor
public final class DefaultModule extends AbstractModule {
	private final Configuration configuration;

	@Override
	protected void configure() {
		bind(Integer.class).annotatedWith(Names.named("columnsCount"))
				.toInstance(configuration.getMatrixColumnsCount());
		bind(Integer.class).annotatedWith(Names.named("rowsCount")).toInstance(configuration.getMatrixRowsCount());

		bind(Led.class).toProvider(ProviderLEDJFx.class);
		final Scene canvasScene = new RightToLeftCanvasScene(0, configuration.getMatrixColumnsCount());
		final Scene randomColorsScene = new RandomColorLedScene();
		bind(new TypeLiteral<Collection<Scene>>() {
		}).toInstance(Arrays.asList(canvasScene, randomColorsScene));

		bind(Board.class).to(BoardJFX.class);
	}
}