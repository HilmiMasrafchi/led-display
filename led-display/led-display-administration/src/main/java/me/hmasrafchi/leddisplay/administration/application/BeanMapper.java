/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application;

import me.hmasrafchi.leddisplay.administration.model.domain.Matrix;
import me.hmasrafchi.leddisplay.administration.model.view.MatrixView;

/**
 * @author michelin
 *
 */
public interface BeanMapper<T> {
	Matrix mapMatrixFromViewToDomainModel(MatrixView matrixView);

	T mapMatrixFromViewToDataModel(MatrixView matrixView);
}