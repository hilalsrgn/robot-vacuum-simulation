package model;

public class Robot {

    private Position position;
    private Direction direction;

    private double batteryLevel;
    private double speed;

    private int cleanedCells;

    public Robot(Position position) {
        this.position = position;
        this.direction = Direction.RIGHT;

        this.batteryLevel = 100.0;
        this.speed = 1.0;

        this.cleanedCells = 0;
    }

    // GETTER - SETTER

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public double getBatteryLevel() {
        return batteryLevel;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getCleanedCells() {
        return cleanedCells;
    }

    // ROBOT DAVRANIŞLARI

    public void decreaseBattery(double amount) {
        batteryLevel -= amount;

        if (batteryLevel < 0) {
            batteryLevel = 0;
        }
    }

    public void chargeBattery() {
        batteryLevel = 100;
    }

    public boolean isBatteryLow() {
        return batteryLevel <= 20;
    }

    public void increaseCleanedCells() {
        cleanedCells++;
    }
}