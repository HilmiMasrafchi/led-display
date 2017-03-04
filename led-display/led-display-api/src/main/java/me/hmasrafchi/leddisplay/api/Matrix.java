/**
 * 
 */
package me.hmasrafchi.leddisplay.api;

/**
 * @author michelin
 *
 */
public interface Matrix {
	CompiledFrames compile(Scene scene, int rowCount, int columnCount);
}