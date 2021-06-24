package org.temple.cis.kantlab.edgecomputing.server.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.temple.cis.kantlab.edgecomputing.server.biz.FramesService;
import org.temple.cis.kantlab.edgecomputing.server.resources.Packet;
import org.temple.cis.kantlab.edgecomputing.server.resources.Response;

@RestController
@RequestMapping("/edge-controller/frames")
public class FramesHandler {

	@Autowired
	private FramesService framesService;

	@PostMapping(path = "/{deviceName}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response frames(@RequestHeader("userName") String loginId, @RequestHeader("password") String loginPassword,
			@RequestBody Packet[] packets, @PathVariable("deviceName") String deviceName) {
		Response response = null;
		try {
			framesService.saveFrames(deviceName, packets);
			response = new Response("Success");
		} catch (Exception e) {
			System.out.println(e);
			response = new Response("Error");
		}

		return response;
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> frames(@RequestHeader("userName") String loginId,
			@RequestHeader("password") String loginPassword, @RequestParam("query") String query,
			@RequestParam(value = "timeRange", defaultValue = "30") String timeRange,
			@RequestParam(value = "future", defaultValue = "false") Boolean future) {
		try {
			framesService.queryForFrames(query, timeRange, future);
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
}