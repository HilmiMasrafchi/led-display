/**
 * 
 */
package me.hmasrafchi.leddisplay.rest.persist.inmem;

import static java.util.Optional.ofNullable;

import java.util.Optional;

import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import me.hmasrafchi.leddisplay.rest.persist.MatrixEntity;
import me.hmasrafchi.leddisplay.rest.persist.MatrixRepository;

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
}