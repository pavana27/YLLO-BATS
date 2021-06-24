package org.temple.cis.kantlab.edgecomputing.client.resources;

import java.io.Serializable;

public class AplrResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String uuid;
	
	private AplrResult[] results;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public AplrResult[] getResults() {
		return results;
	}

	public void setResults(AplrResult[] results) {
		this.results = results;
	}
}