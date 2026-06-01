package com.robotvacuum.vacuum_cleaner_simulation.model;

public class Liquid extends Dirt {

    public Liquid() {
        cleaningTime = 3;
        batteryCost = 2;
    }

    @Override
    public DirtType getType() {
        return DirtType.LIQUID;
    }
}