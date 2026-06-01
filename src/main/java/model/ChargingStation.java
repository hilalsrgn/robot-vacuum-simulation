package model;

public class ChargingStation
{
    private Position position;

    public ChargingStation(Position position)
    {
        this.position = position;
    }

    public Position getPosition()
    {
        return position;
    }
}
