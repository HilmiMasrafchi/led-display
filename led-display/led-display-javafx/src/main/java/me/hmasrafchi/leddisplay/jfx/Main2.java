/**
 * 
 */
package me.hmasrafchi.leddisplay.jfx;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import me.hmasrafchi.leddisplay.api.Led.RgbColor;
import me.hmasrafchi.leddisplay.framework.scene.RandomColorScene;
import me.hmasrafchi.leddisplay.framework.scene.overlay.Overlay;
import me.hmasrafchi.leddisplay.framework.scene.overlay.OverlayRoll;
import me.hmasrafchi.leddisplay.framework.scene.overlay.OverlayStationary;
import me.hmasrafchi.leddisplay.framework.scene.overlay.OverlayedScene;

/**
 * @author michelin
 *
 */
public final class Main2 extends Application {
	private final static String fileName = "scenes.dat";

	private final static double WINDOW_WIDTH = 1200d;
	private final static double WINDOW_HEIGHT = 600d;

	public static void main(String[] args) {
		launch(args);
	}

	final class InterfaceAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {
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

	@Override
	public void start(final Stage primaryStage) throws Exception {
		final BorderPane borderPane = new BorderPane();

		final Path path = Paths.get(fileName);
		Type listType = new TypeToken<List<me.hmasrafchi.leddisplay.framework.scene.Scene>>() {
		}.getType();

		RuntimeTypeAdapterFactory<me.hmasrafchi.leddisplay.framework.scene.Scene> rta = RuntimeTypeAdapterFactory
				.of(me.hmasrafchi.leddisplay.framework.scene.Scene.class).registerSubtype(OverlayedScene.class)
				.registerSubtype(RandomColorScene.class);
		Gson gson = new GsonBuilder().registerTypeAdapterFactory(rta)
				.registerTypeAdapter(Overlay.class, new InterfaceAdapter<Overlay>()).create();

		try (final BufferedReader reader = Files.newBufferedReader(path)) {
			List<me.hmasrafchi.leddisplay.framework.scene.Scene> yourClassList = gson.fromJson(reader, listType);
			System.out.println(yourClassList);
		}

		borderPane.setLeft(getScenesPane());

		final Scene scene = new Scene(borderPane, WINDOW_WIDTH, WINDOW_HEIGHT, true);
		primaryStage.setScene(scene);

		primaryStage.show();
	}

	private Pane getScenesPane() {
		final Button button = new Button("Click me");
		button.setOnAction(actionEvent -> {
			final List<List<Overlay.State>> statesOverlay1 = Arrays.asList(
					Arrays.asList(Overlay.State.STATIONARY_ON, Overlay.State.STATIONARY_ON, Overlay.State.STATIONARY_ON,
							Overlay.State.STATIONARY_ON, Overlay.State.STATIONARY_ON),
					Arrays.asList(Overlay.State.STATIONARY_OFF, Overlay.State.TRANSPARENT, Overlay.State.TRANSPARENT,
							Overlay.State.TRANSPARENT, Overlay.State.STATIONARY_OFF),
					Arrays.asList(Overlay.State.STATIONARY_ON, Overlay.State.TRANSPARENT, Overlay.State.TRANSPARENT,
							Overlay.State.TRANSPARENT, Overlay.State.STATIONARY_ON),
					Arrays.asList(Overlay.State.STATIONARY_OFF, Overlay.State.TRANSPARENT, Overlay.State.TRANSPARENT,
							Overlay.State.TRANSPARENT, Overlay.State.STATIONARY_OFF),
					Arrays.asList(Overlay.State.STATIONARY_ON, Overlay.State.TRANSPARENT, Overlay.State.TRANSPARENT,
							Overlay.State.TRANSPARENT, Overlay.State.STATIONARY_ON),
					Arrays.asList(Overlay.State.STATIONARY_ON, Overlay.State.STATIONARY_ON, Overlay.State.STATIONARY_ON,
							Overlay.State.STATIONARY_ON, Overlay.State.STATIONARY_ON));
			final Overlay overlay1 = new OverlayStationary(statesOverlay1, RgbColor.RED, RgbColor.ORANGE);

			final List<List<Overlay.State>> statesOverlay2 = Arrays.asList(
					Arrays.asList(Overlay.State.ROLL_ON, Overlay.State.ROLL_ON, Overlay.State.ROLL_ON,
							Overlay.State.ROLL_ON, Overlay.State.ROLL_ON, Overlay.State.ROLL_ON, Overlay.State.ROLL_ON),
					Arrays.asList(Overlay.State.ROLL_ON, Overlay.State.TRANSPARENT, Overlay.State.ROLL_ON,
							Overlay.State.TRANSPARENT, Overlay.State.ROLL_ON, Overlay.State.TRANSPARENT,
							Overlay.State.ROLL_ON),
					Arrays.asList(Overlay.State.ROLL_ON, Overlay.State.ROLL_ON, Overlay.State.ROLL_ON,
							Overlay.State.ROLL_ON, Overlay.State.ROLL_ON, Overlay.State.ROLL_ON,
							Overlay.State.ROLL_ON));
			final Overlay overlay2 = new OverlayRoll(statesOverlay2, RgbColor.GREEN, 1, 10);

			final me.hmasrafchi.leddisplay.framework.scene.Scene firstScene = new OverlayedScene(
					Arrays.asList(overlay2, overlay1));

			final me.hmasrafchi.leddisplay.framework.scene.Scene secondScene = new RandomColorScene();

			final List<me.hmasrafchi.leddisplay.framework.scene.Scene> scenes = Arrays.asList(firstScene, secondScene);

			Gson gson = new GsonBuilder().setPrettyPrinting()
					.registerTypeAdapterFactory(
							RuntimeTypeAdapterFactory.of(me.hmasrafchi.leddisplay.framework.scene.Scene.class)
									.registerSubtype(OverlayedScene.class).registerSubtype(RandomColorScene.class))
					.registerTypeAdapter(Overlay.class, new InterfaceAdapter<Overlay>()).create();
			final String json = gson.toJson(scenes);

			final Path path = Paths.get(fileName);
			try (BufferedWriter writer = Files.newBufferedWriter(path)) {
				writer.write(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		return new TilePane(button);
	}
}