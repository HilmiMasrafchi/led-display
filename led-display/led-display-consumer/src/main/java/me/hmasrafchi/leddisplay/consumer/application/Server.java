/**
 * 
 */
package me.hmasrafchi.leddisplay.consumer.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import me.hmasrafchi.leddisplay.consumer.CompiledFrames;
import me.hmasrafchi.leddisplay.consumer.Frame;
import me.hmasrafchi.leddisplay.consumer.FrameRow;
import me.hmasrafchi.leddisplay.consumer.Led;
import me.hmasrafchi.leddisplay.consumer.MatrixUpdatedEvent;

/**
 * @author michelin
 *
 */
@ServerEndpoint("/{matrixId}")
@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "java:jboss/exported/jms/queue/test") }, messageListenerInterface = MessageListener.class)
public class Server implements MessageListener {
	@PersistenceContext
	private EntityManager entityManager;

	private final Map<Integer, Session> sessions = new HashMap<>();

	@OnOpen
	public void init(@PathParam("matrixId") int matrixId, Session session) {
		sessions.put(matrixId, session);
		final CompiledFrames compiledFrames = entityManager.find(CompiledFrames.class, matrixId);
		final ObjectMapper objectMapper = new ObjectMapper();

		try {
			final String json = objectMapper.writeValueAsString(compiledFrames);
			session.getBasicRemote().sendText(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onMessage(final Message message) {
		final TextMessage textMessage = (TextMessage) message;
		try {
			final String text = textMessage.getText();
			final ObjectMapper objectMapper = new ObjectMapper();
			final MatrixUpdatedEvent readValue = objectMapper.readValue(text, MatrixUpdatedEvent.class);

			final List<Frame> compiledFramesList = new ArrayList<>();
			final List<List<List<Led>>> compiledFrames = readValue.getCompiledFrames();
			for (final List<List<Led>> currentFrameData : compiledFrames) {
				final List<FrameRow> frameRows = new ArrayList<>();
				for (final List<Led> currentFrameRow : currentFrameData) {
					final FrameRow frameRow = new FrameRow(currentFrameRow);
					frameRows.add(frameRow);
				}
				final Frame frame = new Frame(frameRows);
				compiledFramesList.add(frame);
			}

			final CompiledFrames frames = new CompiledFrames(readValue.getMatrixId(), compiledFramesList);
			entityManager.merge(frames);
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}