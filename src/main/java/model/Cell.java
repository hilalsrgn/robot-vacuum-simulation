package model;

public class Cell
{
    private Position position;

    private boolean obstacle;

    private boolean chargingStation;

    private Dirt dirt;

    public Cell(Position position)
    {
        this.position = position;
        this.obstacle = false;
        this.chargingStation = false;
        this.dirt = null;
    }

    public Position getPosition()
    {
        return position;
    }

    public boolean isObstacle()
    {
        return obstacle;
    }

    public void setObstacle(boolean obstacle)
    {
        this.obstacle = obstacle;
    }

    public boolean isChargingStation()
    {
        return chargingStation;
    }

    public void setChargingStation(boolean chargingStation)
    {
        this.chargingStation = chargingStation;
    }

    public Dirt getDirt()
    {
        return dirt;
    }

    public void setDirt(Dirt dirt)
    {
        this.dirt = dirt;
    }

    public boolean isDirty()
    {
        return dirt != null;
    }
}