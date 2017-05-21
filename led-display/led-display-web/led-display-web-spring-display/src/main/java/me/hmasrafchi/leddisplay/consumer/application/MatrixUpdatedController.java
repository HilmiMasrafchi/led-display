/**
 * 
 */
package me.hmasrafchi.leddisplay.consumer.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import me.hmasrafchi.leddisplay.consumer.data.jpa.FrameJpa;
import me.hmasrafchi.leddisplay.consumer.data.jpa.LedJpa;
import me.hmasrafchi.leddisplay.consumer.data.jpa.LedRowJpa;
import me.hmasrafchi.leddisplay.consumer.data.jpa.MatrixJpa;
import me.hmasrafchi.leddisplay.consumer.data.jpa.RgbColorJpa;
import me.hmasrafchi.leddisplay.consumer.infrastructure.MatrixRepository;
import me.hmasrafchi.leddisplay.model.event.MatrixUpdatedEvent;
import me.hmasrafchi.leddisplay.model.view.LedView;
import me.hmasrafchi.leddisplay.model.view.RgbColorView;

/**
 * @author michelin
 *
 */
@Controller
public class MatrixUpdatedController {
	@Autowired
	private MatrixRepository matrixRepository;

	@Autowired
	public SimpMessageSendingOperations messagingTemplate;

	@JmsListener(destination = "domainEvents", containerFactory = "myFactory")
	public void receiveMessage(final MatrixUpdatedEvent matrixUpdatedEvent) {
		matrixRepository.save(mapEventToDataModel(matrixUpdatedEvent));
		messagingTemplate.convertAndSend("/topic/matrixupdates/" + matrixUpdatedEvent.getMatrixId(),
				matrixUpdatedEvent);
	}

	@SubscribeMapping("/matrix/{matrixId}")
	public MatrixUpdatedEvent init(@DestinationVariable("matrixId") Integer matrixId) {
		final MatrixJpa matrixEntity = matrixRepository.findOne(matrixId);
		return mapFromDataModelToEventModel(matrixEntity);
	}

	private MatrixJpa mapEventToDataModel(final MatrixUpdatedEvent matrixUpdatedEvent) {
		final Integer matrixId = matrixUpdatedEvent.getMatrixId();
		final int rowCount = matrixUpdatedEvent.getRowCount();
		final int columnCount = matrixUpdatedEvent.getColumnCount();
		final List<FrameJpa> mapCompiledFramesFromEventToJpaModel = mapCompiledFramesFromEventToDataModel(
				matrixUpdatedEvent.getCompiledFrames());

		return new MatrixJpa(matrixId, rowCount, columnCount, mapCompiledFramesFromEventToJpaModel);
	}

	private List<FrameJpa> mapCompiledFramesFromEventToDataModel(final List<List<List<LedView>>> compiledFrames) {
		return compiledFrames.stream().map(frame -> {
			final List<LedRowJpa> frameJpaList = frame.stream().map(ledRow -> {
				final List<LedJpa> ledRowJpaList = ledRow.stream().map(led -> {
					final String text = led.getText();
					final RgbColorJpa rgbColor = mapRgbColorFromEventToJpa(led.getRgbColor());

					return new LedJpa(text, rgbColor);
				}).collect(Collectors.toList());
				return new LedRowJpa(ledRowJpaList);
			}).collect(Collectors.toList());
			return new FrameJpa(frameJpaList);
		}).collect(Collectors.toList());

	}

	private RgbColorJpa mapRgbColorFromEventToJpa(RgbColorView rgbColor) {
		return new RgbColorJpa(rgbColor.getR(), rgbColor.getG(), rgbColor.getB());
	}

	private MatrixUpdatedEvent mapFromDataModelToEventModel(final MatrixJpa matrixJpa) {
		if (matrixJpa == null) {
			return null;
		}

		final Integer matrixId = matrixJpa.getMatrixId();
		final int rowCount = matrixJpa.getRowCount();
		final int columnCount = matrixJpa.getColumnCount();
		final List<List<List<LedView>>> collect = matrixJpa.getCompiledFrames().stream().map(frame -> {
			return frame.getLedRows().stream().map(ledRow -> {
				return ledRow.getLeds().stream().map(led -> {
					final RgbColorView rgbColor = new RgbColorView(led.getRgbColor().getR(), led.getRgbColor().getG(),
							led.getRgbColor().getB());
					return new LedView(led.getText(), rgbColor);
				}).collect(Collectors.toList());
			}).collect(Collectors.toList());
		}).collect(Collectors.toList());

		return new MatrixUpdatedEvent(matrixId, rowCount, columnCount, collect);
	}
}