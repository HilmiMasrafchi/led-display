/**
 * 
 */
package me.hmasrafchi.leddisplay.consumer;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @author michelin
 *
 */
@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "java:jboss/exported/jms/queue/test") }, messageListenerInterface = MessageListener.class)
public class MyMDB implements MessageListener {
	@Override
	public void onMessage(final Message message) {
		final TextMessage textMessage = (TextMessage) message;
		try {
			System.out.println(textMessage.getText());
		} catch (NumberFormatException | JMSException e) {
			e.printStackTrace();
		}
	}
}