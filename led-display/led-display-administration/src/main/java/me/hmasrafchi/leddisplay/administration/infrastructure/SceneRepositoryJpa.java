/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.infrastructure;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import me.hmasrafchi.leddisplay.administration.model.Scene;
import me.hmasrafchi.leddisplay.administration.model.SceneRepository;

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
	}

	@Override
	public Optional<Scene> find(final Object sceneId) {
		return Optional.ofNullable(entityManager.find(Scene.class, sceneId));
	}
}