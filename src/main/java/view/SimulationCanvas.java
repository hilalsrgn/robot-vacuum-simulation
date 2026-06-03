package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import model.Cell;
import model.DirtType;
import model.Room;
import model.Robot;
import model.Obstacle;


public class SimulationCanvas extends Canvas
{
    private Room room;

    public SimulationCanvas(Room room)
    {
        this.room = room;

        widthProperty().addListener(evt -> draw());
        heightProperty().addListener(evt -> draw());
    }
    private final Image dustImage =
            new Image(
                    getClass().getResourceAsStream(
                            "/images/dust.png"
                    )
            );

    private final Image liquidImage =
            new Image(
                    getClass().getResourceAsStream(
                            "/images/splash.png"
                    )
            );

    private final Image stainImage =
            new Image(
                    getClass().getResourceAsStream(
                            "/images/spots.png"
                    )
            );

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
                Cell cell = room.getCell(row,col);

                if(cell.isDirty())
                {
                    drawDirt(
                            gc,
                            cell,
                            col * cellWidth,
                            row * cellHeight,
                            cellWidth,
                            cellHeight
                    );
                }
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

        for(Obstacle obstacle : room.getObstacles())
        {
            Image obstacleImage =
                    new Image(
                            getClass().getResourceAsStream(
                                    obstacle.getImagePath()
                            )
                    );

            gc.drawImage(
                    obstacleImage,

                    obstacle.getPosition().getCol()
                            * cellWidth,

                    obstacle.getPosition().getRow()
                            * cellHeight,

                    obstacle.getWidth()
                            * cellWidth,

                    obstacle.getHeight()
                            * cellHeight
            );
        }

        // Robot
        if(room.getRobot() != null)
        {
            Robot robot = room.getRobot();

            double size = Math.min(cellWidth, cellHeight) * 1.5;

            double x =
                    robot.getPosition().getCol() * cellWidth
                            - (size - cellWidth) / 2;

            double y =
                    robot.getPosition().getRow() * cellHeight
                            - (size - cellHeight) / 2;

            // --- SİHİRLİ DÖNÜŞ KODU BAŞLANGICI ---
            double centerX = x + size / 2;
            double centerY = y + size / 2;

            double angle = 0;

            // Kızların çizimi varsayılan olarak YUKARI bakıyor. Doğru açılar:
            if (robot.getDirection() == model.Direction.RIGHT) angle = 90;
            else if (robot.getDirection() == model.Direction.DOWN) angle = 180;
            else if (robot.getDirection() == model.Direction.LEFT) angle = 270;
            else if (robot.getDirection() == model.Direction.UP) angle = 0;

            gc.save();
            gc.translate(centerX, centerY);
            gc.rotate(angle);
            gc.translate(-centerX, -centerY);
            // --- SİHİRLİ DÖNÜŞ KODU BİTİŞİ ---

            // Gölge
            gc.setFill(javafx.scene.paint.Color.rgb(0,0,0,0.25));
            gc.fillOval(
                    x + size*0.08,
                    y + size*0.08,
                    size,
                    size
            );

            // Dış siyah halka
            gc.setFill(javafx.scene.paint.Color.web("#1A1A1A"));
            gc.fillOval(
                    x,
                    y,
                    size,
                    size
            );

            // Beyaz gövde
            gc.setFill(javafx.scene.paint.Color.web("#F8F8F8"));
            gc.fillOval(
                    x + size*0.05,
                    y + size*0.05,
                    size*0.90,
                    size*0.90
            );

            // Metal çerçeve
            gc.setStroke(javafx.scene.paint.Color.web("#B0B0B0"));
            gc.setLineWidth(2);
            gc.strokeOval(
                    x + size*0.05,
                    y + size*0.05,
                    size*0.90,
                    size*0.90
            );

            // Lidar kulesi
            gc.setFill(javafx.scene.paint.Color.web("#2B2B2B"));
            gc.fillOval(
                    x + size*0.36,
                    y + size*0.10,
                    size*0.28,
                    size*0.28
            );

            // Lidar iç halka
            gc.setFill(javafx.scene.paint.Color.web("#555555"));
            gc.fillOval(
                    x + size*0.40,
                    y + size*0.14,
                    size*0.20,
                    size*0.20
            );

            // Kamera sensörü
            gc.setFill(javafx.scene.paint.Color.BLACK);
            gc.fillOval(
                    x + size*0.47,
                    y + size*0.21,
                    size*0.06,
                    size*0.06
            );

            // Üst kapak çizgisi
            gc.setStroke(javafx.scene.paint.Color.web("#C0C0C0"));
            gc.setLineWidth(1);

            gc.strokeRoundRect(
                    x + size*0.18,
                    y + size*0.22,
                    size*0.64,
                    size*0.45,
                    10,
                    10
            );

            // Güç tuşu
            gc.setFill(javafx.scene.paint.Color.web("#EAEAEA"));
            gc.fillOval(
                    x + size*0.43,
                    y + size*0.72,
                    size*0.14,
                    size*0.14
            );

            gc.setStroke(javafx.scene.paint.Color.web("#999999"));
            gc.strokeOval(
                    x + size*0.43,
                    y + size*0.72,
                    size*0.14,
                    size*0.14
            );

            // Ön sensör penceresi
            gc.setFill(javafx.scene.paint.Color.BLACK);

            gc.fillRoundRect(
                    x + size*0.42,
                    y + size*0.92,
                    size*0.16,
                    size*0.03,
                    4,
                    4
            );
            gc.restore();
        }

        }private void drawSofa(
        GraphicsContext gc,
        double x,
        double y,
        double width,
        double height)
{
    gc.setFill(javafx.scene.paint.Color.web("#6D4C41"));

    gc.fillRoundRect(
            x,
            y,
            width,
            height,
            20,
            20
    );

    gc.setFill(javafx.scene.paint.Color.web("#A1887F"));

    gc.fillRoundRect(
            x + 10,
            y + 10,
            width - 20,
            height - 20,
            15,
            15
    );
}

    private void drawDirt(
            GraphicsContext gc,
            Cell cell,
            double x,
            double y,
            double width,
            double height)
    {
        Image dirtImage = null;

        if(cell.getDirt().getType() ==
                DirtType.DUST)
        {
            dirtImage = dustImage;
        }
        else if(cell.getDirt().getType() ==
                DirtType.LIQUID)
        {
            dirtImage = liquidImage;
        }
        else if(cell.getDirt().getType() ==
                DirtType.STAIN)
        {
            dirtImage = stainImage;
        }

        gc.drawImage(
                dirtImage,
                x,
                y,
                width,
                height
        );
    }

    }
