/**
 * 
 */
package me.hmasrafchi.leddisplay.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javafx.scene.layout.BorderPane;
import me.hmasrafchi.leddisplay.jfx.RuntimeTypeAdapterFactory;

/**
 * @author michelin
 *
 */
public final class TestJSon {

	interface MyInterface {
		void m1();
	}

	static class C1 implements MyInterface {
		private static final String st = "static";
		private final BorderPane borderPane = new BorderPane();
		private final int i = 1;

		@Override
		public void m1() {
			System.out.println("C1");
		}
	}

	static class C2 implements MyInterface {
		private final int i = 2;

		@Override
		public void m1() {
			System.out.println("C2");
		}
	}

	static class C3 implements MyInterface {
		private final int i = 3;

		@Override
		public void m1() {
			System.out.println("C3");
		}
	}

	public static void main(String[] args) {
		writeJson();
		readJson();
	}

	private static void readJson() {
		final RuntimeTypeAdapterFactory<MyInterface> typeFactory = RuntimeTypeAdapterFactory
				.of(MyInterface.class, "type").registerSubtype(C1.class).registerSubtype(C2.class)
				.registerSubtype(C3.class);

		final Gson gson = new GsonBuilder().registerTypeAdapterFactory(typeFactory).create();

		final Path path = Paths.get("test.txt");
		try (final BufferedReader reader = Files.newBufferedReader(path)) {
			final Collection<MyInterface> coll = gson.fromJson(reader, new TypeToken<Collection<MyInterface>>() {
			}.getType());
			System.out.println(coll);
		} catch (final IOException e) {
			throw new RuntimeException("can not load data file");
		}
	}

	private static void writeJson() {
		final RuntimeTypeAdapterFactory<MyInterface> typeFactory = RuntimeTypeAdapterFactory
				.of(MyInterface.class, "type").registerSubtype(C1.class).registerSubtype(C2.class)
				.registerSubtype(C3.class);
		final Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapterFactory(typeFactory).create();

		final Collection<? extends MyInterface> coll = Arrays.asList(new C1(), new C2(), new C3());
		final String json = gson.toJson(coll);
		final Path path = Paths.get("test.txt");
		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			writer.write(json);
		} catch (IOException e) {
		}
	}
}