/**
 * 
 */
package me.hmasrafchi.leddisplay.consumer.infrastructure;

import java.math.BigInteger;

import me.hmasrafchi.leddisplay.model.event.MatrixUpdatedEvent;

/**
 * @author michelin
 *
 */
public interface MatrixRepository {
	MatrixUpdatedEvent save(MatrixUpdatedEvent matrixEvent);

	MatrixUpdatedEvent findById(BigInteger matrixId);
}