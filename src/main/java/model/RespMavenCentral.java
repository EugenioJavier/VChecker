package model;

public class RespMavenCentral {
	responseHeader responseHeader;
	response response;
	
	RespMavenCentral(){}

	public RespMavenCentral(model.responseHeader responseHeader,
			model.response response) {
		super();
		this.responseHeader = responseHeader;
		this.response = response;
	}

	public responseHeader getResponseHeader() {
		return responseHeader;
	}

	public void setResponseHeader(responseHeader responseHeader) {
		this.responseHeader = responseHeader;
	}

	public response getResponse() {
		return response;
	}

	public void setResponse(response response) {
		this.response = response;
	}
	
	
}
