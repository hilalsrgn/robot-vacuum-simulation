package model;

public class Position
{
    private int row;
    private int col;

    public Position(int row, int col)
    {
        this.row = row;
        this.col = col;
    }

    public int getRow()
    {
        return row;
    }

    public void setRow(int row)
    {
        this.row = row;
    }

    public int getCol()
    {
        return col;
    }

    public void setCol(int col)
    {
        this.col = col;
    }
    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;

        if(obj == null || getClass() != obj.getClass())
            return false;

        Position other = (Position) obj;

        return row == other.row &&
                col == other.col;
    }

    @Override
    public int hashCode()
    {
        return 31 * row + col;
    }
}