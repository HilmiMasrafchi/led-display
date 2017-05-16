/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.infrastructure;

import java.util.List;
import java.util.Optional;

import me.hmasrafchi.leddisplay.model.view.MatrixView;

/**
 * @author michelin
 *
 */
// TODO: put into the model
public interface MatrixRepository {
	MatrixView create(MatrixView matrixView);

	Optional<MatrixView> find(int matrixId);

	List<MatrixView> findAll();

	MatrixView update(MatrixView matrix);

	boolean delete(int matrixId);
}