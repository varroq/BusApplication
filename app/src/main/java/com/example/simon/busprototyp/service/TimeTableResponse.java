package com.example.simon.busprototyp.service;

import java.io.Serializable;
import java.util.List;

public class TimeTableResponse implements Serializable {

	private List<BusStopResponse> stops;
	
	private int tableId;

	private String name;

	public TimeTableResponse(List<BusStopResponse> stops, int tableId) {
		super();
		this.stops = stops;
		this.tableId = tableId;
	}

	public List<BusStopResponse> getStops() {
		return stops;
	}

	public void setStops(List<BusStopResponse> stops) {
		this.stops = stops;
	}

	public int getTableId() {
		return tableId;
	}

	public void setTableId(int tableId) {
		this.tableId = tableId;
	}
	
	
}
