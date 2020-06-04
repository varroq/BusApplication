package com.example.simon.busprototyp.service;

import java.io.Serializable;
import java.sql.Time;

public class BusStop implements Serializable {

	private int nbr;	
	private String name;	
	private String ort;	
	private String strasse;		
	private int hausnummer;		
	private double breitengrad;
	private double laengengrad;
	
	public BusStop(int nbr, String name, String ort, String strasse, int hausnummer, double breitengrad,
			double laengengrad) {
		super();
		this.nbr = nbr;
		this.name = name;
		this.ort = ort;
		this.strasse = strasse;
		this.hausnummer = hausnummer;
		this.breitengrad = breitengrad;
		this.laengengrad = laengengrad;
	}
	
	public BusStop() {
		super();
	}

	public int getNbr() {
		return nbr;
	}

	public void setNbr(int nbr) {
		this.nbr = nbr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrt() {
		return ort;
	}

	public void setOrt(String ort) {
		this.ort = ort;
	}

	public String getStrasse() {
		return strasse;
	}

	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}

	public int getHausnummer() {
		return hausnummer;
	}

	public void setHausnummer(int hausnummer) {
		this.hausnummer = hausnummer;
	}

	public double getBreitengrad() {
		return breitengrad;
	}

	public void setBreitengrad(double breitengrad) {
		this.breitengrad = breitengrad;
	}

	public double getLaengengrad() {
		return laengengrad;
	}

	public void setLaengengrad(double laengengrad) {
		this.laengengrad = laengengrad;
	}
	
	

}
