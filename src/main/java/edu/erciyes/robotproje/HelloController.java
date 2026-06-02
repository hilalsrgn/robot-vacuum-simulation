package edu.erciyes.robotproje;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import model.Room;
import view.SimulationCanvas;
import model.Robot;
import model.Position;

public class HelloController
{
    @FXML
    private AnchorPane simulationPane;

    @FXML
    private Label positionLabel;

    @FXML
    private Label directionLabel;

    @FXML
    private ProgressBar batteryBar;
    @FXML
    private Label totalAreaLabel;

    @FXML
    private Label cleanedAreaLabel;

    @FXML
    private Label remainingAreaLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private Label dustLabel;
    @FXML
    public void initialize()
    {
        Room room = new Room(14, 20);

        Robot robot =
                new Robot(new Position(5,5));

        room.setRobot(robot);

        SimulationCanvas canvas =
                new SimulationCanvas(room);

        canvas.widthProperty().bind(
                simulationPane.widthProperty());

        canvas.heightProperty().bind(
                simulationPane.heightProperty());

        simulationPane.getChildren().add(canvas);

        positionLabel.setText("Konum: (0,0)");
        directionLabel.setText("Yön: Sağ");
        batteryBar.setProgress(1.0);
    } }