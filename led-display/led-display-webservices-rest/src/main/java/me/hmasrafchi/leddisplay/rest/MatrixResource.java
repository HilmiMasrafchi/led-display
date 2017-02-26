/**
 * 
 */
package me.hmasrafchi.leddisplay.rest;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import lombok.Data;
import me.hmasrafchi.leddisplay.api.Led;
import me.hmasrafchi.leddisplay.api.LedRgbColor;
import me.hmasrafchi.leddisplay.api.LedState;
import me.hmasrafchi.leddisplay.model.CompiledFrames;
import me.hmasrafchi.leddisplay.model.Matrix;
import me.hmasrafchi.leddisplay.model.MatrixDefault;
import me.hmasrafchi.leddisplay.model.Overlay;
import me.hmasrafchi.leddisplay.model.OverlayRollHorizontally;
import me.hmasrafchi.leddisplay.model.OverlayStationary;
import me.hmasrafchi.leddisplay.model.Scene;
import me.hmasrafchi.leddisplay.model.SceneOverlayed;
import me.hmasrafchi.leddisplay.rest.persist.MatrixEntity;
import me.hmasrafchi.leddisplay.rest.persist.MatrixRepository;
import me.hmasrafchi.leddisplay.rest.persist.SceneFrames;
import me.hmasrafchi.leddisplay.util.TwoDimensionalListRectangular;

/**
 * @author michelin
 *
 */
@Path("/matrix")
public class MatrixResource {
	@Inject
	private MatrixRepository matrixRepository;

	@GET
	@Path("{matrixId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMatrix(@PathParam("matrixId") final Integer id) {
		return matrixRepository.get(id).map(matrixEntity -> Response.ok(matrixEntity).build())
				.orElse(Response.status(Response.Status.NOT_FOUND).build());
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(final MatrixEntity matrix, @Context UriInfo uriInfo) {
		matrixRepository.create(matrix);

		final UriBuilder builder = uriInfo.getAbsolutePathBuilder();
		builder.path(matrix.getId().toString());
		return Response.created(builder.build()).build();
	}

	@PUT
	@Path("{matrixId}/scenes/stationary")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addSceneStationary(@PathParam("matrixId") final Integer matrixId,
			final AddSceneStationaryCommand addSceneStationaryCommand) {
		final Optional<MatrixEntity> optional = matrixRepository.get(matrixId);
		optional.ifPresent(matrixEntity -> {
			final Matrix matrix = new MatrixDefault();
			final Scene scene = new OverlayStationary(
					new TwoDimensionalListRectangular<>(addSceneStationaryCommand.getStates()),
					addSceneStationaryCommand.getOnColor(), addSceneStationaryCommand.getOffColor(),
					addSceneStationaryCommand.getDuration());
			final CompiledFrames compiledFrames = matrix.compile(scene, matrixEntity.getRowCount(),
					matrixEntity.getColumnCount());
			final List<List<List<Led>>> compiledFramesOutput = getFrames(compiledFrames);

			final SceneFrames sceneFrames = new SceneFrames();
			sceneFrames.setOwnerMatrix(matrixEntity);
			sceneFrames.setFrames(compiledFramesOutput);

			matrixEntity.getSceneFrames().add(sceneFrames);
			matrixRepository.update(matrixEntity);
		});

		return optional.map(matrix -> Response.noContent().build()).orElse(Response.status(NOT_FOUND).build());
	}

	@PUT
	@Path("{matrixId}/scenes/roll")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addSceneRollHorizontally(@PathParam("matrixId") final Integer matrixId,
			final AddSceneRollHorizontallyCommand addSceneRollHorizontallyCommand) {
		final Optional<MatrixEntity> optional = matrixRepository.get(matrixId);
		optional.ifPresent(matrixEntity -> {
			final Matrix matrix = new MatrixDefault();
			final Scene scene = new OverlayRollHorizontally(
					new TwoDimensionalListRectangular<>(addSceneRollHorizontallyCommand.getStates()),
					addSceneRollHorizontallyCommand.getOnColor(), addSceneRollHorizontallyCommand.getOffColor(),
					addSceneRollHorizontallyCommand.getBeginIndexMark(),
					addSceneRollHorizontallyCommand.getYPosition());
			final CompiledFrames compiledFrames = matrix.compile(scene, matrixEntity.getRowCount(),
					matrixEntity.getColumnCount());
			final List<List<List<Led>>> compiledFramesOutput = getFrames(compiledFrames);

			final SceneFrames sceneFrames = new SceneFrames();
			sceneFrames.setOwnerMatrix(matrixEntity);
			sceneFrames.setFrames(compiledFramesOutput);

			matrixEntity.getSceneFrames().add(sceneFrames);
			matrixRepository.update(matrixEntity);
		});

		return optional.map(matrix -> Response.noContent().build()).orElse(Response.status(NOT_FOUND).build());
	}

	@PUT
	@Path("{matrixId}/scene/overlay")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addSceneOverlayed(@PathParam("matrixId") final Integer matrixId,
			final AddSceneOverlayedCommand addSceneOverlayedCommand) {
		final Optional<MatrixEntity> optional = matrixRepository.get(matrixId);
		optional.ifPresent(matrixEntity -> {
			final Matrix matrix = new MatrixDefault();

			List<Overlay> collect = addSceneOverlayedCommand.getCommands().stream().map(command -> command.getScene())
					.collect(Collectors.toList());
			final Scene scene = new SceneOverlayed(collect);

			final CompiledFrames compiledFrames = matrix.compile(scene, matrixEntity.getRowCount(),
					matrixEntity.getColumnCount());
			final List<List<List<Led>>> compiledFramesOutput = getFrames(compiledFrames);

			final SceneFrames sceneFrames = new SceneFrames();
			sceneFrames.setOwnerMatrix(matrixEntity);
			sceneFrames.setFrames(compiledFramesOutput);

			matrixEntity.getSceneFrames().add(sceneFrames);
			matrixRepository.update(matrixEntity);
		});

		return optional.map(matrix -> Response.noContent().build()).orElse(Response.status(NOT_FOUND).build());
	}

	static interface AddSceneCommand {
		Overlay getScene();
	}

	@Data
	static class AddSceneStationaryCommand implements AddSceneCommand {
		List<List<LedState>> states;
		LedRgbColor onColor;
		LedRgbColor offColor;
		int duration;

		@Override
		public Overlay getScene() {
			return new OverlayStationary(new TwoDimensionalListRectangular<>(states), onColor, offColor, duration);
		}
	}

	@Data
	static class AddSceneRollHorizontallyCommand implements AddSceneCommand {
		List<List<LedState>> states;
		LedRgbColor onColor;
		LedRgbColor offColor;
		int beginIndexMark;
		int yPosition;

		@Override
		public Overlay getScene() {
			return new OverlayRollHorizontally(new TwoDimensionalListRectangular<>(states), onColor, offColor,
					beginIndexMark, yPosition);
		}
	}

	@Data
	static class AddSceneOverlayedCommand {
		List<AddSceneCommand> commands;
	}

	private List<List<List<Led>>> getFrames(final CompiledFrames frames) {
		final List<List<List<Led>>> result = new ArrayList<>();

		final ListIterator<TwoDimensionalListRectangular<Led>> listIterator = frames.listIterator();
		while (listIterator.hasNext()) {
			final TwoDimensionalListRectangular<Led> currentFrameInput = listIterator.next();
			final List<List<Led>> currentFrameOutput = new ArrayList<>();
			for (int i = 0; i < currentFrameInput.getRowCount(); i++) {
				final List<Led> rowAt = currentFrameInput.getRowAt(i);
				currentFrameOutput.add(rowAt);
			}

			result.add(currentFrameOutput);
		}

		return result;
	}
}