package com.robotvacuum.vacuum_cleaner_simulation.model;

public class Dust extends Dirt {

    public Dust() {
        cleaningTime = 1;
        batteryCost = 1;
    }

    @Override
    public DirtType getType() {
        return DirtType.DUST;
    }
}