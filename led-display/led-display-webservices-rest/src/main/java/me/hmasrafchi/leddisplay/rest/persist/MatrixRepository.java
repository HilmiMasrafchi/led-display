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

	Scene addScene(MatrixEntity matrixEntity);

	// TODO: this should be hidden
	void update(MatrixEntity matrixEntity);

	Overlay appendOverlay(Scene scene, Overlay overlay);

	Overlay updateOverlay(Scene scene, Overlay overlay, Overlay newOverlay);

	void delete(int matrixId);
}