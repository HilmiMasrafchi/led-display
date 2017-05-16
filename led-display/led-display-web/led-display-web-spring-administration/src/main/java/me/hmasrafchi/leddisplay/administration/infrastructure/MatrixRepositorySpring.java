/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.hmasrafchi.leddisplay.data.jpa.MatrixEntity;

/**
 * @author michelin
 *
 */
@Repository
public interface MatrixRepositorySpring extends JpaRepository<MatrixEntity, Integer> {

}