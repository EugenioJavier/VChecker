package model;

import java.util.List;

public class artifactList {
	
	List<art> artifacts;
	
	artifactList(){}

	public artifactList(List<art> artifacts) {
		super();
		this.artifacts = artifacts;
	}

	public List<art> getArtifacts() {
		return artifacts;
	}

	public void setArtifacts(List<art> artifacts) {
		this.artifacts = artifacts;
	}	
}
