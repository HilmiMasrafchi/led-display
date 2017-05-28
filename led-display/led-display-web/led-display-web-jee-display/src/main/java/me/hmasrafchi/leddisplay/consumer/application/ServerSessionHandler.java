/**
 * 
 */
package me.hmasrafchi.leddisplay.consumer.application;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.hmasrafchi.leddisplay.model.event.MatrixUpdatedEvent;

/**
 * @author michelin
 *
 */
@ApplicationScoped
public class ServerSessionHandler {
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final Map<BigInteger, Set<Session>> sessions = new HashMap<>();

	public void addSessionForMatrixWithId(final BigInteger matrixId, final Session session) {
		if (sessions.containsKey(matrixId)) {
			final Set<Session> set = sessions.get(matrixId);
			set.add(session);
		} else {
			final Set<Session> newSet = new HashSet<>();
			newSet.add(session);
			sessions.put(matrixId, newSet);
		}
	}

	public void removeSessionForMatrixWithId(final Session session) {
		sessions.entrySet().stream().forEach(entrySet -> {
			entrySet.getValue().remove(session);
		});
	}

	public void sendEventToAllClients(final MatrixUpdatedEvent event) {
		final BigInteger matrixId = event.getId();
		final Set<Session> set = sessions.get(matrixId);
		if (set != null) {
			set.forEach(session -> {
				sendEventToClient(session, event);
			});
		}
	}

	public void sendEventToClient(final Session session, final MatrixUpdatedEvent event) {
		try {
			final String json = objectMapper.writeValueAsString(event);
			session.getBasicRemote().sendText(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}