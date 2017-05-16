/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.infrastructure;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import me.hmasrafchi.leddisplay.data.jpa.MatrixEntity;
import me.hmasrafchi.leddisplay.model.domain.CompiledFrames;
import me.hmasrafchi.leddisplay.model.domain.Matrix;
import me.hmasrafchi.leddisplay.model.mapping.BeanMapper;
import me.hmasrafchi.leddisplay.model.view.MatrixView;

/**
 * @author michelin
 *
 */
public final class MatrixRepositoryJpa implements MatrixRepository {
	@Autowired
	private MatrixRepositorySpring matrixRepositorySpring;

	@Autowired
	private BeanMapper<MatrixEntity> beanMapper;

	@Override
	public MatrixView create(final MatrixView matrixView) {
		final Matrix matrix = beanMapper.mapMatrixFromViewToDomainModel(matrixView);
		final Optional<CompiledFrames> compiledFrames = matrix.getCompiledFrames();

		final MatrixEntity matrixEntity = beanMapper.mapMatrixFromViewToDataModel(matrixView, compiledFrames);
		matrixRepositorySpring.save(matrixEntity);

		return beanMapper.mapMatrixFromDataToViewModel(matrixEntity);
	}
}