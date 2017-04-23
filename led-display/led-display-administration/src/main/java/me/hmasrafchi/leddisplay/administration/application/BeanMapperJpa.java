/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application;

import static java.util.Optional.of;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import me.hmasrafchi.leddisplay.administration.model.jpa.FrameEntity;
import me.hmasrafchi.leddisplay.administration.model.jpa.LedEmbeddable;
import me.hmasrafchi.leddisplay.administration.model.jpa.LedRowEntity;
import me.hmasrafchi.leddisplay.administration.model.jpa.LedStateEntity;
import me.hmasrafchi.leddisplay.administration.model.jpa.LedStateRowEntity;
import me.hmasrafchi.leddisplay.administration.model.jpa.MatrixEntity;
import me.hmasrafchi.leddisplay.administration.model.jpa.OverlayEntity;
import me.hmasrafchi.leddisplay.administration.model.jpa.OverlayRollHorizontallyEntity;
import me.hmasrafchi.leddisplay.administration.model.jpa.OverlayStationaryEntity;
import me.hmasrafchi.leddisplay.administration.model.jpa.RgbColorEmbeddable;
import me.hmasrafchi.leddisplay.administration.model.jpa.SceneEntity;
import me.hmasrafchi.leddisplay.administration.model.view.LedStateView;
import me.hmasrafchi.leddisplay.administration.model.view.MatrixView;
import me.hmasrafchi.leddisplay.administration.model.view.OverlayRollHorizontallyView;
import me.hmasrafchi.leddisplay.administration.model.view.OverlayStationaryView;
import me.hmasrafchi.leddisplay.administration.model.view.OverlayView;
import me.hmasrafchi.leddisplay.domain.CompiledFrames;
import me.hmasrafchi.leddisplay.domain.Led;
import me.hmasrafchi.leddisplay.domain.Matrix;
import me.hmasrafchi.leddisplay.domain.Overlay;
import me.hmasrafchi.leddisplay.domain.OverlayRollHorizontally;
import me.hmasrafchi.leddisplay.domain.OverlayStationary;
import me.hmasrafchi.leddisplay.domain.RgbColor;
import me.hmasrafchi.leddisplay.domain.Scene;
import me.hmasrafchi.leddisplay.domain.SceneComposite;
import me.hmasrafchi.leddisplay.domain.SceneOverlayed;
import me.hmasrafchi.leddisplay.domain.event.LedView;
import me.hmasrafchi.leddisplay.domain.event.RgbColorView;

/**
 * @author michelin
 *
 */
public class BeanMapperJpa implements BeanMapper<MatrixEntity> {
	public Matrix mapMatrixFromViewToDomainModel(final MatrixView matrixView) {
		final int rowCount = matrixView.getRowCount();
		final int columnCount = matrixView.getColumnCount();
		final Optional<Scene> scene = mapScenesFromViewToDomainModel(matrixView.getScenes());

		return new Matrix(rowCount, columnCount, scene);
	}

	private Optional<Scene> mapScenesFromViewToDomainModel(final List<List<OverlayView>> scenesView) {
		if (scenesView == null || scenesView.isEmpty()) {
			return Optional.empty();
		}

		final List<SceneOverlayed> scenesDomain = scenesView.stream().map(sceneView -> {
			final List<Overlay> overlays = sceneView.stream().map(overlayView -> {
				if (overlayView instanceof OverlayStationaryView) {
					final OverlayStationaryView overlayStationaryView = (OverlayStationaryView) overlayView;

					final List<List<Led.State>> ledStatesDomain = mapLedStatesFromViewToDomainModel(
							overlayStationaryView.getStates());
					final RgbColor onColorDomain = mapRgbColorFromViewToModel(overlayStationaryView.getOnColor());
					final RgbColor offColorDomain = mapRgbColorFromViewToModel(overlayStationaryView.getOffColor());
					final int durationDomain = overlayStationaryView.getDuration();

					return new OverlayStationary(ledStatesDomain, onColorDomain, offColorDomain, durationDomain);
				} else if (overlayView instanceof OverlayRollHorizontallyView) {
					final OverlayRollHorizontallyView overlayRollView = (OverlayRollHorizontallyView) overlayView;

					final List<List<Led.State>> ledStatesDomain = mapLedStatesFromViewToDomainModel(
							overlayRollView.getStates());
					final RgbColor onColorDomain = mapRgbColorFromViewToModel(overlayRollView.getOnColor());
					final RgbColor offColorDomain = mapRgbColorFromViewToModel(overlayRollView.getOffColor());
					final int beginIndexMarkDomain = overlayRollView.getBeginIndexMark();
					final int ypositionDomain = overlayRollView.getYposition();

					return new OverlayRollHorizontally(ledStatesDomain, onColorDomain, offColorDomain,
							beginIndexMarkDomain, ypositionDomain);
				} else {
					throw new BeanMapperException(
							"can not find proper domain implementation for overlay: " + overlayView);
				}
			}).collect(Collectors.toList());
			return new SceneOverlayed(overlays);
		}).collect(Collectors.toList());

		final Scene sceneDomain = new SceneComposite(scenesDomain);
		return of(sceneDomain);
	}

	private RgbColor mapRgbColorFromViewToModel(final RgbColorView onColorView) {
		return new RgbColor(onColorView.getR(), onColorView.getG(), onColorView.getB());
	}

	private List<List<Led.State>> mapLedStatesFromViewToDomainModel(final List<List<LedStateView>> ledStatesView) {
		return ledStatesView.stream().map(ledStateRowView -> {
			return ledStateRowView.stream().map(ledStateView -> {
				return Led.State.valueOf(ledStateView.name());
			}).collect(Collectors.toList());
		}).collect(Collectors.toList());
	}

	@Override
	public MatrixEntity mapMatrixFromViewToDataModel(final MatrixView matrixView,
			final Optional<CompiledFrames> compiledFrames) {
		final Integer matrixId = matrixView.getId();
		final String name = matrixView.getName();
		final int rowCount = matrixView.getRowCount();
		final int columnCount = matrixView.getColumnCount();
		final List<SceneEntity> scenes = mapScenesFromViewToDataModel(matrixView.getScenes());
		final List<FrameEntity> compiledFramesData = mapCompiledFramesFromDomainToDataModel(compiledFrames);

		return new MatrixEntity(matrixId, name, rowCount, columnCount, scenes, compiledFramesData);
	}

	private List<FrameEntity> mapCompiledFramesFromDomainToDataModel(final Optional<CompiledFrames> compiledFrames) {
		return compiledFrames.map(frames -> {
			return frames.stream().map(frame -> {
				final List<LedRowEntity> frameList = frame.getFrameData().stream().map(frameRow -> {
					final List<LedEmbeddable> ledRowList = frameRow.stream().map(led -> {
						final RgbColor rgbColorDomain = led.getRgbColor();
						final RgbColorEmbeddable rgbColorData = new RgbColorEmbeddable(rgbColorDomain.getR(),
								rgbColorDomain.getG(), rgbColorDomain.getB());
						return new LedEmbeddable(led.getText(), rgbColorData);
					}).collect(Collectors.toList());
					return new LedRowEntity(ledRowList);
				}).collect(Collectors.toList());
				return new FrameEntity(frameList);
			}).collect(Collectors.toList());
		}).orElse(null);
	}

	private List<SceneEntity> mapScenesFromViewToDataModel(final List<List<OverlayView>> scenesView) {
		if (scenesView == null) {
			return null;
		}

		return scenesView.stream().map(scene -> {
			final List<OverlayEntity> overlays = scene.stream().map(overlay -> {
				if (overlay instanceof OverlayStationaryView) {
					final OverlayStationaryView overlayStationaryView = (OverlayStationaryView) overlay;

					final List<LedStateRowEntity> ledStatesData = mapStatesFromViewToDataModel(
							overlayStationaryView.getStates());
					final RgbColorEmbeddable onColorEntity = mapRgbColorFromViewToDataModel(
							overlayStationaryView.getOnColor());
					final RgbColorEmbeddable offColorEntity = mapRgbColorFromViewToDataModel(
							overlayStationaryView.getOffColor());
					final int durationView = overlayStationaryView.getDuration();

					return new OverlayStationaryEntity(ledStatesData, onColorEntity, offColorEntity, durationView);
				}

				if (overlay instanceof OverlayRollHorizontallyView) {
					final OverlayRollHorizontallyView overlayRollHorizontallyView = (OverlayRollHorizontallyView) overlay;
					final List<LedStateRowEntity> ledStatesData = mapStatesFromViewToDataModel(
							overlayRollHorizontallyView.getStates());
					final RgbColorEmbeddable onColorEntity = mapRgbColorFromViewToDataModel(
							overlayRollHorizontallyView.getOnColor());
					final RgbColorEmbeddable offColorEntity = mapRgbColorFromViewToDataModel(
							overlayRollHorizontallyView.getOffColor());
					final int beginIndexView = overlayRollHorizontallyView.getBeginIndexMark();
					final int ypositionView = overlayRollHorizontallyView.getYposition();

					return new OverlayRollHorizontallyEntity(ledStatesData, onColorEntity, offColorEntity,
							beginIndexView, ypositionView);
				}

				throw new BeanMapperException("can not find proper domain implementation for overlay: " + overlay);
			}).collect(Collectors.toList());
			return new SceneEntity(overlays);
		}).collect(Collectors.toList());
	}

	private RgbColorEmbeddable mapRgbColorFromViewToDataModel(final RgbColorView rgbColor) {
		return new RgbColorEmbeddable(rgbColor.getR(), rgbColor.getG(), rgbColor.getB());
	}

	private List<LedStateRowEntity> mapStatesFromViewToDataModel(final List<List<LedStateView>> statesView) {
		final List<LedStateRowEntity> currentStatesData = new ArrayList<>();
		for (final List<LedStateView> currentStateRowView : statesView) {
			final List<LedStateEntity> currentStateRowData = new ArrayList<>();
			for (final LedStateView currentStateView : currentStateRowView) {
				final LedStateEntity currentStateData = LedStateEntity.valueOf(currentStateView.name());
				currentStateRowData.add(currentStateData);
			}
			currentStatesData.add(new LedStateRowEntity(currentStateRowData));
		}

		return currentStatesData;
	}

	@Override
	public MatrixView mapMatrixFromDataToViewModel(final MatrixEntity matrixEntity) {
		final Integer matrixId = matrixEntity.getId();
		final String name = matrixEntity.getName();
		final int rowCount = matrixEntity.getRowCount();
		final int columnCount = matrixEntity.getColumnCount();

		final List<List<OverlayView>> scenes = mapScenesFromDataToViewModel(matrixEntity.getScenes());
		final List<List<List<LedView>>> compiledFrames = mapCompiledFramesFromDataToViewModel(
				matrixEntity.getCompiledFrames());
		return new MatrixView(matrixId, name, rowCount, columnCount, scenes, compiledFrames);
	}

	private List<List<List<LedView>>> mapCompiledFramesFromDataToViewModel(final List<FrameEntity> compiledFrames) {
		if (compiledFrames == null) {
			return null;
		}

		return compiledFrames.stream().map(frame -> {
			return frame.getLedRows().stream().map(ledRow -> {
				return ledRow.getLeds().stream().map(led -> {
					final String text = led.getText();
					final RgbColorEmbeddable rgbColor = led.getRgbColor();
					final RgbColorView rgbColorView = new RgbColorView(rgbColor.getR(), rgbColor.getG(),
							rgbColor.getB());
					return new LedView(text, rgbColorView);
				}).collect(Collectors.toList());
			}).collect(Collectors.toList());
		}).collect(Collectors.toList());
	}

	private List<List<OverlayView>> mapScenesFromDataToViewModel(final List<SceneEntity> scenes) {
		if (scenes == null) {
			return null;
		}

		return scenes.stream().map(scene -> {
			return scene.getOverlays().stream().map(overlay -> {
				if (overlay instanceof OverlayStationaryEntity) {
					final OverlayStationaryEntity overlayStationary = (OverlayStationaryEntity) overlay;
					final List<List<LedStateView>> states = mapLedStatesFromDataToViewMode(
							overlayStationary.getStates());
					final RgbColorView onColorView = mapRgbColorFromDataToViewModel(overlayStationary.getOnColor());
					final RgbColorView offColorView = mapRgbColorFromDataToViewModel(overlayStationary.getOffColor());
					final int duration = overlayStationary.getDuration();

					return new OverlayStationaryView(states, onColorView, offColorView, duration);
				}

				if (overlay instanceof OverlayRollHorizontallyEntity) {
					final OverlayRollHorizontallyEntity overlayRollHorizontallyEntity = (OverlayRollHorizontallyEntity) overlay;
					final List<List<LedStateView>> states = mapLedStatesFromDataToViewMode(
							overlayRollHorizontallyEntity.getStates());
					final RgbColorView onColorView = mapRgbColorFromDataToViewModel(
							overlayRollHorizontallyEntity.getOnColor());
					final RgbColorView offColorView = mapRgbColorFromDataToViewModel(
							overlayRollHorizontallyEntity.getOffColor());
					final int beginIndexMark = overlayRollHorizontallyEntity.getBeginIndexMark();
					final int yposition = overlayRollHorizontallyEntity.getYPosition();

					return new OverlayRollHorizontallyView(states, onColorView, offColorView, beginIndexMark,
							yposition);
				}

				throw new BeanMapperException("can not find proper domain implementation for overlay: " + overlay);
			}).collect(Collectors.<OverlayView>toList());
		}).collect(Collectors.toList());
	}

	private RgbColorView mapRgbColorFromDataToViewModel(final RgbColorEmbeddable rgbColor) {
		return new RgbColorView(rgbColor.getR(), rgbColor.getG(), rgbColor.getB());
	}

	private List<List<LedStateView>> mapLedStatesFromDataToViewMode(final List<LedStateRowEntity> states) {
		return states.stream().map(stateRow -> {
			return stateRow.getLedStates().stream().map(state -> {
				return LedStateView.valueOf(state.name());
			}).collect(Collectors.toList());
		}).collect(Collectors.toList());
	}
}