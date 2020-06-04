package com.example.simon.busprototyp.service;

import java.util.List;

public class BusSettings {

    private List<BusVehicle> vehicles;
    private List<BusLine> timetables;

    public BusSettings(List<BusVehicle> vehicles, List<BusLine> timetables) {
        super();
        this.vehicles = vehicles;
        this.timetables = timetables;
    }

    public List<BusVehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<BusVehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public List<BusLine> getTimetables() {
        return timetables;
    }

    public void setTimetables(List<BusLine> timetables) {
        this.timetables = timetables;
    }


}
