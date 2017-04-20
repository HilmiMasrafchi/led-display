/**
 * 
 */
package me.hmasrafchi.leddisplay.administration.application;

import static java.lang.String.valueOf;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.SystemException;
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
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import me.hmasrafchi.leddisplay.administration.infrastructure.MatrixRepository;
import me.hmasrafchi.leddisplay.administration.model.domain.Led;
import me.hmasrafchi.leddisplay.administration.model.domain.OverlayRollHorizontally;
import me.hmasrafchi.leddisplay.administration.model.domain.OverlayStationary;
import me.hmasrafchi.leddisplay.administration.model.domain.Scene;
import me.hmasrafchi.leddisplay.administration.model.domain.SceneComposite;
import me.hmasrafchi.leddisplay.administration.model.domain.SceneOverlayed;
import me.hmasrafchi.leddisplay.administration.model.view.CreateMatrixCommand;
import me.hmasrafchi.leddisplay.administration.model.view.LedStateView;
import me.hmasrafchi.leddisplay.administration.model.view.MatrixView;
import me.hmasrafchi.leddisplay.administration.model.view.OverlayView;

/**
 * @author michelin
 *
 */
@Stateless
@Path(PathLiterals.MATRICES)
public class MatrixResource {
	@Inject
	private MatrixRepository matrixRepository;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createMatrix(final CreateMatrixCommand createMatrixCommand, @Context UriInfo uriInfo)
			throws IllegalStateException, SecurityException, SystemException {
		final MatrixView matrixView = new MatrixView(createMatrixCommand.getRowCount(),
				createMatrixCommand.getColumnCount(), createMatrixCommand.getScenes());

		final Object matrixId = matrixRepository.create(matrixView);
		final UriBuilder builder = uriInfo.getAbsolutePathBuilder();
		final URI createdMatrixLocationURI = builder.path(valueOf(matrixId)).build();

		return Response.created(createdMatrixLocationURI).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateMatrix(final MatrixView matrix) {
		matrixRepository.update(matrix);
		return Response.noContent().build();
	}

	@GET
	@Path("{matrixId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("matrixId") final int matrixId) {
		return matrixRepository.find(matrixId).map(matrix -> Response.ok(matrix).build())
				.orElse(Response.status(Status.NOT_FOUND).build());
	}

	@GET
	@Path("{matrixId}/compiled_frames")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCompiledFrames(@PathParam("matrixId") final int matrixId) {
		return matrixRepository.find(matrixId).map(matrixEntity -> {
			final int rowCount = matrixEntity.getRowCount();

			final int columnCount = matrixEntity.getColumnCount();

			final List<Scene> scenes = new ArrayList<>();

			Optional<Scene> of = null;
			final List<List<OverlayView>> scenesEntity = matrixEntity.getScenes();
			if (scenesEntity == null || scenesEntity.isEmpty()) {
				of = Optional.empty();
			} else {
				for (final List<OverlayView> scene : scenesEntity) {
					final List<me.hmasrafchi.leddisplay.administration.model.domain.Overlay> overlays = new ArrayList<>();

					for (final OverlayView overlay : scene) {
						if (overlay instanceof me.hmasrafchi.leddisplay.administration.model.view.OverlayStationaryView) {
							final me.hmasrafchi.leddisplay.administration.model.view.OverlayStationaryView overlayView = (me.hmasrafchi.leddisplay.administration.model.view.OverlayStationaryView) overlay;

							final List<List<Led.State>> states = new ArrayList<>();
							final List<List<LedStateView>> statesEntity = overlayView.getStates();
							for (final List<LedStateView> stateRowEntity : statesEntity) {
								final List<Led.State> stateRow = new ArrayList<>();
								for (final LedStateView stateEntity : stateRowEntity) {
									Led.State ledState = Led.State.valueOf(stateEntity.name());
									stateRow.add(ledState);
								}
								states.add(stateRow);
							}

							final me.hmasrafchi.leddisplay.administration.model.view.RgbColorView onColorEntity = overlayView
									.getOnColor();
							me.hmasrafchi.leddisplay.administration.model.domain.RgbColor onColor = new me.hmasrafchi.leddisplay.administration.model.domain.RgbColor(
									onColorEntity.getR(), onColorEntity.getG(), onColorEntity.getB());

							final me.hmasrafchi.leddisplay.administration.model.view.RgbColorView offColorEntity = overlayView
									.getOffColor();
							me.hmasrafchi.leddisplay.administration.model.domain.RgbColor offColor = new me.hmasrafchi.leddisplay.administration.model.domain.RgbColor(
									offColorEntity.getR(), offColorEntity.getG(), offColorEntity.getB());

							final int duration = overlayView.getDuration();

							final OverlayStationary overlayStationary = new OverlayStationary(states, onColor, offColor,
									duration);
							overlays.add(overlayStationary);
						}

						if (overlay instanceof me.hmasrafchi.leddisplay.administration.model.view.OverlayRollHorizontallyView) {
							final me.hmasrafchi.leddisplay.administration.model.view.OverlayRollHorizontallyView overlayEntity = (me.hmasrafchi.leddisplay.administration.model.view.OverlayRollHorizontallyView) overlay;

							final List<List<Led.State>> states = new ArrayList<>();
							final List<List<LedStateView>> statesEntity = overlayEntity.getStates();
							for (final List<LedStateView> stateRowEntity : statesEntity) {
								final List<Led.State> stateRow = new ArrayList<>();
								for (final LedStateView stateEntity : stateRowEntity) {
									Led.State ledState = Led.State.valueOf(stateEntity.name());
									stateRow.add(ledState);
								}
								states.add(stateRow);
							}

							final me.hmasrafchi.leddisplay.administration.model.view.RgbColorView onColorEntity = overlayEntity
									.getOnColor();
							me.hmasrafchi.leddisplay.administration.model.domain.RgbColor onColor = new me.hmasrafchi.leddisplay.administration.model.domain.RgbColor(
									onColorEntity.getR(), onColorEntity.getG(), onColorEntity.getB());

							final me.hmasrafchi.leddisplay.administration.model.view.RgbColorView offColorEntity = overlayEntity
									.getOffColor();
							me.hmasrafchi.leddisplay.administration.model.domain.RgbColor offColor = new me.hmasrafchi.leddisplay.administration.model.domain.RgbColor(
									offColorEntity.getR(), offColorEntity.getG(), offColorEntity.getB());

							final int beginIndexMark = overlayEntity.getBeginIndexMark();

							final int yposition = overlayEntity.getYposition();

							final OverlayRollHorizontally overlayStationary = new OverlayRollHorizontally(states,
									onColor, offColor, beginIndexMark, yposition);
							overlays.add(overlayStationary);
						}
					}

					scenes.add(new SceneOverlayed(overlays));
				}
				final SceneComposite sceneComposite = new SceneComposite(scenes);
				of = Optional.of(sceneComposite);
			}

			final me.hmasrafchi.leddisplay.administration.model.domain.Matrix matrix = new me.hmasrafchi.leddisplay.administration.model.domain.Matrix(
					rowCount, columnCount, of);
			return matrix.getCompiledFrames().map(compiledFramesDomain -> {
				final List<List<List<me.hmasrafchi.leddisplay.administration.model.view.LedView>>> collect = compiledFramesDomain
						.stream().map(frame -> {
							final List<List<Led>> frameData = frame.getFrameData();
							final List<List<me.hmasrafchi.leddisplay.administration.model.view.LedView>> frameView = new ArrayList<>();
							for (final List<Led> ledRow : frameData) {
								final List<me.hmasrafchi.leddisplay.administration.model.view.LedView> ledRowView = new ArrayList<>();
								for (final Led ledDomain : ledRow) {
									final String text = ledDomain.getText();
									final me.hmasrafchi.leddisplay.administration.model.domain.RgbColor rgbColor = ledDomain
											.getRgbColor();
									final me.hmasrafchi.leddisplay.administration.model.view.RgbColorView rgbColorView = new me.hmasrafchi.leddisplay.administration.model.view.RgbColorView(
											rgbColor.getR(), rgbColor.getG(), rgbColor.getB());

									final me.hmasrafchi.leddisplay.administration.model.view.LedView led = new me.hmasrafchi.leddisplay.administration.model.view.LedView(
											text, rgbColorView);

									ledRowView.add(led);
								}
								frameView.add(ledRowView);
							}

							return frameView;
						}).collect(Collectors.toList());

				final me.hmasrafchi.leddisplay.administration.model.view.CompiledFrames compiledFrameView = new me.hmasrafchi.leddisplay.administration.model.view.CompiledFrames(
						collect);
				return Response.ok(compiledFrameView).build();
			}).orElse(Response.status(Status.NOT_FOUND).build());
		}).orElse(Response.status(Status.NOT_FOUND).build());
	}
}