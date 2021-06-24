package org.temple.cis.kantlab.edgecomputing.server.biz;

import org.temple.cis.kantlab.edgecomputing.server.resources.Packet;

public interface FramesService {

	public void saveFrames(String deviceName, Packet[] packets) throws Exception;

	public void queryForFrames(String query, String timeRange, Boolean future) throws Exception;
}