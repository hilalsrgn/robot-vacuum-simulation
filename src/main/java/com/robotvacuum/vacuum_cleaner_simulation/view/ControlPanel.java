package com.robotvacuum.vacuum_cleaner_simulation.view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class ControlPanel extends VBox {

    public ControlPanel() {

        setSpacing(10);
        setPadding(new Insets(15));
        setStyle(
                "-fx-background-color: #102A43;" +
                        "-fx-pref-width: 220;"
        );

        Label dirtLabel =
                new Label("Kir Türü");

        dirtLabel.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 15;" +
                        "-fx-font-weight: bold;"
        );

        RadioButton dust =
                new RadioButton("Toz");

        RadioButton liquid =
                new RadioButton("Sıvı");

        RadioButton stain =
                new RadioButton("Leke");

        ToggleGroup dirtGroup =
                new ToggleGroup();

        dust.setToggleGroup(dirtGroup);
        liquid.setToggleGroup(dirtGroup);
        stain.setToggleGroup(dirtGroup);

        Button addDirtButton =
                new Button("Kir Ekle");
        addDirtButton.setStyle(
                "-fx-background-color: #1E90FF;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;"
        );

        Button addObstacleButton =
                new Button("Mobilya Ekle");
        addObstacleButton.setStyle(
                "-fx-background-color: #32CD32;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;"
        );

        Label speedLabel =
                new Label("Robot Hızı");

        Slider speedSlider =
                new Slider(1,5,1);

        Button startButton =
                new Button("Başlat");
        startButton.setStyle(
                "-fx-background-color: #28A745;" +
                        "-fx-text-fill: white;"
        );

        Button pauseButton =
                new Button("Durdur");
        pauseButton.setStyle(
                "-fx-background-color: #FFC107;" +
                        "-fx-text-fill: black;"
        );

        Button resetButton =
                new Button("Sıfırla");
        resetButton.setStyle(
                "-fx-background-color: #DC3545;" +
                        "-fx-text-fill: white;"
        );

        Button returnButton =
                new Button("İstasyona Dön");
        returnButton.setStyle(
                "-fx-background-color: #17A2B8;" +
                        "-fx-text-fill: white;"
        );

        Label batteryLabel =
                new Label("Batarya : %100");

        Label positionLabel =
                new Label("Konum : (0,0)");
        dust.setStyle("-fx-text-fill: white;");
        liquid.setStyle("-fx-text-fill: white;");
        stain.setStyle("-fx-text-fill: white;");
        batteryLabel.setStyle(
                "-fx-text-fill: white;"
        );

        positionLabel.setStyle(
                "-fx-text-fill: white;"
        );

        getChildren().addAll(

                dirtLabel,

                dust,
                liquid,
                stain,

                addDirtButton,
                addObstacleButton,

                speedLabel,
                speedSlider,

                startButton,
                pauseButton,
                resetButton,
                returnButton,

                batteryLabel,
                positionLabel
        );

    }
}