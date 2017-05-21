/**
 * 
 */
package me.hmasrafchi.leddisplay.consumer.infrastructure;

import me.hmasrafchi.leddisplay.model.event.MatrixUpdatedEvent;

/**
 * @author michelin
 *
 */
public interface MatrixRepository {
	MatrixUpdatedEvent save(MatrixUpdatedEvent matrixEvent);

	MatrixUpdatedEvent findById(Integer matrixId);
}