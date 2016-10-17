package me.hmasrafchi.leddisplay.jfx;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

import lombok.RequiredArgsConstructor;
import me.hmasrafchi.leddisplay.api.Board;
import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.api.Led.RgbColor;
import me.hmasrafchi.leddisplay.framework.scene.Scene;
import me.hmasrafchi.leddisplay.framework.scene.overlay.Overlay;
import me.hmasrafchi.leddisplay.framework.scene.overlay.OverlayRoll;
import me.hmasrafchi.leddisplay.framework.scene.overlay.OverlayStationary;
import me.hmasrafchi.leddisplay.framework.scene.overlay.OverlayedScene;

@RequiredArgsConstructor
public final class DefaultModule extends AbstractModule {
	private final Configuration configuration;

	@Override
	protected void configure() {
		bind(Integer.class).annotatedWith(Names.named("columnsCount"))
				.toInstance(configuration.getMatrixColumnsCount());
		bind(Integer.class).annotatedWith(Names.named("rowsCount")).toInstance(configuration.getMatrixRowsCount());
		bind(Integer.class).annotatedWith(Names.named("delayBetweenFrames"))
				.toInstance(configuration.getDelayBetweenFrames());

		bind(Led.class).toProvider(ProviderLEDJFx.class);

		final List<List<Overlay.State>> statesOverlay1 = Arrays.asList(Arrays.asList(Overlay.State.STATIONARY_ON),
				Arrays.asList(Overlay.State.STATIONARY_ON));
		final Overlay overlay1 = new OverlayStationary(statesOverlay1, RgbColor.RED, RgbColor.ORANGE);

		final List<List<Overlay.State>> statesOverlay2 = Arrays.asList(
				Arrays.asList(Overlay.State.ROLL_ON, Overlay.State.ROLL_ON),
				Arrays.asList(Overlay.State.ROLL_ON, Overlay.State.ROLL_ON));
		final Overlay overlay2 = new OverlayRoll(statesOverlay2, RgbColor.GREEN, 0,
				configuration.getMatrixColumnsCount());

		final Scene secondScene = new OverlayedScene(Arrays.asList(overlay2, overlay1));
		bind(new TypeLiteral<Collection<Scene>>() {
		}).toInstance(Arrays.asList(secondScene));

		bind(Board.class).to(BoardJFX.class);
	}
}