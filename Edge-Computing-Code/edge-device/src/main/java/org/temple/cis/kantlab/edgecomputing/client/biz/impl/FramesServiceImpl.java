package org.temple.cis.kantlab.edgecomputing.client.biz.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.temple.cis.kantlab.edgecomputing.client.biz.FramesService;
import org.temple.cis.kantlab.edgecomputing.client.messaging.ISender;
import org.temple.cis.kantlab.edgecomputing.client.repo.FrameRepository;
import org.temple.cis.kantlab.edgecomputing.client.repo.resources.Frame;
import org.temple.cis.kantlab.edgecomputing.client.resources.Packet;

@Service
public class FramesServiceImpl implements FramesService {

	@Value("${frames.folder.all}")
	private String allFramesFolder;

	@Value("${frames.folder.sampling}")
	private String samplingFloder;

	@Value("${device.name}")
	private String deviceName;

	@Value("${controller.url}")
	private String controllerEndpoint;

	@Autowired
	private ISender frameSender;

	@Autowired
	private FrameRepository repository;

	static final long ONE_MINUTE_IN_MILLIS = 60000;// millisecs

	public Packet[] queryForFrames(String makeModel, String timeRange) throws Exception {

		Calendar date = Calendar.getInstance();
		long t = date.getTimeInMillis();
		Date dateRange = null;
		Packet[] packets = null;

		dateRange = new Date(t - (Integer.parseInt(timeRange) * ONE_MINUTE_IN_MILLIS));
		List<Frame> frames = repository.findByMakeModelAndLastUpdate(makeModel, dateRange);

		packets = new Packet[frames.size()];
		int count = 0;
		for (Frame frame : frames) {
			File file = new File(allFramesFolder + "/" + frame.getFrameId());
			FileInputStream stream = new FileInputStream(file);
			int size = stream.available();
			byte[] buffer = new byte[size];
			stream.read(buffer, 0, size);
			String base64String = Base64.encodeBase64String(buffer);
			Packet packet = new Packet(frame.getFrameId(), base64String, "");
			packets[count] = packet;
			stream.close();
			count++;
		}
		return packets;
	}

	public void sendFrame(String frameId) throws Exception {
		frameSender.sendSampling(frameId);
	}

	public void saveFrameDetails(String frameId) throws Exception {
		frameSender.sendFrame(frameId);
	}
}