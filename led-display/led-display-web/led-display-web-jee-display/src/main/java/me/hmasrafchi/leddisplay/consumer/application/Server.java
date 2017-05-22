/**
 * 
 */
package me.hmasrafchi.leddisplay.consumer.application;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.hmasrafchi.leddisplay.consumer.model.jpa.FrameJpa;
import me.hmasrafchi.leddisplay.consumer.model.jpa.LedJpa;
import me.hmasrafchi.leddisplay.consumer.model.jpa.LedRowJpa;
import me.hmasrafchi.leddisplay.consumer.model.jpa.MatrixJpa;
import me.hmasrafchi.leddisplay.consumer.model.jpa.RgbColorJpa;
import me.hmasrafchi.leddisplay.model.event.MatrixUpdatedEvent;
import me.hmasrafchi.leddisplay.model.view.LedView;
import me.hmasrafchi.leddisplay.model.view.RgbColorView;

/**
 * @author michelin
 *
 */
@ServerEndpoint("/live/{matrixId}")
@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "java:jboss/exported/jms/queue/test") }, messageListenerInterface = MessageListener.class)
public class Server implements MessageListener {
	@PersistenceContext
	private EntityManager entityManager;

	private final Map<BigInteger, Session> sessions = new HashMap<>();

	@OnOpen
	public void init(@PathParam("matrixId") String matrixId, final Session session) {
		final BigInteger valueOf = new BigInteger(matrixId);
		sessions.put(valueOf, session);

		final MatrixJpa matrix = entityManager.find(MatrixJpa.class, valueOf);
		if (matrix != null) {
			final MatrixUpdatedEvent event = mapDataModelToEvent(matrix);
			sendMatrixUpdatedEventToClient(session, event);
		}
	}

	private void sendMatrixUpdatedEventToClient(final Session session, final MatrixUpdatedEvent matrix) {
		final ObjectMapper objectMapper = new ObjectMapper();

		try {
			final String json = objectMapper.writeValueAsString(matrix);
			session.getBasicRemote().sendText(json);
		} catch (final IOException ioe) {
			ioe.printStackTrace();
		}
	}

	@Override
	public void onMessage(final Message message) {
		final ObjectMessage objectMessage = (ObjectMessage) message;
		try {
			final Object object = objectMessage.getObject();
			if (object instanceof MatrixUpdatedEvent) {
				final MatrixUpdatedEvent matrixUpdatedEvent = (MatrixUpdatedEvent) objectMessage.getObject();

				final MatrixJpa matrix = mapEventToDataModel(matrixUpdatedEvent);
				entityManager.merge(matrix);

				final Session session = sessions.get(matrixUpdatedEvent.getId());
				if (session != null) {
					sendMatrixUpdatedEventToClient(session, matrixUpdatedEvent);
				}
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	private MatrixUpdatedEvent mapDataModelToEvent(final MatrixJpa matrixDataModel) {
		final BigInteger matrixId = matrixDataModel.getId();
		final int rowCount = matrixDataModel.getRowCount();
		final int columnCount = matrixDataModel.getColumnCount();
		final List<List<List<LedView>>> compiledFrames = mapCompiledFramesFromDataModelToEvent(
				matrixDataModel.getCompiledFrames());

		return new MatrixUpdatedEvent(matrixId, rowCount, columnCount, compiledFrames);
	}

	private List<List<List<LedView>>> mapCompiledFramesFromDataModelToEvent(final List<FrameJpa> compiledFrames) {
		return compiledFrames.stream().map(frame -> {
			return frame.getLedRows().stream().map(frameRow -> {
				return frameRow.getLeds().stream().map(led -> {
					final String text = led.getText();
					final RgbColorJpa rgbColor = led.getRgbColor();
					return new LedView(text, new RgbColorView(rgbColor.getR(), rgbColor.getG(), rgbColor.getB()));
				}).collect(Collectors.toList());
			}).collect(Collectors.toList());
		}).collect(Collectors.toList());
	}

	private MatrixJpa mapEventToDataModel(final MatrixUpdatedEvent matrixUpdatedEvent) {
		final BigInteger matrixId = matrixUpdatedEvent.getId();
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
}