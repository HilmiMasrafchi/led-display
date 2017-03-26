/**
 * 
 */
package me.hmasrafchi.leddisplay.rest.persist;

/**
 * @author michelin
 *
 */
public interface MatrixRepository {
	void create(MatrixEntity matrix);

	MatrixEntity read(Object matrixId);

	void delete(Object matrixId);

	Scene createNewScene(Object matrixId);

	void deleteScene(Object matrixId, Object sceneId);

	// TODO: this should be hidden
	void update(MatrixEntity matrixEntity);

	Overlay appendOverlay(Object matrixId, Object sceneId, Overlay overlay);

	Overlay updateOverlay(Object matrixId, Object sceneId, Object overlayId, Overlay newOverlay);

	void deleteOverlay(Object matrixId, Object sceneId, Object overlayId);
}