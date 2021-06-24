package org.temple.cis.kantlab.edgecomputing.server.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.temple.cis.kantlab.edgecomputing.server.registry.DeviceRegistry;
import org.temple.cis.kantlab.edgecomputing.server.resources.Response;

@RestController
@RequestMapping("/edge-controller/admin")
public class AdminHandler {

	@Autowired
	private DeviceRegistry registry;

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> register(@RequestHeader("userName") String loginId,
			@RequestHeader("password") String loginPassword, @RequestParam("name") String deviceName,
			@RequestParam("ipAddress") String hostName) {
		ResponseEntity<Response> response = null;

		Boolean registered = registry.registerDevice(deviceName, hostName);

		if (!registered) {
			response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Failed Registration"));
		} else {
			response = ResponseEntity.ok().body(new Response("Registered"));
		}

		return response;
	}
}
