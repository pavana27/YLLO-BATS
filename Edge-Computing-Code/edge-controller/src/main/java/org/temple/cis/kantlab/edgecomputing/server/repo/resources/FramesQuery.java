package org.temple.cis.kantlab.edgecomputing.server.repo.resources;

import java.util.Date;

import org.springframework.data.annotation.Id;

public class FramesQuery {

	@Id
	private String id;

	private Date scheduledTime;

	private String licensePlate;

	private Boolean processed;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getScheduledTime() {
		return scheduledTime;
	}

	public void setScheduledTime(Date scheduledTime) {
		this.scheduledTime = scheduledTime;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public Boolean getProcessed() {
		return processed;
	}

	public void setProcessed(Boolean processed) {
		this.processed = processed;
	}
}