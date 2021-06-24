package org.temple.cis.kantlab.edgecomputing.client.biz;

import org.temple.cis.kantlab.edgecomputing.client.resources.Packet;

public interface FramesService {

	public Packet[] queryForFrames(String makeModel, String timeRange) throws Exception;

	public void sendFrame(String frameId) throws Exception;

	public void saveFrameDetails(String frameId) throws Exception;
}