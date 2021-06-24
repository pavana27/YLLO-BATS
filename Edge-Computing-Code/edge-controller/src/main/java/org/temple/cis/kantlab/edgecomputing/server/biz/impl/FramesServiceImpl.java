package org.temple.cis.kantlab.edgecomputing.server.biz.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.temple.cis.kantlab.edgecomputing.server.biz.FramesService;
import org.temple.cis.kantlab.edgecomputing.server.repo.resources.DeviceRecord;
import org.temple.cis.kantlab.edgecomputing.server.repo.resources.FramesQuery;
import org.temple.cis.kantlab.edgecomputing.server.repo.resources.Record;
import org.temple.cis.kantlab.edgecomputing.server.resources.Packet;

@Service
public class FramesServiceImpl extends AbstractFramesService implements FramesService {

	static final long ONE_MINUTE_IN_MILLIS = 60000;// millisecs

	public void saveFrames(String deviceName, Packet[] packets) throws Exception {

		for (Packet packet : packets) {
			File file = new File(path + "/" + deviceName);
			String directoryName = null;
			if (!file.exists()) {
				file.mkdirs();
			}
			directoryName = file.getAbsolutePath();
			FileOutputStream outStream = new FileOutputStream(directoryName + "/" + packet.getImageName());
			byte[] backToBytes = Base64.decodeBase64(packet.getBuffer());
			outStream.write(backToBytes);
			outStream.flush();
			outStream.close();

			getLicensePlateDetails(packet);

			saveRecord(packet, deviceName);
		}
	}

	public void queryForFrames(String query, String timeRange, Boolean future) throws Exception {

		Calendar date = Calendar.getInstance();
		long t = date.getTimeInMillis();
		Date dateRange = null;
		if (future) {
			dateRange = new Date(t + (Integer.parseInt(timeRange) * ONE_MINUTE_IN_MILLIS));
			FramesQuery framesQuery = new FramesQuery();
			framesQuery.setScheduledTime(dateRange);
			framesQuery.setLicensePlate(query);
			framesQuery.setProcessed(false);
			scheduledRepository.save(framesQuery);
			System.out.println("scheduled the query for " + query + " for time range " + timeRange);
		} else {
			dateRange = new Date(t - (Integer.parseInt(timeRange) * ONE_MINUTE_IN_MILLIS));
			getFrames(query, dateRange, timeRange);
		}
	}

	@SuppressWarnings("unchecked")
	protected void getFrames(String query, Date dateRange, String timeRange) throws Exception {
		Record record = repository.findByLicensePlateAndLastUpdateRange(query, dateRange);
		System.out.println("Record :: " + record);
		if (record != null) {
			Map<String, List<DeviceRecord>> records = record.getDeviceRecords();
			for (String key : records.keySet()) {
				List<DeviceRecord> dRecords = records.get(key);
				if (dRecords != null && dRecords.size() > 0) {
					String hostName = registry.getHostName(key);
					ResponseEntity<Object> response = restTemplate.getForEntity("http://" + hostName + ":8080"
							+ deviceQueryUri + record.getMakeModel() + "&timeRange=" + timeRange, Object.class);
					if (response.getStatusCodeValue() >= 200 && response.getStatusCodeValue() <= 299) {
						List<Map<String, String>> packets = (List<Map<String, String>>) response.getBody();
						//System.out.println(packets);
						if (packets != null) {
							for (Map<String, String> packet : packets) {
								File file = new File(
										queryResults + "/" + query + "/" + key);
								String directoryName = null;
								if (!file.exists()) {
									file.mkdirs();
								}
								directoryName = file.getAbsolutePath();
								FileOutputStream outStream = new FileOutputStream(
										directoryName + "/" + packet.get("imageName"));
								byte[] backToBytes = Base64.decodeBase64(packet.get("buffer"));
								outStream.write(backToBytes);
								outStream.flush();
								outStream.close();
							}
						}
					} else {
						System.out.println(response.getStatusCodeValue());
						//System.out.println(response.getBody());
					}
				}
			}
		}
	}

	@Scheduled(fixedRate = 5000)
	public void processScheduledQueries() throws Exception {
	 	System.out.println("Inside scheduler");
		List<FramesQuery> framesQueries = scheduledRepository.findByDateRange(new Date());
		System.out.println("Frames Queries :: " + framesQueries);
		Calendar date = Calendar.getInstance();
		long t = date.getTimeInMillis();
		Date dateRange = new Date(t + (5 * ONE_MINUTE_IN_MILLIS));
		if (framesQueries != null && framesQueries.size() > 0) {
			for (FramesQuery framesQuery : framesQueries) {
				getFrames(framesQuery.getLicensePlate(), dateRange, "5");
				framesQuery.setProcessed(true);
				scheduledRepository.save(framesQuery);
			}
		}
	}
}