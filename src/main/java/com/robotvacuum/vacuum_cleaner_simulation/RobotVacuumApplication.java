package com.robotvacuum.vacuum_cleaner_simulation;

import com.robotvacuum.vacuum_cleaner_simulation.view.ControlPanel;
import com.robotvacuum.vacuum_cleaner_simulation.view.SimulationCanvas;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class RobotVacuumApplication extends Application {

    @Override
    public void start(Stage stage) {

        BorderPane root = new BorderPane();

        ControlPanel controlPanel =
                new ControlPanel();

        SimulationCanvas canvas =
                new SimulationCanvas();

        root.setLeft(controlPanel);
        root.setCenter(canvas);

        Scene scene =
                new Scene(root, 900, 650);

        stage.setTitle(
                "Robot Vacuum Cleaning Simulation"
        );

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}