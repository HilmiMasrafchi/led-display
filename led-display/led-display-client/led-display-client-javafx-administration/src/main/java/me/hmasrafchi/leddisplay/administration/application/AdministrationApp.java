/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.hmasrafchi.leddisplay.administration.application.gui.javafx.PrimaryGui;

/**
 * @author michelin
 *
 */
public final class AdministrationApp extends Application {
	private static final Logger LOGGER = LoggerFactory.getLogger(AdministrationApp.class);

	private static PrimaryGui gui;

	public static void main(String[] args) {
		LOGGER.debug("launching main gui");
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