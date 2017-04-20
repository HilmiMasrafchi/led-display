/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import me.hmasrafchi.leddisplay.administration.application.BeanMapper;
import me.hmasrafchi.leddisplay.administration.model.jpa.LedStateRowEntity;
import me.hmasrafchi.leddisplay.administration.model.jpa.MatrixEntity;
import me.hmasrafchi.leddisplay.administration.model.jpa.OverlayRollHorizontallyEntity;
import me.hmasrafchi.leddisplay.administration.model.jpa.OverlayStationaryEntity;
import me.hmasrafchi.leddisplay.administration.model.jpa.RgbColor;
import me.hmasrafchi.leddisplay.administration.model.jpa.SceneEntity;
import me.hmasrafchi.leddisplay.administration.model.view.LedStateView;
import me.hmasrafchi.leddisplay.administration.model.view.MatrixView;
import me.hmasrafchi.leddisplay.administration.model.view.OverlayRollHorizontallyView;
import me.hmasrafchi.leddisplay.administration.model.view.OverlayStationaryView;
import me.hmasrafchi.leddisplay.administration.model.view.OverlayView;

/**
 * @author michelin
 *
 */
public class MatrixRepositoryJpa implements MatrixRepository {
	@PersistenceContext
	private EntityManager em;

	@Inject
	private BeanMapper<MatrixEntity> beanMapper;

	@Override
	public Object create(final MatrixView matrixView) {
		beanMapper.mapMatrixFromViewToDomainModel(matrixView);

		final MatrixEntity matrixEntity = new MatrixEntity();
		beanMapper.mapMatrixViewToDataModel(matrixEntity, matrixView);

		em.persist(matrixEntity);

		return matrixEntity.getId();
	}

	@Override
	public Optional<MatrixView> find(final Object matrixId) {
		final MatrixEntity matrixEntity = em.find(MatrixEntity.class, matrixId);

		if (matrixEntity == null) {
			return Optional.empty();
		}

		final MatrixView dto = mergeFromEntityToDto(matrixEntity);
		return Optional.of(dto);
	}

	private MatrixView mergeFromEntityToDto(final MatrixEntity matrixEntity) {
		final List<List<OverlayView>> scene = new ArrayList<>();
		final List<SceneEntity> scenes = matrixEntity.getScenes();
		for (final SceneEntity sceneOverlayedEntity : scenes) {
			final List<OverlayView> collect2 = sceneOverlayedEntity.getOverlays().stream().map(currentSceneEntity -> {
				if (currentSceneEntity instanceof OverlayStationaryEntity) {
					final OverlayStationaryEntity overlayStationaryEntity = (OverlayStationaryEntity) currentSceneEntity;

					final List<List<LedStateView>> statesView = new ArrayList<>();
					final List<LedStateRowEntity> states = overlayStationaryEntity.getStates();
					for (final LedStateRowEntity statesRow : states) {
						final List<LedStateView> collect = statesRow.getLedStates().stream().map(ledStateEntity -> {
							return LedStateView.valueOf(ledStateEntity.name());
						}).collect(Collectors.toList());
						statesView.add(collect);
					}

					final RgbColor offColor = overlayStationaryEntity.getOffColor();
					final me.hmasrafchi.leddisplay.administration.model.view.RgbColorView offColorView = new me.hmasrafchi.leddisplay.administration.model.view.RgbColorView(
							offColor.getR(), offColor.getG(), offColor.getB());
					final RgbColor onColor = overlayStationaryEntity.getOnColor();
					final me.hmasrafchi.leddisplay.administration.model.view.RgbColorView onColorView = new me.hmasrafchi.leddisplay.administration.model.view.RgbColorView(
							onColor.getR(), onColor.getG(), onColor.getB());

					int duration = overlayStationaryEntity.getDuration();

					final OverlayStationaryView overlayStationaryView = new OverlayStationaryView(statesView,
							onColorView, offColorView, duration);

					return overlayStationaryView;
				}

				if (currentSceneEntity instanceof OverlayRollHorizontallyEntity) {
					final OverlayRollHorizontallyEntity overlayStationaryEntity = (OverlayRollHorizontallyEntity) currentSceneEntity;

					final List<List<LedStateView>> statesView = new ArrayList<>();
					final List<LedStateRowEntity> states = overlayStationaryEntity.getStates();
					for (final LedStateRowEntity statesRow : states) {
						final List<LedStateView> collect = statesRow.getLedStates().stream().map(ledStateEntity -> {
							return LedStateView.valueOf(ledStateEntity.name());
						}).collect(Collectors.toList());
						statesView.add(collect);
					}

					final RgbColor offColor = overlayStationaryEntity.getOffColor();
					final me.hmasrafchi.leddisplay.administration.model.view.RgbColorView offColorView = new me.hmasrafchi.leddisplay.administration.model.view.RgbColorView(
							offColor.getR(), offColor.getG(), offColor.getB());
					final RgbColor onColor = overlayStationaryEntity.getOnColor();
					final me.hmasrafchi.leddisplay.administration.model.view.RgbColorView onColorView = new me.hmasrafchi.leddisplay.administration.model.view.RgbColorView(
							onColor.getR(), onColor.getG(), onColor.getB());

					int beginIndexMark = overlayStationaryEntity.getBeginIndexMark();
					int yposition = overlayStationaryEntity.getYPosition();

					final OverlayRollHorizontallyView overlayStationaryView = new OverlayRollHorizontallyView(
							statesView, onColorView, offColorView, beginIndexMark, yposition);

					return overlayStationaryView;
				}
				return null;
			}).collect(Collectors.toList());

			scene.add(collect2);
		}

		final Integer id = matrixEntity.getId();
		final int rowCount = matrixEntity.getRowCount();
		final int columnCount = matrixEntity.getColumnCount();
		final MatrixView dto = new MatrixView(id, rowCount, columnCount, scene);
		return dto;
	}

	@Override
	public void update(final MatrixView matrix) {
		final MatrixEntity matrixEntity = em.find(MatrixEntity.class, matrix.getId());
		beanMapper.mapMatrixViewToDataModel(matrixEntity, matrix);
		em.merge(matrixEntity);
	}
}