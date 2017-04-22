/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.infrastructure;

import java.util.Optional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import me.hmasrafchi.leddisplay.administration.application.BeanMapper;
import me.hmasrafchi.leddisplay.administration.model.jpa.MatrixEntity;
import me.hmasrafchi.leddisplay.administration.model.view.MatrixView;
import me.hmasrafchi.leddisplay.domain.CompiledFrames;
import me.hmasrafchi.leddisplay.domain.Matrix;

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
	public MatrixView create(final MatrixView matrixView) {
		final Matrix matrix = beanMapper.mapMatrixFromViewToDomainModel(matrixView);
		final Optional<CompiledFrames> compiledFrames = matrix.getCompiledFrames();

		final MatrixEntity matrixEntity = beanMapper.mapMatrixFromViewToDataModel(matrixView, compiledFrames);
		em.persist(matrixEntity);

		return beanMapper.mapMatrixFromDataToViewModel(matrixEntity);
	}

	@Override
	public Optional<MatrixView> find(final Object matrixId) {
		final MatrixEntity matrixEntity = em.find(MatrixEntity.class, matrixId);

		if (matrixEntity == null) {
			return Optional.empty();
		}

		final MatrixView matrixView = beanMapper.mapMatrixFromDataToViewModel(matrixEntity);
		return Optional.of(matrixView);
	}

	@Override
	public MatrixView update(final MatrixView matrixView) {
		final Matrix matrix = beanMapper.mapMatrixFromViewToDomainModel(matrixView);
		final Optional<CompiledFrames> compiledFrames = matrix.getCompiledFrames();

		final MatrixEntity matrixEntityMerged = beanMapper.mapMatrixFromViewToDataModel(matrixView, compiledFrames);
		em.merge(matrixEntityMerged);

		return beanMapper.mapMatrixFromDataToViewModel(matrixEntityMerged);
	}
}