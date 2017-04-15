/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.model.view;

import java.util.Optional;

/**
 * @author michelin
 *
 */
public interface MatrixRepository {
	Object create(CreateMatrixCommand createMatrixCommand);

	Optional<Matrix> find(Object matrixId);

	void update(Matrix matrix);
}