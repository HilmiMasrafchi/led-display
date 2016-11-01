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
import me.hmasrafchi.leddisplay.framework.generator.GeneratorLed;
import me.hmasrafchi.leddisplay.framework.generator.GeneratorLedUniformText;
import me.hmasrafchi.leddisplay.framework.scene.RandomColorScene;
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
		bind(Integer.class).annotatedWith(Names.named("matrixColumnsCount"))
				.toInstance(configuration.getMatrixColumnsCount());
		bind(Integer.class).annotatedWith(Names.named("matrixRowsCount"))
				.toInstance(configuration.getMatrixRowsCount());
		bind(Integer.class).annotatedWith(Names.named("delayBetweenFrames"))
				.toInstance(configuration.getDelayBetweenFrames());

		bind(String.class).annotatedWith(Names.named("generatorLedUniformText")).toInstance("●");
		bind(Double.class).annotatedWith(Names.named("generatorLedUniformTextSize")).toInstance(140d);

		bind(GeneratorLed.class).to(GeneratorLedUniformText.class);
		bind(Led.class).toProvider(ProviderLEDJFx.class);

		final List<List<Overlay.State>> statesOverlay1 = Arrays.asList(
				Arrays.asList(Overlay.State.STATIONARY_ON, Overlay.State.STATIONARY_ON, Overlay.State.STATIONARY_ON,
						Overlay.State.STATIONARY_ON, Overlay.State.STATIONARY_ON),
				Arrays.asList(Overlay.State.STATIONARY_OFF, Overlay.State.TRANSPARENT, Overlay.State.TRANSPARENT,
						Overlay.State.TRANSPARENT, Overlay.State.STATIONARY_OFF),
				Arrays.asList(Overlay.State.STATIONARY_ON, Overlay.State.TRANSPARENT, Overlay.State.TRANSPARENT,
						Overlay.State.TRANSPARENT, Overlay.State.STATIONARY_ON),
				Arrays.asList(Overlay.State.STATIONARY_OFF, Overlay.State.TRANSPARENT, Overlay.State.TRANSPARENT,
						Overlay.State.TRANSPARENT, Overlay.State.STATIONARY_OFF),
				Arrays.asList(Overlay.State.STATIONARY_ON, Overlay.State.TRANSPARENT, Overlay.State.TRANSPARENT,
						Overlay.State.TRANSPARENT, Overlay.State.STATIONARY_ON),
				Arrays.asList(Overlay.State.STATIONARY_ON, Overlay.State.STATIONARY_ON, Overlay.State.STATIONARY_ON,
						Overlay.State.STATIONARY_ON, Overlay.State.STATIONARY_ON));
		final Overlay overlay1 = new OverlayStationary(statesOverlay1, RgbColor.RED, RgbColor.ORANGE);

		final List<List<Overlay.State>> statesOverlay2 = Arrays.asList(
				Arrays.asList(Overlay.State.ROLL_ON, Overlay.State.ROLL_ON, Overlay.State.ROLL_ON,
						Overlay.State.ROLL_ON, Overlay.State.ROLL_ON, Overlay.State.ROLL_ON, Overlay.State.ROLL_ON),
				Arrays.asList(Overlay.State.ROLL_ON, Overlay.State.TRANSPARENT, Overlay.State.ROLL_ON,
						Overlay.State.TRANSPARENT, Overlay.State.ROLL_ON, Overlay.State.TRANSPARENT,
						Overlay.State.ROLL_ON),
				Arrays.asList(Overlay.State.ROLL_ON, Overlay.State.ROLL_ON, Overlay.State.ROLL_ON,
						Overlay.State.ROLL_ON, Overlay.State.ROLL_ON, Overlay.State.ROLL_ON, Overlay.State.ROLL_ON));
		final Overlay overlay2 = new OverlayRoll(statesOverlay2, RgbColor.GREEN, configuration.getCanvasYPosition(),
				configuration.getMatrixColumnsCount());

		final Scene firstScene = new OverlayedScene(Arrays.asList(overlay2, overlay1));

		final Scene secondScene = new RandomColorScene();
		bind(new TypeLiteral<Collection<Scene>>() {
		}).toInstance(Arrays.asList(firstScene, secondScene));

		bind(Board.class).to(BoardJFX.class);
	}
}