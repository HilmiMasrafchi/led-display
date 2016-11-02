/**
 * 
 */
package me.hmasrafchi.leddisplay.jfx.gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Data;
import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.framework.scene.RandomColorScene;
import me.hmasrafchi.leddisplay.framework.scene.overlay.Overlay;
import me.hmasrafchi.leddisplay.framework.scene.overlay.OverlayedScene;
import me.hmasrafchi.leddisplay.jfx.RuntimeTypeAdapterFactory;

/**
 * @author michelin
 *
 */
public final class Playground extends Application {
	private final static double WINDOW_WIDTH = 1200d;
	private final static double WINDOW_HEIGHT = 600d;

	private final static String DATA_FILE_NAME = "scenes.dat";

	private BorderPane borderPane = new BorderPane();
	private ButtonGrid matrixButtonGrid;
	private Map<me.hmasrafchi.leddisplay.framework.scene.Scene, Model<me.hmasrafchi.leddisplay.framework.scene.Scene>> scenesToGuiMap;

	public static void main(final String[] args) {
		launch(args);
	}

	@Override
	public void start(final Stage primaryStage) {
		final GUIModel persistedGUIConfiguration = loadGuiConfigurationFromFile(DATA_FILE_NAME);

		final Node leftPane = getLeftPane(persistedGUIConfiguration);
		borderPane.setLeft(leftPane);

		final Node bottomPane = getSaveButtonPane(persistedGUIConfiguration);
		borderPane.setBottom(bottomPane);

		final Scene scene = new Scene(borderPane, WINDOW_WIDTH, WINDOW_HEIGHT, true);
		primaryStage.setScene(scene);

		primaryStage.show();
	}

	private Pane getLeftPane(final GUIModel persistedGUIConfiguration) {
		scenesToGuiMap = initScenesPaneMap(persistedGUIConfiguration);
		final ListView<String> matrixListView = new ListView<>(FXCollections.observableArrayList("LED Display"));
		final int matrixColumnsCount = persistedGUIConfiguration.getMatrixColumnsCount();
		final int matrixRowsCount = persistedGUIConfiguration.getMatrixRowsCount();
		matrixButtonGrid = new ButtonGrid(matrixColumnsCount, matrixRowsCount);
		matrixListView.setOnMouseClicked(event -> {
			borderPane.setCenter(matrixButtonGrid);
		});

		final ListView<me.hmasrafchi.leddisplay.framework.scene.Scene> scenesListView = new ListView<>(
				FXCollections.observableArrayList(scenesToGuiMap.keySet()));

		scenesListView.setOnMouseClicked(event -> {
			final me.hmasrafchi.leddisplay.framework.scene.Scene clickedScene = scenesListView.getSelectionModel()
					.getSelectedItem();

			final Node selectedPane = (Node) scenesToGuiMap.get(clickedScene);
			borderPane.setCenter((Node) selectedPane);
		});

		return new VBox(matrixListView, scenesListView);
	}

	private Map<me.hmasrafchi.leddisplay.framework.scene.Scene, Model<me.hmasrafchi.leddisplay.framework.scene.Scene>> initScenesPaneMap(
			final GUIModel persistedGUIConfiguration) {
		final Map<me.hmasrafchi.leddisplay.framework.scene.Scene, Model<me.hmasrafchi.leddisplay.framework.scene.Scene>> map = new HashMap<>();

		final Collection<? extends me.hmasrafchi.leddisplay.framework.scene.Scene> loadedScenes = persistedGUIConfiguration
				.getScenes();
		for (final me.hmasrafchi.leddisplay.framework.scene.Scene currentScene : loadedScenes) {
			final Model<me.hmasrafchi.leddisplay.framework.scene.Scene> pane = determinePaneBasedOnScene(currentScene);
			map.put(currentScene, pane);
		}

		return map;
	}

	private Model<me.hmasrafchi.leddisplay.framework.scene.Scene> determinePaneBasedOnScene(
			final me.hmasrafchi.leddisplay.framework.scene.Scene scene) {
		if (scene instanceof RandomColorScene) {
			final RandomColorScene randomColorScene = (RandomColorScene) scene;
			final List<Led.RgbColor> colors = randomColorScene.getColors();
			return new SceneRandomColorGUI(colors);
		}

		if (scene instanceof OverlayedScene) {
			final OverlayedScene overlayedScene = (OverlayedScene) scene;
			return new SceneOverlayedGUI(overlayedScene);
		}

		throw new RuntimeException("can not determine the gui panel based on loaded scenes");
	}

	private GUIModel loadGuiConfigurationFromFile(final String fileName) {
		final RuntimeTypeAdapterFactory<me.hmasrafchi.leddisplay.framework.scene.Scene> typeAdapterFactory = RuntimeTypeAdapterFactory
				.of(me.hmasrafchi.leddisplay.framework.scene.Scene.class).registerSubtype(OverlayedScene.class)
				.registerSubtype(RandomColorScene.class);
		final Gson gson = new GsonBuilder().registerTypeAdapterFactory(typeAdapterFactory)
				.registerTypeAdapter(Overlay.class, new InterfaceAdapter<Overlay>()).create();

		final Path path = Paths.get(fileName);
		try (final BufferedReader reader = Files.newBufferedReader(path)) {
			return gson.fromJson(reader, GUIModel.class);
		} catch (final IOException e) {
			throw new RuntimeException("can not load data file");
		}
	}

	private Node getSaveButtonPane(final GUIModel persistedGUIConfiguration) {
		final Button button = new Button("Save");
		button.setOnAction(event -> {
			final Gson gson = new GsonBuilder().setPrettyPrinting()
					.registerTypeAdapterFactory(
							RuntimeTypeAdapterFactory.of(me.hmasrafchi.leddisplay.framework.scene.Scene.class)
									.registerSubtype(OverlayedScene.class).registerSubtype(RandomColorScene.class))
					.registerTypeAdapter(Overlay.class, new InterfaceAdapter<Overlay>()).create();
			final String json = gson.toJson(getModel());
			final Path path = Paths.get(DATA_FILE_NAME);
			try (BufferedWriter writer = Files.newBufferedWriter(path)) {
				writer.write(json);
			} catch (IOException e) {
			}
		});

		return button;
	}

	private GUIModel getModel() {
		final GUIModel guiModel = new GUIModel();

		guiModel.setMatrixRowsCount(matrixButtonGrid.getRowsCount());
		guiModel.setMatrixColumnsCount(matrixButtonGrid.getColumnsCount());

		final Collection<me.hmasrafchi.leddisplay.framework.scene.Scene> collect = scenesToGuiMap.values().stream()
				.map(model -> model.getModel()).collect(Collectors.toList());
		guiModel.setScenes(collect);

		return guiModel;
	}

	private final class InterfaceAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {
		public JsonElement serialize(T object, Type interfaceType, JsonSerializationContext context) {
			final JsonObject wrapper = new JsonObject();
			wrapper.addProperty("type", object.getClass().getName());
			wrapper.add("data", context.serialize(object));
			return wrapper;
		}

		public T deserialize(JsonElement elem, Type interfaceType, JsonDeserializationContext context)
				throws JsonParseException {
			final JsonObject wrapper = (JsonObject) elem;
			final JsonElement typeName = get(wrapper, "type");
			final JsonElement data = get(wrapper, "data");
			final Type actualType = typeForName(typeName);
			return context.deserialize(data, actualType);
		}

		private Type typeForName(final JsonElement typeElem) {
			try {
				return Class.forName(typeElem.getAsString());
			} catch (ClassNotFoundException e) {
				throw new JsonParseException(e);
			}
		}

		private JsonElement get(final JsonObject wrapper, String memberName) {
			final JsonElement elem = wrapper.get(memberName);
			if (elem == null)
				throw new JsonParseException(
						"no '" + memberName + "' member found in what was expected to be an interface wrapper");
			return elem;
		}
	}
}

@Data
final class GUIModel {
	private int matrixColumnsCount;
	private int matrixRowsCount;
	private Collection<? extends me.hmasrafchi.leddisplay.framework.scene.Scene> scenes = new ArrayList<>();
}

// private Pane getButtonPane() {
// final Button button = new Button("Click me");
// button.setOnAction(actionEvent -> {
// final List<List<Overlay.State>> statesOverlay1 = Arrays.asList(
// Arrays.asList(Overlay.State.ON, Overlay.State.ON, Overlay.State.ON,
// Overlay.State.ON,
// Overlay.State.ON),
// Arrays.asList(Overlay.State.OFF, Overlay.State.TRANSPARENT,
// Overlay.State.TRANSPARENT,
// Overlay.State.TRANSPARENT, Overlay.State.OFF),
// Arrays.asList(Overlay.State.ON, Overlay.State.TRANSPARENT,
// Overlay.State.TRANSPARENT,
// Overlay.State.TRANSPARENT, Overlay.State.ON),
// Arrays.asList(Overlay.State.OFF, Overlay.State.TRANSPARENT,
// Overlay.State.TRANSPARENT,
// Overlay.State.TRANSPARENT, Overlay.State.OFF),
// Arrays.asList(Overlay.State.ON, Overlay.State.TRANSPARENT,
// Overlay.State.TRANSPARENT,
// Overlay.State.TRANSPARENT, Overlay.State.ON),
// Arrays.asList(Overlay.State.ON, Overlay.State.ON, Overlay.State.ON,
// Overlay.State.ON,
// Overlay.State.ON));
// final Overlay overlay1 = new OverlayStationary(statesOverlay1,
// Led.RgbColor.RED, Led.RgbColor.ORANGE);
//
// final List<List<Overlay.State>> statesOverlay2 = Arrays.asList(
// Arrays.asList(Overlay.State.ON, Overlay.State.ON, Overlay.State.ON,
// Overlay.State.ON,
// Overlay.State.ON, Overlay.State.ON, Overlay.State.ON),
// Arrays.asList(Overlay.State.ON, Overlay.State.TRANSPARENT, Overlay.State.ON,
// Overlay.State.TRANSPARENT, Overlay.State.ON, Overlay.State.TRANSPARENT,
// Overlay.State.ON),
// Arrays.asList(Overlay.State.ON, Overlay.State.ON, Overlay.State.ON,
// Overlay.State.ON,
// Overlay.State.ON, Overlay.State.ON, Overlay.State.ON));
// final Overlay overlay2 = new OverlayRollHorizontal(statesOverlay2,
// Led.RgbColor.GREEN, 1, 10);
//
// final me.hmasrafchi.leddisplay.framework.scene.Scene firstScene = new
// OverlayedScene(
// Arrays.asList(overlay2, overlay1));
//
// final List<Led.RgbColor> rainbowColors = Arrays.asList(Led.RgbColor.INDIGO,
// Led.RgbColor.BLUE,
// Led.RgbColor.GREEN, Led.RgbColor.YELLOW, Led.RgbColor.ORANGE,
// Led.RgbColor.RED);
// final me.hmasrafchi.leddisplay.framework.scene.Scene secondScene = new
// RandomColorScene(rainbowColors);
//
// final List<me.hmasrafchi.leddisplay.framework.scene.Scene> scenes =
// Arrays.asList(firstScene, secondScene);
//
// Gson gson = new GsonBuilder().setPrettyPrinting()
// .registerTypeAdapterFactory(
// RuntimeTypeAdapterFactory.of(me.hmasrafchi.leddisplay.framework.scene.Scene.class)
// .registerSubtype(OverlayedScene.class).registerSubtype(RandomColorScene.class))
// .registerTypeAdapter(Overlay.class, new
// InterfaceAdapter<Overlay>()).create();
// final String json = gson.toJson(scenes);
//
// final Path path = Paths.get(DATA_FILE_NAME);
// try (BufferedWriter writer = Files.newBufferedWriter(path)) {
// writer.write(json);
// } catch (IOException e) {
// e.printStackTrace();
// }
// });
// return new TilePane(button);
// }
