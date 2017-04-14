/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.infrastructure;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import me.hmasrafchi.leddisplay.administration.model.Matrix;
import me.hmasrafchi.leddisplay.administration.model.MatrixRepository;

/**
 * @author michelin
 *
 */
public class MatrixRepositoryJpa implements MatrixRepository {
	@PersistenceContext
	private EntityManager em;

	@Override
	public void create(final Matrix matrix) {
		em.persist(matrix);
	}

	@Override
	public Matrix find(final Object matrixId) {
		final Matrix find = em.find(Matrix.class, matrixId);
		if (find == null) {
			throw new MatrixDoesntExistsException();
		}

		return find;
	}
}