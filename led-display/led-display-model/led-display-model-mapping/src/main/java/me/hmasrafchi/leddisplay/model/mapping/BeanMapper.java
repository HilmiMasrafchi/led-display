/**
 * 
 */
package me.hmasrafchi.leddisplay.model.mapping;

import java.util.Optional;

import me.hmasrafchi.leddisplay.data.jpa.MatrixEntity;
import me.hmasrafchi.leddisplay.model.domain.CompiledFrames;
import me.hmasrafchi.leddisplay.model.domain.Matrix;
import me.hmasrafchi.leddisplay.model.view.MatrixView;

/**
 * @author michelin
 *
 */
public interface BeanMapper<T> {
	Matrix mapMatrixFromViewToDomainModel(MatrixView matrixView);

	T mapMatrixFromViewToDataModel(MatrixView matrixView, Optional<CompiledFrames> compiledFrames);

	MatrixView mapMatrixFromDataToViewModel(MatrixEntity matrixEntity);
}