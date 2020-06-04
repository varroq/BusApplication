package com.example.simon.busprototyp.service;


public class BusVehicle {

	private int nbr;
	private String kennzeichen;	
	private int sitzplaetze;	
	private String beschreibung;
	private String marker;
	private String publicinfo;
	private String privatinfo;
	private int linie;
	private int umlaufplanpos;
	
	public BusVehicle(int nbr, String kennzeichen, int sitzplaetze, String beschreibung, String marker,
			String publicinfo, String privatinfo, int linie, int umlaufplanpos) {
		super();
		this.nbr = nbr;
		this.kennzeichen = kennzeichen;
		this.sitzplaetze = sitzplaetze;
		this.beschreibung = beschreibung;
		this.marker = marker;
		this.publicinfo = publicinfo;
		this.privatinfo = privatinfo;
		this.linie = linie;
		this.umlaufplanpos = umlaufplanpos;
	}
	
	public BusVehicle() {
		super();
	}

	public int getNbr() {
		return nbr;
	}

	public void setNbr(int nbr) {
		this.nbr = nbr;
	}

	public String getKennzeichen() {
		return kennzeichen;
	}

	public void setKennzeichen(String kennzeichen) {
		this.kennzeichen = kennzeichen;
	}

	public int getSitzplaetze() {
		return sitzplaetze;
	}

	public void setSitzplaetze(int sitzplaetze) {
		this.sitzplaetze = sitzplaetze;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public String getMarker() {
		return marker;
	}

	public void setMarker(String marker) {
		this.marker = marker;
	}

	public String getPublicinfo() {
		return publicinfo;
	}

	public void setPublicinfo(String publicinfo) {
		this.publicinfo = publicinfo;
	}

	public String getPrivatinfo() {
		return privatinfo;
	}

	public void setPrivatinfo(String privatinfo) {
		this.privatinfo = privatinfo;
	}

	public int getLinie() {
		return linie;
	}

	public void setLinie(int linie) {
		this.linie = linie;
	}

	public int getUmlaufplanpos() {
		return umlaufplanpos;
	}

	public void setUmlaufplanpos(int umlaufplanpos) {
		this.umlaufplanpos = umlaufplanpos;
	}
	
	

	

}
