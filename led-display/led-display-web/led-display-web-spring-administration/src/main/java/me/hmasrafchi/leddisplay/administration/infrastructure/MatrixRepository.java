/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.infrastructure;

import me.hmasrafchi.leddisplay.model.view.MatrixView;

/**
 * @author michelin
 *
 */
// TODO: put into the model
public interface MatrixRepository {
	MatrixView create(MatrixView matrixView);
}