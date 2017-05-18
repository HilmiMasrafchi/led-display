/**
 * 
 */
package me.hmasrafchi.leddisplay.consumer.application;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import me.hmasrafchi.leddisplay.model.view.MatrixView;

/**
 * @author michelin
 *
 */
@Component
public class Server {
	@JmsListener(destination = "matrixUpdated", containerFactory = "myFactory")
	public void receiveMessage(MatrixView matrix) {
		System.out.println("Received <" + matrix + ">");
	}
}