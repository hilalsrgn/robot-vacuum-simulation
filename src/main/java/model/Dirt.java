package model;

public class Dirt
{
    private Position position;
    private DirtType type;

    public Dirt(Position position, DirtType type)
    {
        this.position = position;
        this.type = type;
    }

    public Position getPosition()
    {
        return position;
    }

    public DirtType getType()
    {
        return type;
    }
}