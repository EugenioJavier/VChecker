package model;

public class params {
	String fl;
	String sort;
	String indent;
	String q;
	String wt;
	String rows;
	String version;
	
	params(){}

	public params(String fl, String sort, String indent, String q, String wt,
			String rows, String version) {
		super();
		this.fl = fl;
		this.sort = sort;
		this.indent = indent;
		this.q = q;
		this.wt = wt;
		this.rows = rows;
		this.version = version;
	}

	public String getFl() {
		return fl;
	}

	public void setFl(String fl) {
		this.fl = fl;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getIndent() {
		return indent;
	}

	public void setIndent(String indent) {
		this.indent = indent;
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public String getWt() {
		return wt;
	}

	public void setWt(String wt) {
		this.wt = wt;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}	
	
}
