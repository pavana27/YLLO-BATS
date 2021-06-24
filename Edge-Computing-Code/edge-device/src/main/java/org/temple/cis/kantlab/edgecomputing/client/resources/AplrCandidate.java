package org.temple.cis.kantlab.edgecomputing.client.resources;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AplrCandidate implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("matches_template")
	private Integer templateIndex;

	private Double confidence;

	private String plate;

	public Integer getTemplateIndex() {
		return templateIndex;
	}

	public void setTemplateIndex(Integer templateIndex) {
		this.templateIndex = templateIndex;
	}

	public Double getConfidence() {
		return confidence;
	}

	public void setConfidence(Double confidence) {
		this.confidence = confidence;
	}

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}
}