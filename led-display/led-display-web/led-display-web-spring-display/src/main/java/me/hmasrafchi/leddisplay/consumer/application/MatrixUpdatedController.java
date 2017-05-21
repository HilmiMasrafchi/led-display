/**
 * 
 */
package me.hmasrafchi.leddisplay.consumer.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import me.hmasrafchi.leddisplay.consumer.infrastructure.MatrixRepository;
import me.hmasrafchi.leddisplay.model.event.MatrixUpdatedEvent;

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
		matrixRepository.save(matrixUpdatedEvent);
		messagingTemplate.convertAndSend("/topic/matrixupdates/" + matrixUpdatedEvent.getMatrixId(),
				matrixUpdatedEvent);
	}

	@SubscribeMapping("/matrix/{matrixId}")
	public MatrixUpdatedEvent init(@DestinationVariable("matrixId") final Integer matrixId) {
		return matrixRepository.findByMatrixId(matrixId);
	}
}