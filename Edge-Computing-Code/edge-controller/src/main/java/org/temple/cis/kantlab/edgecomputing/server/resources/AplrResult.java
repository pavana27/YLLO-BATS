package org.temple.cis.kantlab.edgecomputing.server.resources;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AplrResult implements Serializable{

	private static final long serialVersionUID = 1L;

	@JsonProperty("plate_index")
	private Integer plateIndex;

	private AplrCandidate[] candidates;

	public Integer getPlateIndex() {
		return plateIndex;
	}

	public void setPlateIndex(Integer plateIndex) {
		this.plateIndex = plateIndex;
	}

	public AplrCandidate[] getCandidates() {
		return candidates;
	}

	public void setCandidates(AplrCandidate[] candidates) {
		this.candidates = candidates;
	}
}