/**
 * 
 */
package test.infra;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import test.model.Scene;
import test.model.SceneRepository;

/**
 * @author michelin
 *
 */
public class SceneRepositoryJpa implements SceneRepository {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void add(final Scene scene) {
		entityManager.persist(scene);
		entityManager.flush();
	}

	@Override
	public Optional<Scene> find(final Object sceneId) {
		return Optional.ofNullable(entityManager.find(Scene.class, sceneId));
	}
}