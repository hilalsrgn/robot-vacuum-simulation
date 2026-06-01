package com.robotvacuum.vacuum_cleaner_simulation.model;

public class Stain extends Dirt {

    public Stain() {
        cleaningTime = 5;
        batteryCost = 4;
    }

    @Override
    public DirtType getType() {
        return DirtType.STAIN;
    }
}