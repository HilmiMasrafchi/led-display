/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application;

import static java.util.Optional.of;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import me.hmasrafchi.leddisplay.administration.model.domain.Led;
import me.hmasrafchi.leddisplay.administration.model.domain.Matrix;
import me.hmasrafchi.leddisplay.administration.model.domain.Overlay;
import me.hmasrafchi.leddisplay.administration.model.domain.OverlayRollHorizontally;
import me.hmasrafchi.leddisplay.administration.model.domain.OverlayStationary;
import me.hmasrafchi.leddisplay.administration.model.domain.RgbColor;
import me.hmasrafchi.leddisplay.administration.model.domain.Scene;
import me.hmasrafchi.leddisplay.administration.model.domain.SceneComposite;
import me.hmasrafchi.leddisplay.administration.model.domain.SceneOverlayed;
import me.hmasrafchi.leddisplay.administration.model.jpa.LedStateEntity;
import me.hmasrafchi.leddisplay.administration.model.jpa.LedStateRowEntity;
import me.hmasrafchi.leddisplay.administration.model.jpa.MatrixEntity;
import me.hmasrafchi.leddisplay.administration.model.jpa.OverlayEntity;
import me.hmasrafchi.leddisplay.administration.model.jpa.OverlayRollHorizontallyEntity;
import me.hmasrafchi.leddisplay.administration.model.jpa.OverlayStationaryEntity;
import me.hmasrafchi.leddisplay.administration.model.jpa.SceneEntity;
import me.hmasrafchi.leddisplay.administration.model.view.LedStateView;
import me.hmasrafchi.leddisplay.administration.model.view.MatrixView;
import me.hmasrafchi.leddisplay.administration.model.view.OverlayRollHorizontallyView;
import me.hmasrafchi.leddisplay.administration.model.view.OverlayStationaryView;
import me.hmasrafchi.leddisplay.administration.model.view.OverlayView;
import me.hmasrafchi.leddisplay.administration.model.view.RgbColorView;

/**
 * @author michelin
 *
 */
public class BeanMapperJpa implements BeanMapper<MatrixEntity> {
	public Matrix mapMatrixFromViewToDomainModel(final MatrixView matrixView) {
		final int rowCount = matrixView.getRowCount();
		final int columnCount = matrixView.getColumnCount();

		final List<List<OverlayView>> scenes = matrixView.getScenes();
		final Optional<Scene> sceneOptional = mapScenesFromViewToDomainModel(scenes);

		return new Matrix(rowCount, columnCount, sceneOptional);
	}

	private Optional<Scene> mapScenesFromViewToDomainModel(final List<List<OverlayView>> scenesView) {
		if (scenesView == null || scenesView.isEmpty()) {
			return Optional.empty();
		}

		final List<Scene> scenes = new ArrayList<>();
		for (final List<OverlayView> currentSceneView : scenesView) {
			final List<Overlay> overlays = new ArrayList<>();
			for (final OverlayView currentOverlayView : currentSceneView) {
				if (currentOverlayView instanceof OverlayStationaryView) {
					final OverlayStationaryView currentOverlayStationaryView = (OverlayStationaryView) currentOverlayView;

					final List<List<Led.State>> currentStatesDomain = mapStatesFromViewToDomainModel(
							currentOverlayStationaryView.getStates());
					final RgbColor onColorDomain = mapRgbColorFromViewToModel(
							currentOverlayStationaryView.getOnColor());
					final RgbColor offColorDomain = mapRgbColorFromViewToModel(
							currentOverlayStationaryView.getOffColor());
					final int durationDomain = currentOverlayStationaryView.getDuration();

					final OverlayStationary overlayStationary = new OverlayStationary(currentStatesDomain,
							onColorDomain, offColorDomain, durationDomain);

					overlays.add(overlayStationary);
				} else if (currentOverlayView instanceof OverlayRollHorizontallyView) {
					final OverlayRollHorizontallyView currentOverlayRollView = (OverlayRollHorizontallyView) currentOverlayView;

					final List<List<Led.State>> currentStatesDomain = mapStatesFromViewToDomainModel(
							currentOverlayRollView.getStates());
					final RgbColor onColorDomain = mapRgbColorFromViewToModel(currentOverlayRollView.getOnColor());
					final RgbColor offColorDomain = mapRgbColorFromViewToModel(currentOverlayRollView.getOffColor());
					final int beginIndexMarkDomain = currentOverlayRollView.getBeginIndexMark();
					final int yposition = currentOverlayRollView.getYposition();

					final OverlayRollHorizontally overlayRoll = new OverlayRollHorizontally(currentStatesDomain,
							onColorDomain, offColorDomain, beginIndexMarkDomain, yposition);

					overlays.add(overlayRoll);
				} else {
					throw new BeanMapperException(
							"can not find proper domain implementation for overlay: " + currentOverlayView);
				}

				scenes.add(new SceneOverlayed(overlays));
			}
		}

		final SceneComposite sceneDomain = new SceneComposite(scenes);
		return of(sceneDomain);
	}

	private RgbColor mapRgbColorFromViewToModel(final RgbColorView onColorView) {
		final RgbColor onColorDomain = new RgbColor(onColorView.getR(), onColorView.getG(), onColorView.getB());
		return onColorDomain;
	}

	private List<List<Led.State>> mapStatesFromViewToDomainModel(final List<List<LedStateView>> currentStatesView) {
		final List<List<Led.State>> currentStatesDomain = new ArrayList<>();
		for (final List<LedStateView> currentStateRowView : currentStatesView) {
			final List<Led.State> currentStateRowDomain = new ArrayList<>();
			for (final LedStateView currentStateView : currentStateRowView) {
				final Led.State currentStateDomain = Led.State.valueOf(currentStateView.name());
				currentStateRowDomain.add(currentStateDomain);
			}
			currentStatesDomain.add(currentStateRowDomain);
		}
		return currentStatesDomain;
	}

	@Override
	public void mapMatrixViewToDataModel(final MatrixEntity matrixEntity, final MatrixView matrixView) {
		final int rowCountView = matrixView.getRowCount();
		matrixEntity.setRowCount(rowCountView);

		final int columnCountView = matrixView.getColumnCount();
		matrixEntity.setColumnCount(columnCountView);

		final List<SceneEntity> scenes = mapScenesFromViewToDataModel(matrixEntity, matrixView.getScenes());
		matrixEntity.setScenes(scenes);
	}

	private List<SceneEntity> mapScenesFromViewToDataModel(final MatrixEntity matrixEntity,
			final List<List<OverlayView>> scenesView) {
		if (scenesView == null) {
			return null;
		}

		final List<SceneEntity> scenesModel = new ArrayList<>();
		for (final List<OverlayView> sceneView : scenesView) {
			final List<OverlayEntity> overlaysModel = new ArrayList<>();
			for (final OverlayView currentOverlayView : sceneView) {
				if (currentOverlayView instanceof OverlayStationaryView) {
					final OverlayStationaryView currentOverlayStationaryView = (OverlayStationaryView) currentOverlayView;

					final List<LedStateRowEntity> currentStatesData = mapStatesFromViewToDataModel(
							currentOverlayStationaryView.getStates());

					final RgbColorView onColorView = currentOverlayStationaryView.getOnColor();
					me.hmasrafchi.leddisplay.administration.model.jpa.RgbColor onColorEntity = new me.hmasrafchi.leddisplay.administration.model.jpa.RgbColor(
							onColorView.getR(), onColorView.getG(), onColorView.getB());

					final RgbColorView offColorView = currentOverlayStationaryView.getOffColor();
					me.hmasrafchi.leddisplay.administration.model.jpa.RgbColor offColorEntity = new me.hmasrafchi.leddisplay.administration.model.jpa.RgbColor(
							offColorView.getR(), offColorView.getG(), offColorView.getB());

					final int durationView = currentOverlayStationaryView.getDuration();

					final OverlayStationaryEntity overlayStationaryEntity = new OverlayStationaryEntity(
							currentStatesData, onColorEntity, offColorEntity, durationView);

					overlaysModel.add(overlayStationaryEntity);
				}

				if (currentOverlayView instanceof OverlayRollHorizontallyView) {
					final OverlayRollHorizontallyView currentOverlayStationaryView = (OverlayRollHorizontallyView) currentOverlayView;

					final List<LedStateRowEntity> currentStatesData = mapStatesFromViewToDataModel(
							currentOverlayStationaryView.getStates());

					final RgbColorView onColorView = currentOverlayStationaryView.getOnColor();
					me.hmasrafchi.leddisplay.administration.model.jpa.RgbColor onColorEntity = new me.hmasrafchi.leddisplay.administration.model.jpa.RgbColor(
							onColorView.getR(), onColorView.getG(), onColorView.getB());

					final RgbColorView offColorView = currentOverlayStationaryView.getOffColor();
					me.hmasrafchi.leddisplay.administration.model.jpa.RgbColor offColorEntity = new me.hmasrafchi.leddisplay.administration.model.jpa.RgbColor(
							offColorView.getR(), offColorView.getG(), offColorView.getB());

					final int beginIndexView = currentOverlayStationaryView.getBeginIndexMark();

					final int ypositionView = currentOverlayStationaryView.getYposition();

					final OverlayRollHorizontallyEntity overlayRollHorizontallyEntity = new OverlayRollHorizontallyEntity(
							currentStatesData, onColorEntity, offColorEntity, beginIndexView, ypositionView);

					overlaysModel.add(overlayRollHorizontallyEntity);
				}
			}

			final SceneEntity sceneEntity = new SceneEntity(overlaysModel);
			scenesModel.add(sceneEntity);
		}

		return scenesModel;
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
}