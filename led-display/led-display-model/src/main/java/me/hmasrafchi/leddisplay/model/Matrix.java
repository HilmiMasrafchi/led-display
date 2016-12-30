/**
 * 
 */
package me.hmasrafchi.leddisplay.model;

/**
 * @author michelin
 *
 */
public interface Matrix {
	CompiledFrames compile(Scene scene, int rowCount, int columnCount);
}