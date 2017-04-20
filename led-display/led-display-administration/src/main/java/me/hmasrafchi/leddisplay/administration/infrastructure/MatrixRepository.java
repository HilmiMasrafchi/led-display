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
	Object create(MatrixView matrixView);

	Optional<MatrixView> find(Object matrixId);

	void update(MatrixView matrix);
}