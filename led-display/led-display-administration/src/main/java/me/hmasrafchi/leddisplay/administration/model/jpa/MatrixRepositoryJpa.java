/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.model.jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import me.hmasrafchi.leddisplay.administration.model.view.CreateMatrixCommand;
import me.hmasrafchi.leddisplay.administration.model.view.LedState;
import me.hmasrafchi.leddisplay.administration.model.view.Matrix;
import me.hmasrafchi.leddisplay.administration.model.view.MatrixRepository;
import me.hmasrafchi.leddisplay.administration.model.view.Overlay;
import me.hmasrafchi.leddisplay.administration.model.view.OverlayRollHorizontally;
import me.hmasrafchi.leddisplay.administration.model.view.OverlayStationary;

/**
 * @author michelin
 *
 */
public class MatrixRepositoryJpa implements MatrixRepository {
	@PersistenceContext
	private EntityManager em;

	@Override
	public Object create(final CreateMatrixCommand createMatrixCommand) {
		final MatrixEntity matrixEntity = new MatrixEntity();
		matrixEntity.setRowCount(createMatrixCommand.getRowCount());
		matrixEntity.setColumnCount(createMatrixCommand.getColumnCount());
		mapScenes(matrixEntity, createMatrixCommand.getScenes());
		em.persist(matrixEntity);

		return matrixEntity.getId();
	}

	private MatrixEntity mergeFromDtoToEntity(final Matrix dto, final MatrixEntity entity) {
		if (dto.getId() != null) {
			entity.setId(dto.getId());
		}

		entity.setRowCount(dto.getRowCount());
		entity.setColumnCount(dto.getColumnCount());

		final List<List<Overlay>> scenesDto = dto.getScenes();
		if (scenesDto != null) {
			final List<SceneEntity> scenes = mapScenes(entity, scenesDto);
			entity.setScenes(scenes);
		}
		return entity;
	}

	private List<SceneEntity> mapScenes(MatrixEntity matrixEntity, final List<List<Overlay>> scenesDto) {
		if (scenesDto == null || scenesDto.isEmpty()) {
			return null;
		}

		final List<SceneEntity> sceneEntities = scenesDto.stream().map(overlayDtos -> {
			final SceneEntity sceneEntity = new SceneEntity();
			sceneEntity.setMatrix(matrixEntity);

			final List<OverlayEntity> overlayEntities = new ArrayList<>();
			for (final Overlay overlayDto : overlayDtos) {
				if (overlayDto instanceof OverlayStationary) {
					final OverlayStationary overlayDtoCasted = (OverlayStationary) overlayDto;

					final List<List<LedState>> stateDtos = overlayDtoCasted.getStates();
					final List<LedStateRowEntity> statesPersist = new ArrayList<>();
					for (List<LedState> ledStateRowView : stateDtos) {
						final List<LedStateEntity> ledStates = ledStateRowView.stream().map(ledStateView -> {
							return LedStateEntity.valueOf(ledStateView.name());
						}).collect(Collectors.toList());
						final LedStateRowEntity ledStateRowPersist = new LedStateRowEntity();
						ledStateRowPersist.setLedStates(ledStates);
						statesPersist.add(ledStateRowPersist);
					}

					final me.hmasrafchi.leddisplay.administration.model.view.RgbColor onColorView = overlayDtoCasted
							.getOnColor();
					final me.hmasrafchi.leddisplay.administration.model.jpa.RgbColor onColor = new me.hmasrafchi.leddisplay.administration.model.jpa.RgbColor(
							onColorView.getR(), onColorView.getG(), onColorView.getB());
					final me.hmasrafchi.leddisplay.administration.model.view.RgbColor offColorView = overlayDtoCasted
							.getOffColor();
					final me.hmasrafchi.leddisplay.administration.model.jpa.RgbColor offColor = new me.hmasrafchi.leddisplay.administration.model.jpa.RgbColor(
							offColorView.getR(), offColorView.getG(), offColorView.getB());
					final int duration = overlayDtoCasted.getDuration();
					final OverlayStationaryEntity overlayEntity = new OverlayStationaryEntity(statesPersist, onColor,
							offColor, duration);
					overlayEntity.setScene(sceneEntity);
					overlayEntities.add(overlayEntity);
				}

				if (overlayDto instanceof OverlayRollHorizontally) {
					final OverlayRollHorizontally overlayDtoCasted = (OverlayRollHorizontally) overlayDto;
					final List<List<LedState>> stateDtos = overlayDtoCasted.getStates();
					final List<LedStateRowEntity> statesPersist = new ArrayList<>();
					for (List<LedState> ledStateRowView : stateDtos) {
						final List<LedStateEntity> ledStates = ledStateRowView.stream().map(ledStateView -> {
							return LedStateEntity.valueOf(ledStateView.name());
						}).collect(Collectors.toList());
						final LedStateRowEntity ledStateRowPersist = new LedStateRowEntity();
						ledStateRowPersist.setLedStates(ledStates);
						statesPersist.add(ledStateRowPersist);
					}

					final me.hmasrafchi.leddisplay.administration.model.view.RgbColor onColorView = overlayDtoCasted
							.getOnColor();
					final me.hmasrafchi.leddisplay.administration.model.jpa.RgbColor onColor = new me.hmasrafchi.leddisplay.administration.model.jpa.RgbColor(
							onColorView.getR(), onColorView.getG(), onColorView.getB());
					final me.hmasrafchi.leddisplay.administration.model.view.RgbColor offColorView = overlayDtoCasted
							.getOffColor();
					final me.hmasrafchi.leddisplay.administration.model.jpa.RgbColor offColor = new me.hmasrafchi.leddisplay.administration.model.jpa.RgbColor(
							offColorView.getR(), offColorView.getG(), offColorView.getB());
					final int beginIndexMark = overlayDtoCasted.getBeginIndexMark();
					final int yposition = overlayDtoCasted.getYposition();
					final OverlayRollHorizontallyEntity overlayEntity = new OverlayRollHorizontallyEntity(statesPersist,
							onColor, offColor, beginIndexMark, yposition);
					overlayEntity.setScene(sceneEntity);
					overlayEntities.add(overlayEntity);
				}
			}

			sceneEntity.setOverlays(overlayEntities);
			return sceneEntity;
		}).collect(Collectors.toList());

		matrixEntity.setScenes(sceneEntities);
		return sceneEntities;
	}

	@Override
	public Optional<Matrix> find(final Object matrixId) {
		final MatrixEntity matrixEntity = em.find(MatrixEntity.class, matrixId);

		if (matrixEntity == null) {
			return Optional.empty();
		}

		final Matrix dto = mergeFromEntityToDto(matrixEntity);
		return Optional.of(dto);
	}

	private Matrix mergeFromEntityToDto(final MatrixEntity matrixEntity) {
		final List<List<Overlay>> scene = new ArrayList<>();
		final List<SceneEntity> scenes = matrixEntity.getScenes();
		for (final SceneEntity sceneOverlayedEntity : scenes) {
			final List<Overlay> collect2 = sceneOverlayedEntity.getOverlays().stream().map(currentSceneEntity -> {
				if (currentSceneEntity instanceof OverlayStationaryEntity) {
					final OverlayStationaryEntity overlayStationaryEntity = (OverlayStationaryEntity) currentSceneEntity;

					final List<List<LedState>> statesView = new ArrayList<>();
					final List<LedStateRowEntity> states = overlayStationaryEntity.getStates();
					for (final LedStateRowEntity statesRow : states) {
						final List<LedState> collect = statesRow.getLedStates().stream().map(ledStateEntity -> {
							return LedState.valueOf(ledStateEntity.name());
						}).collect(Collectors.toList());
						statesView.add(collect);
					}

					final RgbColor offColor = overlayStationaryEntity.getOffColor();
					final me.hmasrafchi.leddisplay.administration.model.view.RgbColor offColorView = new me.hmasrafchi.leddisplay.administration.model.view.RgbColor(
							offColor.getR(), offColor.getG(), offColor.getB());
					final RgbColor onColor = overlayStationaryEntity.getOnColor();
					final me.hmasrafchi.leddisplay.administration.model.view.RgbColor onColorView = new me.hmasrafchi.leddisplay.administration.model.view.RgbColor(
							onColor.getR(), onColor.getG(), onColor.getB());

					int duration = overlayStationaryEntity.getDuration();

					final OverlayStationary overlayStationaryView = new OverlayStationary(statesView, onColorView,
							offColorView, duration);

					return overlayStationaryView;
				}

				if (currentSceneEntity instanceof OverlayRollHorizontallyEntity) {
					final OverlayRollHorizontallyEntity overlayStationaryEntity = (OverlayRollHorizontallyEntity) currentSceneEntity;

					final List<List<LedState>> statesView = new ArrayList<>();
					final List<LedStateRowEntity> states = overlayStationaryEntity.getStates();
					for (final LedStateRowEntity statesRow : states) {
						final List<LedState> collect = statesRow.getLedStates().stream().map(ledStateEntity -> {
							return LedState.valueOf(ledStateEntity.name());
						}).collect(Collectors.toList());
						statesView.add(collect);
					}

					final RgbColor offColor = overlayStationaryEntity.getOffColor();
					final me.hmasrafchi.leddisplay.administration.model.view.RgbColor offColorView = new me.hmasrafchi.leddisplay.administration.model.view.RgbColor(
							offColor.getR(), offColor.getG(), offColor.getB());
					final RgbColor onColor = overlayStationaryEntity.getOnColor();
					final me.hmasrafchi.leddisplay.administration.model.view.RgbColor onColorView = new me.hmasrafchi.leddisplay.administration.model.view.RgbColor(
							onColor.getR(), onColor.getG(), onColor.getB());

					int beginIndexMark = overlayStationaryEntity.getBeginIndexMark();
					int yposition = overlayStationaryEntity.getYPosition();

					final OverlayRollHorizontally overlayStationaryView = new OverlayRollHorizontally(statesView,
							onColorView, offColorView, beginIndexMark, yposition);

					return overlayStationaryView;
				}
				return null;
			}).collect(Collectors.toList());

			scene.add(collect2);
		}

		final Integer id = matrixEntity.getId();
		final int rowCount = matrixEntity.getRowCount();
		final int columnCount = matrixEntity.getColumnCount();
		final Matrix dto = new Matrix(id, rowCount, columnCount, scene);
		return dto;
	}

	@Override
	public void update(final Matrix matrix) {
		final MatrixEntity matrixEntity = em.find(MatrixEntity.class, matrix.getId());
		MatrixEntity mergeFromDtoToEntity = mergeFromDtoToEntity(matrix, matrixEntity);
		em.merge(mergeFromDtoToEntity);
	}
}