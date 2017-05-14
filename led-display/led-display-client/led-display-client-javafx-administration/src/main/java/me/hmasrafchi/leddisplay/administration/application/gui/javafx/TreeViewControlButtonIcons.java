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
	MINUS_SIGN("/close.png"), PLUS_SIGN("/download.png");

	@Getter
	private final String iconPath;

	private TreeViewControlButtonIcons(final String iconPath) {
		this.iconPath = iconPath;
	}
}