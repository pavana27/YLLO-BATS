package org.temple.cis.kantlab.edgecomputing.client.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.temple.cis.kantlab.edgecomputing.client.biz.FramesService;
import org.temple.cis.kantlab.edgecomputing.client.resources.Packet;

@RestController
@RequestMapping("/edge-device")
public class EdgeRequestHandler {

	@Autowired
	private FramesService framesService;

	@GetMapping(path = "/frames/query", produces = MediaType.APPLICATION_JSON_VALUE)
	public Packet[] frameList(@RequestParam("makeModel") String makeModel,
			@RequestParam(value = "timeRange", defaultValue = "30") String timeRange) {
		Packet[] packets = null;
		try {
			packets = framesService.queryForFrames(makeModel, timeRange);
		} catch (Exception e) {
			System.out.println("Error while retreving the results");
		}

		return packets;
	}

	@GetMapping(path = "/frames")
	public void frames(@RequestParam("frameId") String frameId) {
		try {
			framesService.sendFrame(frameId);
		} catch (Exception e) {
			System.out.println("Error while retreving the results");
		}
	}

	@GetMapping(path = "/frames/details")
	public void saveFrameDetails(@RequestParam("frameId") String frameId) {
		try {
			framesService.saveFrameDetails(frameId);
		} catch (Exception e) {
			System.out.println("Error while retreving the results");
		}
	}
}
