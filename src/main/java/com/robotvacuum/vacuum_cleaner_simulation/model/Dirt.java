package com.robotvacuum.vacuum_cleaner_simulation.model;

public abstract class Dirt {

    protected int cleaningTime;
    protected int batteryCost;

    public int getCleaningTime() {
        return cleaningTime;
    }

    public int getBatteryCost() {
        return batteryCost;
    }

    public abstract DirtType getType();
}