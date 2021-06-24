package org.temple.cis.kantlab.edgecomputing.client.messaging;

import javax.jms.Session;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

public interface IReceiver {

	public void receiveSamplingFrame(@Payload String resource, @Headers MessageHeaders headers, Message<String> message,
			Session session) throws Exception;

	public void receiveFrameDetails(@Payload String resource, @Headers MessageHeaders headers, Message<String> message,
			Session session) throws Exception;

}
