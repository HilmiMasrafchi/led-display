/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application.gui.javafx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.Value;
import me.hmasrafchi.leddisplay.administration.model.view.MatrixView;

/**
 * @author michelin
 *
 */
public final class LedDisplayAdministrationGuiJavaFx extends Application {
	private final BorderPane borderPane = new BorderPane();

	private Map<MatrixView, MatrixGui> listItemToMatrixGuiMapping = new HashMap<>();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(final Stage primaryStage) throws Exception {
		final Client jaxRsClient = ClientBuilder.newClient();
		final Response getAllMatricesResponse = jaxRsClient.target("http://localhost:8080/led-display-administration")
				.path("matrices").request(MediaType.APPLICATION_JSON).get();
		if (getAllMatricesResponse.getStatus() == Response.Status.OK.getStatusCode()) {
			final List<MatrixView> allMatrices = getAllMatricesResponse.readEntity(new GenericType<List<MatrixView>>() {
			});

			allMatrices.forEach(matrix -> listItemToMatrixGuiMapping.put(matrix, new MatrixGui(matrix)));
			setBorderPaneLeft(allMatrices);
		}

		primaryStage.setTitle("Hello World!");

		primaryStage.setScene(new Scene(borderPane, 1200, 800));
		primaryStage.show();
	}

	private void setBorderPaneLeft(final List<MatrixView> allMatrices) {
		final ObservableList<MatrixView> items = FXCollections.observableArrayList(allMatrices);
		final ListView<MatrixView> listView = new ListView<>(items);
		listView.setCellFactory(param -> new ListCell<MatrixView>() {
			@Override
			protected void updateItem(MatrixView item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null || item.getName() == null) {
					setText(null);
				} else {
					setText(item.getName());
				}
			}
		});

		listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MatrixView>() {
			@Override
			public void changed(final ObservableValue<? extends MatrixView> observable, final MatrixView oldValue,
					final MatrixView newValue) {
				final ScrollPane sp = new ScrollPane();
				final MatrixGui matrixGui = listItemToMatrixGuiMapping.get(newValue);
				sp.setContent(matrixGui);
				borderPane.setCenter(sp);
			}
		});

		borderPane.setLeft(listView);
	}

	@Value
	private class ListItem {
		private final int matrixId;
		private final String matrixName;
	}
}