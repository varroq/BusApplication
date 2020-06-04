package com.example.simon.busprototyp.service;


public class BusLine {

	private int linie_id;
	private String linie;
	private String beschreibung;
	
	public BusLine(int linie_id, String linie, String beschreibung) {
		super();
		this.linie_id = linie_id;
		this.linie = linie;
		this.beschreibung = beschreibung;
	}
	
	public BusLine() {
		super();
	}

	public int getLinie_id() {
		return linie_id;
	}

	public void setLinie_id(int linie_id) {
		this.linie_id = linie_id;
	}

	public String getLinie() {
		return linie;
	}

	public void setLinie(String linie) {
		this.linie = linie;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}
	

}
