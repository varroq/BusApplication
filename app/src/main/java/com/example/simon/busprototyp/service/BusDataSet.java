package com.example.simon.busprototyp.service;

public class BusDataSet {

    private int datensatz;
    private String zeitstempel;
    private double laengengrad;
    private double breitengrad;
    private int fahrzeugid;
    private int fahrgaeste;
    private float geschwindigkeit;
    private String status;
    private int linienId;
    private int naechsterHalt;
    private double distanz;


    public BusDataSet(int datensatz, String zeitstempel, double laengengrad, double breitengrad, int fahrzeugid,
                      int fahrgaeste, float geschwindigkeit, String status, int linienId, int naechsterHalt, double distanz) {
        super();
        this.datensatz = datensatz;
        this.zeitstempel = zeitstempel;
        this.laengengrad = laengengrad;
        this.breitengrad = breitengrad;
        this.fahrzeugid = fahrzeugid;
        this.fahrgaeste = fahrgaeste;
        this.geschwindigkeit = geschwindigkeit;
        this.status = status;
        this.linienId = linienId;
        this.naechsterHalt = naechsterHalt;
        this.distanz = distanz;
    }

    public BusDataSet() {
        super();
    }

    public BusDataSet(String zeitstempel, double laengengrad, double breitengrad, int fahrzeugid, int fahrgaeste,
                      float geschwindigkeit, String status, int linienId, int naechsterHalt, double distanz) {
        super();
        this.zeitstempel = zeitstempel;
        this.laengengrad = laengengrad;
        this.breitengrad = breitengrad;
        this.fahrzeugid = fahrzeugid;
        this.fahrgaeste = fahrgaeste;
        this.geschwindigkeit = geschwindigkeit;
        this.status = status;
        this.linienId = linienId;
        this.naechsterHalt = naechsterHalt;
        this.distanz = distanz;
    }

    public int getDatensatz() {
        return datensatz;
    }

    public void setDatensatz(int datensatz) {
        this.datensatz = datensatz;
    }

    public String getZeitstempel() {
        return zeitstempel;
    }

    public void setZeitstempel(String zeitstempel) {
        this.zeitstempel = zeitstempel;
    }

    public double getLaengengrad() {
        return laengengrad;
    }

    public void setLaengengrad(double laengengrad) {
        this.laengengrad = laengengrad;
    }

    public double getBreitengrad() {
        return breitengrad;
    }

    public void setBreitengrad(double breitengrad) {
        this.breitengrad = breitengrad;
    }

    public int getFahrzeugid() {
        return fahrzeugid;
    }

    public void setFahrzeugid(int fahrzeugid) {
        this.fahrzeugid = fahrzeugid;
    }

    public int getFahrgaeste() {
        return fahrgaeste;
    }

    public void setFahrgaeste(int fahrgaeste) {
        this.fahrgaeste = fahrgaeste;
    }

    public float getGeschwindigkeit() {
        return geschwindigkeit;
    }

    public void setGeschwindigkeit(float geschwindigkeit) {
        this.geschwindigkeit = geschwindigkeit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getLinienId() {
        return linienId;
    }

    public void setLinienId(int linienId) {
        this.linienId = linienId;
    }

    public int getNaechsterHalt() {
        return naechsterHalt;
    }

    public void setNaechsterHalt(int naechsterHalt) {
        this.naechsterHalt = naechsterHalt;
    }

    public double getDistanz() {
        return distanz;
    }

    public void setDistanz(double distanz) {
        this.distanz = distanz;
    }



}

