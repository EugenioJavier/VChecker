package model;

import java.util.List;

public class RespArchiva {
	List<String> versions;
	
	RespArchiva(){}

	public RespArchiva(List<String> versions) {
		super();
		this.versions = versions;
	}

	public List<String> getVersions() {
		return versions;
	}

	public void setVersions(List<String> versions) {
		this.versions = versions;
	}
	
	
}
