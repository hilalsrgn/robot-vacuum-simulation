package com.robotvacuum.vacuum_cleaner_simulation.model;

public class Cell {

    private boolean obstacle;
    private boolean chargingStation;
    private Dirt dirt;

    public boolean isObstacle() {
        return obstacle;
    }

    public void setObstacle(boolean obstacle) {
        this.obstacle = obstacle;
    }

    public boolean isChargingStation() {
        return chargingStation;
    }

    public void setChargingStation(boolean chargingStation) {
        this.chargingStation = chargingStation;
    }

    public Dirt getDirt() {
        return dirt;
    }

    public void setDirt(Dirt dirt) {
        this.dirt = dirt;
    }
}