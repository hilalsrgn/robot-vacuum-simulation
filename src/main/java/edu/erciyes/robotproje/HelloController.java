package edu.erciyes.robotproje;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import model.Room;
import view.SimulationCanvas;
import model.Robot;
import model.Position;

public class HelloController
{
    @FXML private Button addDirtButton;
    @FXML private Button addObstacleButton;
    @FXML private RadioButton dustRadioButton;
    @FXML private RadioButton liquidRadioButton;
    @FXML private RadioButton stainRadioButton;

    // Şu an hangi aracın (kir mi mobilya mı) seçili olduğunu akılda tutmak için bir değişken
    private String currentInteractionMode = "NONE"; // NONE, DIRT, OBSTACLE
    @FXML
    private Slider speedSlider;

    @FXML
    private Label speedLabel;

    @FXML
    private RadioButton randomRadioButton;

    @FXML
    private RadioButton spiralRadioButton;

    @FXML
    private RadioButton wallFollowRadioButton;

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

        // Geçici test verileri
        positionLabel.setText("Konum: (0,0)");
        directionLabel.setText("Yön: Sağ");
        batteryBar.setProgress(1.0);

        // Hız çubuğunun değeri her değiştiğinde burası tetiklenir
        speedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Çubuğun o anki sayısal değerini alıyoruz
            double speedValue = newValue.doubleValue();

            // Değeri "1.0x" formatında Label'a yazdırıyoruz
            speedLabel.setText(String.format("%.1fx", speedValue));
        });
        // 1. Bir grup oluşturuyoruz
        ToggleGroup algorithmGroup = new ToggleGroup();

        // 2. Butonları bu gruba üye yapıyoruz
        randomRadioButton.setToggleGroup(algorithmGroup);
        spiralRadioButton.setToggleGroup(algorithmGroup);
        wallFollowRadioButton.setToggleGroup(algorithmGroup);

        // 3. Varsayılan olarak ilk başta "Rastgele" seçili gelsin
        randomRadioButton.setSelected(true);

        // 4. Hangisi seçildiyse anlık olarak yakalayan dinleyici
        algorithmGroup.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if (newToggle != null) {
                RadioButton selectedButton = (RadioButton) newToggle;
                System.out.println("Seçilen Algoritma: " + selectedButton.getText());
            }
        });
        // Kir Türleri için ToggleGroup kurulumu (Eğer SceneBuilder'dan yapmadıysan kodla bağlıyoruz)
        ToggleGroup dirtTypeGroup = new ToggleGroup();
        dustRadioButton.setToggleGroup(dirtTypeGroup);
        liquidRadioButton.setToggleGroup(dirtTypeGroup);
        stainRadioButton.setToggleGroup(dirtTypeGroup);
        dustRadioButton.setSelected(true); // Varsayılan toz seçili gelsin

        // "Kir Ekle" butonuna basıldığında
        addDirtButton.setOnAction(e -> {
            currentInteractionMode = "DIRT";
            System.out.println("Mod Değişti: Haritaya kir ekleyebilirsiniz. Seçili Tür: " +
                    ((RadioButton) dirtTypeGroup.getSelectedToggle()).getText());
        });

        // "Mobilya Ekle" butonuna basıldığında
        addObstacleButton.setOnAction(e -> {
            currentInteractionMode = "OBSTACLE";
            System.out.println("Mod Değişti: Haritaya mobilya/engel ekleyebilirsiniz.");
        });

    }
}
