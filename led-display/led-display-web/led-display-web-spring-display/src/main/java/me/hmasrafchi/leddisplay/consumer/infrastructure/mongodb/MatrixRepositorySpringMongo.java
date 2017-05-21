/**
 * 
 */
package me.hmasrafchi.leddisplay.consumer.infrastructure.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import me.hmasrafchi.leddisplay.consumer.infrastructure.MatrixRepository;
import me.hmasrafchi.leddisplay.model.event.MatrixUpdatedEvent;

/**
 * @author michelin
 *
 */
@Repository
public interface MatrixRepositorySpringMongo extends MongoRepository<MatrixUpdatedEvent, Integer>, MatrixRepository {
	MatrixUpdatedEvent findByMatrixId(Integer matrixId);
}
