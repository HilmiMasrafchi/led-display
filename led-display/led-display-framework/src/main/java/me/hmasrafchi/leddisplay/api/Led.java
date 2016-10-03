/**
 * 
 */
package me.hmasrafchi.leddisplay.api;

/**
 * @author michelin
 *
 */
public interface Led {
	void setCoordinateX(double x);

	double getCoordinateX();

	void setCoordinateY(double y);

	double getCoordinateY();

	double getHeight();

	double getWidth();
}