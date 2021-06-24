package org.temple.cis.kantlab.edgecomputing.server.repo.resources;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;

public class Record {

	@Id
	private String id;

	private String licensePlate;
	private Date createDate;
	private Date lastUpdate;
	private String makeModel;

	private Map<String, List<DeviceRecord>> deviceRecords;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Map<String, List<DeviceRecord>> getDeviceRecords() {
		return deviceRecords;
	}

	public void setDeviceRecords(Map<String, List<DeviceRecord>> deviceRecords) {
		this.deviceRecords = deviceRecords;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@Override
	public String toString() {
		return "Record [id=" + id + ", licensePlate=" + licensePlate + ", createDate=" + createDate + ", lastUpdate="
				+ lastUpdate + ", deviceRecords=" + deviceRecords + "]";
	}

	public String getMakeModel() {
		return makeModel;
	}

	public void setMakeModel(String makeModel) {
		this.makeModel = makeModel;
	}
}