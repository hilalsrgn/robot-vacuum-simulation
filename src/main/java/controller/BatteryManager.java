package controller;

import model.Robot;

public class BatteryManager
{
    private static final double LOW_BATTERY_THRESHOLD = 20.0;

    public boolean isBatteryLow(Robot robot)
    {
        return robot.getBatteryLevel()
                <= LOW_BATTERY_THRESHOLD;
    }

    public void chargeBattery(Robot robot)
    {
        robot.chargeBattery();
    }

    public double getBatteryPercentage(Robot robot)
    {
        return robot.getBatteryLevel();
    }
}