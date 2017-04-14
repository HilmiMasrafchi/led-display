/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.model;

/**
 * @author michelin
 *
 */
public interface MatrixRepository {
	void create(Matrix matrix);

	Matrix find(Object matrixId);
}