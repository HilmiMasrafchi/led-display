/**
 * 
 */
package me.hmasrafchi.leddisplay.rest.persist.inmem;

import java.util.List;
import java.util.Optional;

import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import me.hmasrafchi.leddisplay.rest.persist.MatrixEntity;
import me.hmasrafchi.leddisplay.rest.persist.MatrixRepository;
import me.hmasrafchi.leddisplay.rest.persist.Overlay;
import me.hmasrafchi.leddisplay.rest.persist.Scene;

/**
 * @author michelin
 *
 */
@Default
public class MatrixRepositoryJpa implements MatrixRepository {
	@PersistenceContext
	private EntityManager em;

	@Override
	public void create(final MatrixEntity matrix) {
		em.persist(matrix);
	}

	@Override
	public MatrixEntity read(final Object matrixId) {
		final MatrixEntity find = em.find(MatrixEntity.class, matrixId);
		if (find == null) {
			throw new MatrixDoesntExistsException();
		}
		return find;
	}

	@Override
	public void update(final MatrixEntity matrix) {
		em.merge(matrix);
	}

	@Override
	public void delete(final Object matrixId) {
		final MatrixEntity matrix = read(matrixId);
		em.remove(matrix);
	}

	@Override
	public Scene createNewScene(final Object matrixId) {
		final MatrixEntity matrix = read(matrixId);
		final Scene scene = new Scene();
		em.persist(scene);
		matrix.getScenes().add(scene);
		update(matrix);
		return scene;
	}

	@Override
	public Overlay appendOverlay(final Object matrixId, final Object sceneId, final Overlay overlay) {
		final MatrixEntity matrix = read(matrixId);
		return matrix.getScenes().stream().filter(scene -> scene.getId().equals(sceneId)).findFirst().map(scene -> {
			em.persist(overlay);
			scene.getOverlays().add(overlay);
			em.merge(scene);
			return overlay;
		}).orElseThrow(() -> new SceneDoesntExistsException());
	}

	@Override
	public Overlay updateOverlay(final Object matrixId, final Object sceneId, final Object overlayId,
			final Overlay newOverlay) {
		final MatrixEntity matrix = read(matrixId);
		return matrix.getScenes().stream().filter(scene -> scene.getId().equals(sceneId)).findFirst().map(scene -> {
			final List<Overlay> overlays = scene.getOverlays();
			return overlays.stream().filter(overlay -> overlay.getId().equals(overlayId)).findFirst().map(overlay -> {
				final int overlayIndex = overlays.indexOf(overlay);
				overlays.set(overlayIndex, newOverlay);
				em.merge(scene);
				return newOverlay;
			}).orElseThrow(() -> new OverlayDoesntExistsException());
		}).orElseThrow(() -> new SceneDoesntExistsException());
	}

	@Override
	public void deleteScene(final Object matrixId, final Object sceneId) {
		final MatrixEntity matrix = read(matrixId);
		final Optional<Scene> findFirst = matrix.getScenes().stream().filter(scene -> scene.getId().equals(sceneId))
				.findFirst();
		if (findFirst.isPresent()) {
			matrix.getScenes().remove(findFirst.get());
			update(matrix);
		} else {
			throw new SceneDoesntExistsException();
		}
	}

	@Override
	public void deleteOverlay(final Object matrixId, final Object sceneId, final Object overlayId) {
		final MatrixEntity matrix = read(matrixId);
		final Optional<Scene> findFirst = matrix.getScenes().stream().filter(scene -> scene.getId().equals(sceneId))
				.findFirst();
		if (findFirst.isPresent()) {
			final Optional<Overlay> findFirst2 = findFirst.get().getOverlays().stream()
					.filter(overlay -> overlay.getId().equals(overlayId)).findFirst();
			if (findFirst2.isPresent()) {
				findFirst.get().getOverlays().remove(findFirst2.get());
				update(matrix);
			} else {
				throw new OverlayDoesntExistsException();
			}
		} else {
			throw new SceneDoesntExistsException();
		}
	}
}