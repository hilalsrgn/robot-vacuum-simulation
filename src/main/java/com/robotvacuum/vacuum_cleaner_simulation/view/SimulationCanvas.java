package com.robotvacuum.vacuum_cleaner_simulation.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

import java.util.Objects;

public class SimulationCanvas extends Canvas {

    private static final int GRID_SIZE = 30;
    private static final int ROWS = 20;
    private static final int COLS = 20;

    private final Image robotImage =
            new Image(
                    Objects.requireNonNull(
                            getClass().getResourceAsStream(
                                    "/vacuum-robot.png"
                            )
                    )
            );

    public SimulationCanvas() {

        super(COLS * GRID_SIZE, ROWS * GRID_SIZE);

        drawGrid();
        drawRobot(5,5);
    }

    public void drawGrid() {

        GraphicsContext gc = getGraphicsContext2D();

        gc.setFill(Color.BEIGE);
        gc.fillRect(0,0,getWidth(),getHeight());

        gc.setStroke(Color.GRAY);

        for(int row = 0; row <= ROWS; row++) {
            gc.strokeLine(
                    0,
                    row * GRID_SIZE,
                    COLS * GRID_SIZE,
                    row * GRID_SIZE
            );
        }

        for(int col = 0; col <= COLS; col++) {
            gc.strokeLine(
                    col * GRID_SIZE,
                    0,
                    col * GRID_SIZE,
                    ROWS * GRID_SIZE
            );
        }
    }

    public void drawRobot(int row, int col) {

        GraphicsContext gc = getGraphicsContext2D();

        gc.setFill(Color.rgb(0,0,0,0.2));

        gc.fillOval(
                col * GRID_SIZE + 3,
                row * GRID_SIZE + 3,
                34,
                34
        );
        gc.drawImage(
                robotImage,
                col * GRID_SIZE -5 ,
                row * GRID_SIZE -5 ,
                GRID_SIZE +10,
                GRID_SIZE +10
        );
    }
}