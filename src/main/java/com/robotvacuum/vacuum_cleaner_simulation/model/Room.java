package com.robotvacuum.vacuum_cleaner_simulation.model;

public class Room {

    private final int rows;
    private final int cols;

    private Cell[][] grid;

    public Room(int rows, int cols) {

        this.rows = rows;
        this.cols = cols;

        grid = new Cell[rows][cols];

        for(int r = 0; r < rows; r++) {
            for(int c = 0; c < cols; c++) {
                grid[r][c] = new Cell();
            }
        }
    }

    public Cell getCell(int row,int col) {
        return grid[row][col];
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}