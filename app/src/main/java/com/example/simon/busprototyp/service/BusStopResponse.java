package com.example.simon.busprototyp.service;

import java.io.Serializable;

public class BusStopResponse implements Serializable {
	private BusStop busStop;
	private String ankunft;
	private String abfahrt;
	
	public BusStopResponse(BusStop busStop, String ankunft, String abfahrt) {
		super();
		this.busStop = busStop;
		this.ankunft = ankunft;
		this.abfahrt = abfahrt;
	}

	public BusStop getBusStop() {
		return busStop;
	}

	public void setBusStop(BusStop busStop) {
		this.busStop = busStop;
	}

	public String getAnkunft() {
		return ankunft;
	}

	public void setAnkunft(String ankunft) {
		this.ankunft = ankunft;
	}

	public String getAbfahrt() {
		return abfahrt;
	}

	public void setAbfahrt(String abfahrt) {
		this.abfahrt = abfahrt;
	}

	
	
}
