package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import model.Room;

public class SimulationCanvas extends Canvas
{
    private Room room;

    public SimulationCanvas(Room room)
    {
        this.room = room;

        widthProperty().addListener(evt -> draw());
        heightProperty().addListener(evt -> draw());
    }

    @Override
    public boolean isResizable()
    {
        return true;
    }

    @Override
    public double prefWidth(double height)
    {
        return getWidth();
    }

    @Override
    public double prefHeight(double width)
    {
        return getHeight();
    }

    public void draw()
    {
        GraphicsContext gc = getGraphicsContext2D();

        gc.clearRect(0,0,getWidth(),getHeight());

        double cellWidth = getWidth() / room.getCols();
        double cellHeight = getHeight() / room.getRows();

        for(int row=0; row<room.getRows(); row++)
        {
            for(int col=0; col<room.getCols(); col++)
            {
                gc.strokeRect(
                        col * cellWidth,
                        row * cellHeight,
                        cellWidth,
                        cellHeight
                );
            }
        }
        gc.setFont(new javafx.scene.text.Font(12));

        for(int col = 0; col < room.getCols(); col++)
        {
            gc.fillText(
                    String.valueOf(col),
                    col * cellWidth + cellWidth / 2,
                    15
            );
        }

        for(int row = 0; row < room.getRows(); row++)
        {
            gc.fillText(
                    String.valueOf(row),
                    5,
                    row * cellHeight + cellHeight / 2
            );
        }
        // Robot

        gc.setFill(javafx.scene.paint.Color.DODGERBLUE);

        gc.fillOval(
                5 * cellWidth,
                5 * cellHeight,
                cellWidth,
                cellHeight
        );
    }
}