package com.example.simon.busprototyp.service;

import java.sql.Time;


public class BusTimeTable {

	private int nbr;
	private int linie_id;
	private int haltestelle;
	private Time ankunft;
	private Time abfahrt;
	
	public BusTimeTable(int nbr, int linie_id, int haltestelle, Time ankunft, Time abfahrt) {
		super();
		this.nbr = nbr;
		this.linie_id = linie_id;
		this.haltestelle = haltestelle;
		this.ankunft = ankunft;
		this.abfahrt = abfahrt;
	}
	
	public BusTimeTable() {
		super();
	}

	public int getNbr() {
		return nbr;
	}

	public void setNbr(int nbr) {
		this.nbr = nbr;
	}

	public int getLinie_id() {
		return linie_id;
	}

	public void setLinie_id(int linie_id) {
		this.linie_id = linie_id;
	}

	public int getHaltestelle() {
		return haltestelle;
	}

	public void setHaltestelle(int haltestelle) {
		this.haltestelle = haltestelle;
	}

	public Time getAnkunft() {
		return ankunft;
	}

	public void setAnkunft(Time ankunft) {
		this.ankunft = ankunft;
	}

	public Time getAbfahrt() {
		return abfahrt;
	}

	public void setAbfahrt(Time abfahrt) {
		this.abfahrt = abfahrt;
	}
	
	
	
}
