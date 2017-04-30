/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx;

import static jersey.repackaged.com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import javafx.scene.layout.BorderPane;
import me.hmasrafchi.leddisplay.administration.model.view.MatrixView;

/**
 * @author michelin
 *
 */
public final class AdministrationGui2 extends BorderPane {
	private final MatricesTreeView matricesTreeView;

	public AdministrationGui2(final List<MatrixView> matrices) {
		checkNotNull(matrices);

		this.matricesTreeView = new MatricesTreeView(matrices, this);
		setLeft(matricesTreeView);
	}
}