/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application;

import java.util.Optional;

import me.hmasrafchi.leddisplay.administration.model.jpa.MatrixEntity;
import me.hmasrafchi.leddisplay.administration.model.view.MatrixView;
import me.hmasrafchi.leddisplay.domain.CompiledFrames;
import me.hmasrafchi.leddisplay.domain.Matrix;

/**
 * @author michelin
 *
 */
public interface BeanMapper<T> {
	Matrix mapMatrixFromViewToDomainModel(MatrixView matrixView);

	T mapMatrixFromViewToDataModel(MatrixView matrixView, Optional<CompiledFrames> compiledFrames);

	MatrixView mapMatrixFromDataToViewModel(MatrixEntity matrixEntity);
}