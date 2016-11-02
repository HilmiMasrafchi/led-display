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
import me.hmasrafchi.leddisplay.framework.scene.overlay.OverlayRollHorizontal;
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
				Arrays.asList(Overlay.State.ON, Overlay.State.ON, Overlay.State.ON, Overlay.State.ON, Overlay.State.ON),
				Arrays.asList(Overlay.State.OFF, Overlay.State.TRANSPARENT, Overlay.State.TRANSPARENT,
						Overlay.State.TRANSPARENT, Overlay.State.OFF),
				Arrays.asList(Overlay.State.ON, Overlay.State.TRANSPARENT, Overlay.State.TRANSPARENT,
						Overlay.State.TRANSPARENT, Overlay.State.ON),
				Arrays.asList(Overlay.State.OFF, Overlay.State.TRANSPARENT, Overlay.State.TRANSPARENT,
						Overlay.State.TRANSPARENT, Overlay.State.OFF),
				Arrays.asList(Overlay.State.ON, Overlay.State.TRANSPARENT, Overlay.State.TRANSPARENT,
						Overlay.State.TRANSPARENT, Overlay.State.ON),
				Arrays.asList(Overlay.State.ON, Overlay.State.ON, Overlay.State.ON, Overlay.State.ON,
						Overlay.State.ON));
		final Overlay overlay1 = new OverlayStationary(statesOverlay1, RgbColor.RED, RgbColor.ORANGE);

		final List<List<Overlay.State>> statesOverlay2 = Arrays.asList(
				Arrays.asList(Overlay.State.ON, Overlay.State.ON, Overlay.State.ON, Overlay.State.ON, Overlay.State.ON,
						Overlay.State.ON, Overlay.State.ON),
				Arrays.asList(Overlay.State.ON, Overlay.State.TRANSPARENT, Overlay.State.ON, Overlay.State.TRANSPARENT,
						Overlay.State.ON, Overlay.State.TRANSPARENT, Overlay.State.ON),
				Arrays.asList(Overlay.State.ON, Overlay.State.ON, Overlay.State.ON, Overlay.State.ON, Overlay.State.ON,
						Overlay.State.ON, Overlay.State.ON));
		final Overlay overlay2 = new OverlayRollHorizontal(statesOverlay2, RgbColor.GREEN,
				configuration.getCanvasYPosition(), configuration.getMatrixColumnsCount());

		final Scene firstScene = new OverlayedScene(Arrays.asList(overlay2, overlay1));

		final List<Led.RgbColor> rainbowColors = Arrays.asList(Led.RgbColor.INDIGO, Led.RgbColor.BLUE,
				Led.RgbColor.GREEN, Led.RgbColor.YELLOW, Led.RgbColor.ORANGE, Led.RgbColor.RED);
		final Scene secondScene = new RandomColorScene(rainbowColors);
		bind(new TypeLiteral<Collection<Scene>>() {
		}).toInstance(Arrays.asList(firstScene, secondScene));

		bind(Board.class).to(BoardJFX.class);
	}
}