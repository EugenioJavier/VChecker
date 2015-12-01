package model;

import java.util.List;

public class response {
	Integer numFound;
	Integer start;
	List<doc>docs;
	
	response(){}

	public response(Integer numFound, Integer start, List<doc> docs) {
		super();
		this.numFound = numFound;
		this.start = start;
		this.docs = docs;
	}

	public Integer getNumFound() {
		return numFound;
	}

	public void setNumFound(Integer numFound) {
		this.numFound = numFound;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public List<doc> getDocs() {
		return docs;
	}

	public void setDocs(List<doc> docs) {
		this.docs = docs;
	}
	
	
	
}
