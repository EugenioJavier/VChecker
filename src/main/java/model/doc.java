package model;

import java.util.List;

public class doc {
	String id;
	String g;
	String a;
	String v;
	String p;
	Long timestamp;
	List<String> tags;
	List<String> ec;
	
	doc(){}

	public doc(String id, String g, String a, String v, String p,
			Long timestamp, List<String> tags, List<String> ec) {
		super();
		this.id = id;
		this.g = g;
		this.a = a;
		this.v = v;
		this.p = p;
		this.timestamp = timestamp;
		this.tags = tags;
		this.ec = ec;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getG() {
		return g;
	}

	public void setG(String g) {
		this.g = g;
	}

	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}

	public String getV() {
		return v;
	}

	public void setV(String v) {
		this.v = v;
	}

	public String getP() {
		return p;
	}

	public void setP(String p) {
		this.p = p;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public List<String> getEc() {
		return ec;
	}

	public void setEc(List<String> ec) {
		this.ec = ec;
	}	
}
