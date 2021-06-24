package org.temple.cis.kantlab.edgecomputing.server.registry;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class DeviceRegistry {

	private Map<String, String> DEVICE_NAMES = new HashMap<>();

	public boolean registerDevice(String deviceName, String hostName) {
		boolean added = true;

		DEVICE_NAMES.put(deviceName, hostName);

		return added;
	}
	
	public String getHostName(String deviceName) {
		
		return DEVICE_NAMES.get(deviceName);
	}
}