/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.infrastructure;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import me.hmasrafchi.leddisplay.data.jpa.MatrixEntity;
import me.hmasrafchi.leddisplay.model.domain.CompiledFrames;
import me.hmasrafchi.leddisplay.model.domain.Matrix;
import me.hmasrafchi.leddisplay.model.mapping.BeanMapper;
import me.hmasrafchi.leddisplay.model.view.MatrixView;

/**
 * @author michelin
 *
 */
@Repository
public class MatrixRepositoryJpa implements MatrixRepository {
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

	@Override
	public Optional<MatrixView> find(final int matrixId) {
		final MatrixEntity matrixEntity = matrixRepositorySpring.findOne(matrixId);
		if (matrixEntity == null) {
			return Optional.empty();
		}

		final MatrixView matrixView = beanMapper.mapMatrixFromDataToViewModel(matrixEntity);
		return Optional.of(matrixView);
	}

	@Override
	public List<MatrixView> findAll() {
		return matrixRepositorySpring.findAll().stream().map(beanMapper::mapMatrixFromDataToViewModel)
				.collect(toList());
	}

	@Override
	public MatrixView update(final MatrixView matrix) {
		final Matrix matrixDomain = beanMapper.mapMatrixFromViewToDomainModel(matrix);
		final Optional<CompiledFrames> compiledFrames = matrixDomain.getCompiledFrames();

		final MatrixEntity matrixEntityMerged = beanMapper.mapMatrixFromViewToDataModel(matrix, compiledFrames);
		matrixRepositorySpring.save(matrixEntityMerged);

		return beanMapper.mapMatrixFromDataToViewModel(matrixEntityMerged);
	}

	@Override
	public boolean delete(int matrixId) {
		if (matrixRepositorySpring.findOne(matrixId) != null) {
			matrixRepositorySpring.delete(matrixId);
			return true;
		}

		return false;
	}
}