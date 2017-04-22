/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.infrastructure;

import java.util.Optional;

import me.hmasrafchi.leddisplay.administration.model.view.MatrixView;

/**
 * @author michelin
 *
 */
public interface MatrixRepository {
	MatrixView create(MatrixView matrixView);

	Optional<MatrixView> find(Object matrixId);

	MatrixView update(MatrixView matrix);
}