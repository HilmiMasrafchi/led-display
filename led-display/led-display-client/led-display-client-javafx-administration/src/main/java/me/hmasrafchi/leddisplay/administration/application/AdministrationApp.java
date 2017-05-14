/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.hmasrafchi.leddisplay.administration.application.gui.javafx.PrimaryGui;

/**
 * @author michelin
 *
 */
public final class AdministrationApp extends Application {
	private static PrimaryGui gui;

	public static void main(String[] args) {
		try {
			launch(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start(final Stage primaryStage) throws Exception {
		gui = new PrimaryGui();

		final Scene scene = new Scene(gui, 1200, 800);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Administration Panel");
		primaryStage.setOnCloseRequest((e) -> RestClient.close());
		primaryStage.show();
	}

	public static void showProgressBar() {
		gui.showProgressBar();
	}

	public static void hideProgressBar() {
		gui.hideProgressBar();
	}

	public static void refreshGui() {
		gui.refreshGui();
	}
}