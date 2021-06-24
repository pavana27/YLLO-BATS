package org.temple.cis.kantlab.edgecomputing.client.messaging.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.temple.cis.kantlab.edgecomputing.client.messaging.ISender;

@Service
public class FrameSenderImpl implements ISender {

	@Autowired
	private JmsTemplate jmsTemplate;

	public void sendSampling(String frameId) {
		jmsTemplate.convertAndSend("FRAMES_QUEUE", frameId);
	}

	public void sendFrame(String frameId) {
		jmsTemplate.convertAndSend("FRAME_DETAILS_QUEUE", frameId);
	}

}
