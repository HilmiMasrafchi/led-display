/**
 * 
 */
package me.hmasrafchi.leddisplay.consumer.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.hmasrafchi.leddisplay.consumer.data.jpa.MatrixJpa;

/**
 * @author michelin
 *
 */
@Repository
interface MatrixRepositorySpringJpa extends JpaRepository<MatrixJpa, Integer> {

}