/**
 * 
 */
package me.hmasrafchi.leddisplay.consumer.infrastructure.mongodb;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import me.hmasrafchi.leddisplay.consumer.infrastructure.MatrixRepository;
import me.hmasrafchi.leddisplay.model.event.MatrixUpdatedEvent;

/**
 * @author michelin
 *
 */
@Primary
@Repository
public class MatrixRepositoryMongo implements MatrixRepository {
	@Autowired
	private MatrixRepositorySpringMongo matrixRepositorySpring;

	@Override
	public MatrixUpdatedEvent save(final MatrixUpdatedEvent matrixEvent) {
		return matrixRepositorySpring.save(matrixEvent);
	}

	@Override
	public MatrixUpdatedEvent findById(final BigInteger matrixId) {
		return matrixRepositorySpring.findById(matrixId);
	}
}