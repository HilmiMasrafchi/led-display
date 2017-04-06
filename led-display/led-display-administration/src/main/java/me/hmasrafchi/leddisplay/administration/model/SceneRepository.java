/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.model;

import java.util.Optional;

/**
 * @author michelin
 *
 */
public interface SceneRepository {
	void add(Scene scene);

	Optional<Scene> find(Object sceneId);
}