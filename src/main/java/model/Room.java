package model;

public class Room
{
    private int rows;
    private int cols;

    private Cell[][] cells;

    private Robot robot;

    private ChargingStation chargingStation;

    public Room(int rows, int cols)
    {
        this.rows = rows;
        this.cols = cols;

        cells = new Cell[rows][cols];

        initializeCells();
    }

    private void initializeCells()
    {
        for(int i = 0; i < rows; i++)
        {
            for(int j = 0; j < cols; j++)
            {
                cells[i][j] = new Cell(new Position(i, j));
            }
        }
    }

    public Cell getCell(int row, int col)
    {
        return cells[row][col];
    }

    public int getRows()
    {
        return rows;
    }

    public int getCols()
    {
        return cols;
    }

    public Robot getRobot()
    {
        return robot;
    }

    public void setRobot(Robot robot)
    {
        this.robot = robot;
    }

    public ChargingStation getChargingStation()
    {
        return chargingStation;
    }

    public void setChargingStation(ChargingStation chargingStation)
    {
        this.chargingStation = chargingStation;
    }
}