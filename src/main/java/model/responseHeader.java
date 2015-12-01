package model;

public class responseHeader {
	Integer status;
	Integer QTime;
	params param;
	
	responseHeader(){}

	public responseHeader(Integer status, Integer qTime, params param) {
		super();
		this.status = status;
		QTime = qTime;
		this.param = param;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getQTime() {
		return QTime;
	}

	public void setQTime(Integer qTime) {
		QTime = qTime;
	}

	public params getParam() {
		return param;
	}

	public void setParam(params param) {
		this.param = param;
	}
	
}
