package org.temple.cis.kantlab.edgecomputing.client.resources;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Packet implements Serializable {

	private static final long serialVersionUID = 1L;

	private String deviceName;
	private String imageName;
	private String[] objectIds;
	private List<String> plates;
	private String buffer;
	private String resType;
	private Map<String, String> features = new HashMap<String, String>();
	private String makeModel;

	public Packet(String imageName, String buffer, String deviceName) {
		this.imageName = imageName;
		this.buffer = buffer;
		this.deviceName = deviceName;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String name) {
		this.imageName = name;
	}

	public String getBuffer() {
		return buffer;
	}

	public void setBuffer(String buffer) {
		this.buffer = buffer;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public Map<String, String> getFeatures() {
		return features;
	}

	public void setFeatures(Map<String, String> features) {
		this.features = features;
	}

	public String getResType() {
		return resType;
	}

	public void setResType(String resType) {
		this.resType = resType;
	}

	public String[] getObjectIds() {
		return objectIds;
	}

	public void setObjectIds(String[] objectIds) {
		this.objectIds = objectIds;
	}

	public List<String> getPlates() {
		return plates;
	}

	public void setPlates(List<String> plates) {
		this.plates = plates;
	}

	public String getMakeModel() {
		return makeModel;
	}

	public void setMakeModel(String makeModel) {
		this.makeModel = makeModel;
	}
}
