/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import me.hmasrafchi.leddisplay.administration.model.view.MatrixView;

/**
 * @author michelin
 *
 */
public final class PrimaryGui extends StackPane {
	private BorderPane borderPane;
	private MatricesTreeView matricesTreeView;

	public PrimaryGui() {
		this.borderPane = getBorderPaneGui();
		getChildren().add(borderPane);
	}

	private BorderPane getBorderPaneGui() {
		final BorderPane borderPane = new BorderPane();

		this.matricesTreeView = new MatricesTreeView(getAllMatrices(), borderPane);
		borderPane.setLeft(matricesTreeView);

		return borderPane;
	}

	public List<MatrixView> getAllMatrices() {
		final Response allMatricesResponse = RestClient.getAllMatrices();
		final int responseStatusCode = allMatricesResponse.getStatus();
		if (responseStatusCode == Response.Status.OK.getStatusCode()
				|| responseStatusCode == Response.Status.NOT_FOUND.getStatusCode()) {
			final List<MatrixView> matrices = allMatricesResponse.readEntity(new GenericType<List<MatrixView>>() {
			});

			return matrices != null ? matrices : new ArrayList<>();
		} else {
			throw new RuntimeException("can not get matrices");
		}
	}

	public void refreshGui() {
		matricesTreeView.stopAllAnimations();

		getChildren().clear();
		this.borderPane = getBorderPaneGui();
		getChildren().add(borderPane);
	}

	public void showProgressBar() {
		final ObservableList<Node> children = getChildren();
		if (children.size() == 1) {
			final ProgressIndicator pi = new ProgressIndicator();
			final VBox box = new VBox(pi);
			box.setAlignment(Pos.CENTER);
			borderPane.setDisable(true);
			children.add(box);
		}
	}

	public void hideProgressBar() {
		final ObservableList<Node> children = getChildren();
		if (children.size() == 2) {
			borderPane.setDisable(false);
			getChildren().remove(1);
		}
	}
}