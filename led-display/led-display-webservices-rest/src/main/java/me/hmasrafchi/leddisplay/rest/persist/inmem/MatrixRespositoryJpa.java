/**
 * 
 */
package me.hmasrafchi.leddisplay.rest.persist.inmem;

import static java.util.Optional.ofNullable;

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
public class MatrixRespositoryJpa implements MatrixRepository {
	@PersistenceContext
	private EntityManager em;

	@Override
	public void create(final MatrixEntity matrix) {
		em.persist(matrix);
	}

	@Override
	public Optional<MatrixEntity> get(final int id) {
		return ofNullable(em.find(MatrixEntity.class, id));
	}

	@Override
	public void update(final MatrixEntity matrixEntity) {
		em.merge(matrixEntity);
	}

	@Override
	public void delete(int matrixId) {
		get(matrixId).ifPresent(matrixEntity -> em.remove(matrixEntity));
	}

	@Override
	public Scene addScene(final MatrixEntity matrixEntity) {
		final Scene scene = new Scene();
		em.persist(scene);
		matrixEntity.getScenes().add(scene);
		update(matrixEntity);

		return scene;
	}

	@Override
	public Overlay appendOverlay(final Scene scene, final Overlay overlay) {
		em.persist(overlay);
		scene.getOverlays().add(overlay);
		em.merge(scene);
		return overlay;
	}

	@Override
	public Overlay updateOverlay(final Scene scene, final Overlay overlay, final Overlay newOverlay) {
		final List<Overlay> overlays = scene.getOverlays();
		final int indexOf = overlays.indexOf(overlay);
		overlays.set(indexOf, newOverlay);
		em.merge(scene);
		return newOverlay;
	}
}