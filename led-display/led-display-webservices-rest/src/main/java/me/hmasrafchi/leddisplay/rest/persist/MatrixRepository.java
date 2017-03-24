/**
 * 
 */
package me.hmasrafchi.leddisplay.rest.persist;

import java.util.Optional;

/**
 * @author michelin
 *
 */
public interface MatrixRepository {
	void create(MatrixEntity matrix);

	// TODO: check if I can get by MatrixEntity (without Id specified)
	Optional<MatrixEntity> get(int id);

	void update(final MatrixEntity matrixEntity);

	Scene appendOverlay(final MatrixEntity matrixEntity, final Overlay overlay);

	void delete(int matrixId);
}