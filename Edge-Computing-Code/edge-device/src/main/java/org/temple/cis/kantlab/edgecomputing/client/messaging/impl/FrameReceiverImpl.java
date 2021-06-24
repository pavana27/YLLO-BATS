package org.temple.cis.kantlab.edgecomputing.client.messaging.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

import javax.jms.Session;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.temple.cis.kantlab.edgecomputing.client.messaging.IReceiver;
import org.temple.cis.kantlab.edgecomputing.client.repo.FrameRepository;
import org.temple.cis.kantlab.edgecomputing.client.repo.resources.Frame;
import org.temple.cis.kantlab.edgecomputing.client.resources.AplrMakeModel;
import org.temple.cis.kantlab.edgecomputing.client.resources.AplrResponse;
import org.temple.cis.kantlab.edgecomputing.client.resources.AplrResult;
import org.temple.cis.kantlab.edgecomputing.client.resources.AplrVehicle;
import org.temple.cis.kantlab.edgecomputing.client.resources.Packet;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class FrameReceiverImpl implements IReceiver {

	@Value("${frames.folder.sampling}")
	private String samplingFloder;
	
	@Value("${frames.folder.all}")
	private String allFramesFolder;

	@Value("${device.name}")
	private String deviceName;

	@Value("${controller.url}")
	private String controllerEndpoint;

	@Value("${aplr.url}")
	private String aplrURL;

	@Value("${aplr.secret.key}")
	private String aplrKey;

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private FrameRepository repository;

	@JmsListener(destination = "FRAMES_QUEUE")
	public void receiveSamplingFrame(@Payload String resource, @Headers MessageHeaders headers, Message<String> message,
			Session session) throws Exception {
		sendFrame(resource);
		System.out.println("payload -- " + resource);
		System.out.println("headers -- " + headers);
		System.out.println("message -- " + message);
		System.out.println("session -- " + session);
	}
	
	@JmsListener(destination = "FRAME_DETAILS_QUEUE")
	public void receiveFrameDetails(@Payload String resource, @Headers MessageHeaders headers, Message<String> message,
			Session session) throws Exception {
		saveFrame(resource);
		
		System.out.println("payload -- " + resource);
		System.out.println("headers -- " + headers);
		System.out.println("message -- " + message);
		System.out.println("session -- " + session);
	}

	protected void sendFrame(String frameId) throws Exception {
		File file = new File(samplingFloder + "/" + frameId);
		FileInputStream stream = new FileInputStream(file);
		int size = stream.available();
		byte[] buffer = new byte[size];
		stream.read(buffer, 0, size);
		String base64String = Base64.encodeBase64String(buffer);
		Packet packet = new Packet(frameId, base64String, deviceName);
		stream.close();

		getMakeModelDetails(packet);

		HttpHeaders headers = new HttpHeaders();
		headers.add("userName", "admin");
		headers.add("password", "passw0rd");
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Packet[]> entity = new HttpEntity<>(new Packet[] { packet }, headers);

		ResponseEntity<String> response = restTemplate.exchange(controllerEndpoint + "/" + deviceName, HttpMethod.POST,
				entity, String.class);

		System.out.println(response.getStatusCodeValue());
		System.out.println(response.getBody());
	}
	
	protected void saveFrame(String frameId) throws Exception {
		File file = new File(allFramesFolder + "/" + frameId);
		FileInputStream stream = new FileInputStream(file);
		int size = stream.available();
		byte[] buffer = new byte[size];
		stream.read(buffer, 0, size);
		String base64String = Base64.encodeBase64String(buffer);
		Packet packet = new Packet(frameId, base64String, deviceName);
		stream.close();

		getMakeModelDetails(packet);
		
		Frame frame = new Frame();
		frame.setFrameId(frameId);
		frame.setMakeModel(packet.getMakeModel());
		frame.setLastUpdate(new Date());
		
		repository.save(frame);
	}

	protected void getMakeModelDetails(Packet packet) throws Exception {
		System.out.println(packet.getBuffer());
		ResponseEntity<AplrResponse> response = restTemplate.postForEntity(aplrURL + aplrKey,
				new HttpEntity<>(packet.getBuffer()), AplrResponse.class);
		System.out.println(new ObjectMapper().writeValueAsString(response.getBody()));
		AplrResponse aplrResponse = response.getBody();
		AplrResult[] results = aplrResponse.getResults();
		for (AplrResult result : results) {
			AplrVehicle vehicle = result.getVehicle();
			for (AplrMakeModel makeModel : vehicle.getMakeModels()) {
				System.out.println(makeModel.getName());
				packet.setMakeModel(makeModel.getName());
				break;
			}
		}
	}
}
