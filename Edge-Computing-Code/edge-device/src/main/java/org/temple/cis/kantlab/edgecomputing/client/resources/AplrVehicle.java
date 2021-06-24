package org.temple.cis.kantlab.edgecomputing.client.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AplrVehicle {

	@JsonProperty("make_model")
	private AplrMakeModel[] makeModels;

	public AplrMakeModel[] getMakeModels() {
		return makeModels;
	}

	public void setMakeModels(AplrMakeModel[] makeModels) {
		this.makeModels = makeModels;
	}
}