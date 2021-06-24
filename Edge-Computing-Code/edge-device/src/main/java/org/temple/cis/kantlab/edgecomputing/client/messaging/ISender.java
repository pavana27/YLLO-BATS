package org.temple.cis.kantlab.edgecomputing.client.messaging;

public interface ISender {
	
	public void sendSampling(String frameId);
	
	public void sendFrame(String frameId);
}
