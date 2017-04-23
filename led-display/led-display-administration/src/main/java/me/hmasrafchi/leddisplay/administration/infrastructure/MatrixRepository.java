/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.infrastructure;

import java.util.List;
import java.util.Optional;

import me.hmasrafchi.leddisplay.administration.model.view.MatrixView;

/**
 * @author michelin
 *
 */
public interface MatrixRepository {
	MatrixView create(MatrixView matrixView);

	Optional<MatrixView> find(Object matrixId);

	List<MatrixView> findAll();

	MatrixView update(MatrixView matrix);
}