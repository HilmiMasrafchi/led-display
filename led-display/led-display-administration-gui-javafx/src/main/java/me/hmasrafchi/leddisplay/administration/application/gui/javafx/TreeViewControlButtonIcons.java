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
	PLUS_SIGN("/plus-sign.gif"), PLUS_SIGN2("/plus-sign.png");

	@Getter
	private final String iconPath;

	private TreeViewControlButtonIcons(final String iconPath) {
		this.iconPath = iconPath;
	}
}