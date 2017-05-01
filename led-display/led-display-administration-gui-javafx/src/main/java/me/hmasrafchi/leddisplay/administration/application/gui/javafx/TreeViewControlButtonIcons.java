/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx;

import lombok.Getter;

/**
 * @author michelin
 *
 */
enum TreeViewControlButtonIcons {
	PLUS_SIGN("/plus-sign.gif");

	@Getter
	private final String iconPath;

	private TreeViewControlButtonIcons(final String iconPath) {
		this.iconPath = iconPath;
	}
}