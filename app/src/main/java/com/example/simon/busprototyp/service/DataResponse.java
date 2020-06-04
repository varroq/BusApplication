package com.example.simon.busprototyp.service;

public class DataResponse {
	
	private String privateText;
	private String publicText;
	
	public DataResponse(String privateText, String publicText) {
		super();
		this.privateText = privateText;
		this.publicText = publicText;
	}

	public String getPrivateText() {
		return privateText;
	}

	public void setPrivateText(String privateText) {
		this.privateText = privateText;
	}

	public String getPublicText() {
		return publicText;
	}

	public void setPublicText(String publicText) {
		this.publicText = publicText;
	}
	
	

}
