/**
 * 
 */
package me.hmasrafchi.leddisplay.consumer.application;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

import me.hmasrafchi.leddisplay.consumer.model.jpa.MatrixJpa;

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

	private final Map<Integer, Session> sessions = new HashMap<>();

	@OnOpen
	public void init(@PathParam("matrixId") int matrixId, final Session session) {
		sessions.put(matrixId, session);

		final MatrixJpa matrix = entityManager.find(MatrixJpa.class, matrixId);
		if (matrix != null) {
			sendMatrixUpdatedEventToClient(session, matrix);
		}
	}

	private void sendMatrixUpdatedEventToClient(Session session, final MatrixJpa matrix) {
		final ObjectMapper objectMapper = new ObjectMapper();

		try {
			final String json = objectMapper.writeValueAsString(matrix);
			session.getBasicRemote().sendText(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onMessage(final Message message) {
		final ObjectMessage objectMessage = (ObjectMessage) message;
		try {
			final Object object = objectMessage.getObject();
			// if (object instanceof MatrixUpdatedEvent) {
			// final MatrixUpdatedEvent matrixCreatedEvent =
			// (MatrixUpdatedEvent) objectMessage.getObject();
			//
			// final int matrixId = matrixCreatedEvent.getMatrixId();
			// final List<FrameJpa> compiledFrames =
			// mapCompiledFramesFromEventToJpaModel(
			// matrixCreatedEvent.getCompiledFrames());
			//
			// final MatrixJpa matrix = new MatrixJpa(matrixId, compiledFrames);
			// entityManager.merge(matrix);
			//
			// final Session session =
			// sessions.get(matrixCreatedEvent.getMatrixId());
			// if (session != null) {
			// sendMatrixUpdatedEventToClient(session, matrix);
			// }
			// }

		} catch (JMSException e) {
			e.printStackTrace();
		}

	}

	// private List<FrameJpa> mapCompiledFramesFromEventToJpaModel(final
	// List<List<List<LedView>>> compiledFrames) {
	// return compiledFrames.stream().map(frame -> {
	// final List<LedRowJpa> frameJpaList = frame.stream().map(ledRow -> {
	// final List<LedJpa> ledRowJpaList = ledRow.stream().map(led -> {
	// final String text = led.getText();
	// final RgbColorJpa rgbColor =
	// mapRgbColorFromEventToJpa(led.getRgbColor());
	//
	// return new LedJpa(text, rgbColor);
	// }).collect(Collectors.toList());
	// return new LedRowJpa(ledRowJpaList);
	// }).collect(Collectors.toList());
	// return new FrameJpa(frameJpaList);
	// }).collect(Collectors.toList());
	// }
	//
	// private RgbColorJpa mapRgbColorFromEventToJpa(RgbColorView rgbColor) {
	// return new RgbColorJpa(rgbColor.getR(), rgbColor.getG(),
	// rgbColor.getB());
	// }
}