/**
 * 
 */
package me.hmasrafchi.leddisplay.consumer.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.hmasrafchi.leddisplay.consumer.data.jpa.MatrixJpa;

/**
 * @author michelin
 *
 */
@Repository
public interface MatrixRepository extends JpaRepository<MatrixJpa, Integer> {

}