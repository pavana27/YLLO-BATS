package org.temple.cis.kantlab.edgecomputing.server.biz.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.temple.cis.kantlab.edgecomputing.server.registry.DeviceRegistry;
import org.temple.cis.kantlab.edgecomputing.server.repo.RecordRepository;
import org.temple.cis.kantlab.edgecomputing.server.repo.ScheduledQueryRepository;
import org.temple.cis.kantlab.edgecomputing.server.repo.resources.DeviceRecord;
import org.temple.cis.kantlab.edgecomputing.server.repo.resources.Record;
import org.temple.cis.kantlab.edgecomputing.server.resources.AplrCandidate;
import org.temple.cis.kantlab.edgecomputing.server.resources.AplrResponse;
import org.temple.cis.kantlab.edgecomputing.server.resources.AplrResult;
import org.temple.cis.kantlab.edgecomputing.server.resources.Packet;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractFramesService {

	@Value("${frames.location}")
	protected String path;
	
	@Value("${frames.queryResults.location}")
	protected String queryResults;

	@Value("${aplr.url}")
	protected String aplrURL;

	@Value("${aplr.secret.key}")
	protected String aplrKey;
	
	@Value("${device.query.uri}")
	protected String deviceQueryUri;

	@Autowired
	protected RestTemplate restTemplate;

	@Autowired
	protected RecordRepository repository;
	
	@Autowired
	protected DeviceRegistry registry;

	@Autowired
	protected ScheduledQueryRepository scheduledRepository;

	protected void getLicensePlateDetails(Packet packet) throws Exception {

		//System.out.println(packet.getBuffer());

		ResponseEntity<AplrResponse> response = restTemplate.postForEntity(aplrURL + aplrKey,
				new HttpEntity<>(packet.getBuffer()), AplrResponse.class);

		System.out.println(new ObjectMapper().writeValueAsString(response.getBody()));

		AplrResponse aplrResponse = response.getBody();
		AplrResult[] results = aplrResponse.getResults();
		for (AplrResult result : results) {
			AplrCandidate[] candidates = result.getCandidates();
			for (AplrCandidate candidate : candidates) {

				System.out.println(candidate.getTemplateIndex() + " -- " + candidate.getConfidence() + " -- "
						+ candidate.getPlate());

				List<String> plates = packet.getPlates();
				if (plates == null) {
					plates = new ArrayList<>();
				}
				plates.add(candidate.getPlate());
				packet.setPlates(plates);
				break;
			}
		}
	}

	protected void saveRecord(Packet packet, String deviceName) throws Exception {
		List<String> plates = packet.getPlates();
		for (String plate : plates) {
			Record record = repository.findByLicensePlate(plate);
			if (record == null) {
				record = new Record();
				record.setLicensePlate(plate);
				record.setMakeModel(packet.getMakeModel());
				record.setCreateDate(new Date());
				Map<String, List<DeviceRecord>> deviceRecords = new HashMap<>();
				List<DeviceRecord> dRecords = new ArrayList<>();
				DeviceRecord dRecord = new DeviceRecord();
				dRecord.setFrameId(packet.getImageName());
				dRecords.add(dRecord);
				deviceRecords.put(deviceName, dRecords);
				record.setDeviceRecords(deviceRecords);
				record.setLastUpdate(new Date());
			} else {
				Map<String, List<DeviceRecord>> deviceRecords = record.getDeviceRecords();
				if (deviceRecords != null) {
					List<DeviceRecord> records = deviceRecords.get(deviceName);
					if (records == null) {
						records = new ArrayList<DeviceRecord>();
					}
					DeviceRecord deviceRecord = new DeviceRecord();
					deviceRecord.setFrameId(packet.getImageName());
					records.add(deviceRecord);
					deviceRecords.put(deviceName, records);
				} else {
					deviceRecords = new HashMap<>();
					List<DeviceRecord> dRecords = new ArrayList<>();
					DeviceRecord dRecord = new DeviceRecord();
					dRecord.setFrameId(packet.getImageName());
					dRecords.add(dRecord);
					deviceRecords.put(deviceName, dRecords);
				}
				record.setLastUpdate(new Date());
				record.setDeviceRecords(deviceRecords);
			}
			repository.save(record);
		}
	}
}
