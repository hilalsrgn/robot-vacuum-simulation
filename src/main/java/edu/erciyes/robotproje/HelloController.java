package edu.erciyes.robotproje;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import model.Room;
import view.SimulationCanvas;
import model.Robot;
import model.Position;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.animation.AnimationTimer;



public class HelloController
{
    private boolean[][] visitedCells;

    @FXML private Button startButton; // Sol paneldeki "Başlat" butonunun fx:id'si
    private AnimationTimer simulationTimer;
    private Room room; // Kızların odasına buradan erişeceğiz
    private SimulationCanvas canvas; // Çizim tuvaline erişim
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
                new Robot(new Position(13,0));

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

        // SADECE KİR EKLEME VE GÖRSELLEŞTİRME KODU
        simulationPane.setOnMouseClicked(event -> {
            double mouseX = event.getX();
            double mouseY = event.getY();

            if (currentInteractionMode.equals("DIRT")) {
                RadioButton selectedDirt = (RadioButton) dirtTypeGroup.getSelectedToggle();
                if (selectedDirt != null) {

                    // Görsel bir yuvarlak oluşturuyoruz (İleride kızlarla bunu .png resimle değiştirebilirsiniz)
                    Circle dirtVisual = new Circle(mouseX, mouseY, 8);

                    String dirtType = selectedDirt.getText();
                    if (dirtType.equals("Toz")) {
                        dirtVisual.setFill(Color.GRAY); // İleride: new ImagePattern(new Image("toz.png"))
                    } else if (dirtType.equals("Sıvı")) {
                        dirtVisual.setFill(Color.BLUE); // İleride: new ImagePattern(new Image("sivi.png"))
                    } else if (dirtType.equals("Leke")) {
                        dirtVisual.setFill(Color.DARKRED); // İleride: new ImagePattern(new Image("leke.png"))
                    }

                    // Kiri ekrana (pane içine) ekle
                    simulationPane.getChildren().add(dirtVisual);

                    System.out.println("Haritaya kir basıldı: " + dirtType);
                }
            }
        });
        startButton.setOnAction(e -> {
            if (simulationTimer == null) {
                simulationTimer = new AnimationTimer() {
                    private long lastUpdate = 0;

                    @Override
                    public void handle(long now) {
                        // 1. Hız çubuğundan değeri al (1.0x, 2.0x vs.)
                        double speedFactor = speedSlider.getValue();

                        // Hıza göre adım atma süresini hesapla (Normali saniyede 1 adım)
                        long interval = (long) (1_000_000_000 / speedFactor);

                        if (now - lastUpdate >= interval) {

                            // Güvenlik kontrolü: Robot veya oda yoksa patlamasın
                            if (room != null && room.getRobot() != null) {
                                Robot robot = room.getRobot();
                                Position currentPos = robot.getPosition();
                                // HAFIZA KARTI KONTROLÜ: Eğer hafıza kartı boşsa, odanın boyutuna göre oluştur
                                if (visitedCells == null) {
                                    visitedCells = new boolean[room.getRows()][room.getCols()];
                                    // Robotun şu an durduğu yeri "temizlendi" olarak işaretle
                                    visitedCells[currentPos.getRow()][currentPos.getCol()] = true;
                                }

                                // 2. SEÇİLEN ALGORİTMAYA GÖRE HAREKET ET
                                RadioButton selectedAlgo = (RadioButton) algorithmGroup.getSelectedToggle();

                                if (selectedAlgo != null) {
                                    String algoName = selectedAlgo.getText();

                                    // ==========================================
                                    // GERÇEK SPİRAL ALGORİTMASI (Dıştan İçe Daralan)
                                    // ==========================================
                                    if (algoName.equals("Spiral")) {
                                        int currentRow = currentPos.getRow();
                                        int currentCol = currentPos.getCol();

                                        int nextRow = currentRow;
                                        int nextCol = currentCol;

                                        // 1. Hedef hücreyi hesapla
                                        if (robot.getDirection() == model.Direction.RIGHT) nextCol++;
                                        else if (robot.getDirection() == model.Direction.DOWN) nextRow++;
                                        else if (robot.getDirection() == model.Direction.LEFT) nextCol--;
                                        else if (robot.getDirection() == model.Direction.UP) nextRow--;

                                        // 2. Çarpışma Kontrolü: Duvara Mı Çarptı? VEYA Daha önce temizlediği yere mi geldi?
                                        boolean hitWall = nextRow < 0 || nextRow >= room.getRows() || nextCol < 0 || nextCol >= room.getCols();
                                        boolean alreadyCleaned = !hitWall && visitedCells[nextRow][nextCol];

                                        if (!hitWall && !alreadyCleaned) {
                                            // Önü boş ve kirliyse ilerle, orayı hafızaya temizlendi olarak yaz
                                            robot.move(new Position(nextRow, nextCol));
                                            visitedCells[nextRow][nextCol] = true;
                                        } else {
                                            // Duvara VEYA temizlenmiş bölgeye çarptığı an içe doğru (saat yönünde) dön!
                                            if (robot.getDirection() == model.Direction.RIGHT) robot.setDirection(model.Direction.DOWN);
                                            else if (robot.getDirection() == model.Direction.DOWN) robot.setDirection(model.Direction.LEFT);
                                            else if (robot.getDirection() == model.Direction.LEFT) robot.setDirection(model.Direction.UP);
                                            else if (robot.getDirection() == model.Direction.UP) robot.setDirection(model.Direction.RIGHT);

                                            System.out.println("Spiral Daralıyor: Dönüş yapıldı!");
                                        }
                                    }

                                    // ==========================================
                                    // DUVAR TAKİP ALGORİTMASI (Edge Follow)
                                    // ==========================================
                                    else if (algoName.equals("Duvar Takip")) {
                                        int currentRow = currentPos.getRow();
                                        int currentCol = currentPos.getCol();

                                        // Temel mantık: Robot sürekli dış çeperdeki (0. satır/sütun veya son satır/sütun) duvarı takip eder
                                        int nextRow = currentRow;
                                        int nextCol = currentCol;

                                        if (robot.getDirection() == model.Direction.RIGHT) nextCol++;
                                        else if (robot.getDirection() == model.Direction.DOWN) nextRow++;
                                        else if (robot.getDirection() == model.Direction.LEFT) nextCol--;
                                        else if (robot.getDirection() == model.Direction.UP) nextRow--;

                                        // Önü özgürse ve hala duvar kenarındaysa ilerle
                                        if (nextRow >= 0 && nextRow < room.getRows() && nextCol >= 0 && nextCol < room.getCols()) {
                                            robot.move(new Position(nextRow, nextCol));
                                        } else {
                                            // Köşeye geldiği an yön değiştirip odayı çevrelesin
                                            if (robot.getDirection() == model.Direction.RIGHT) robot.setDirection(model.Direction.DOWN);
                                            else if (robot.getDirection() == model.Direction.DOWN) robot.setDirection(model.Direction.LEFT);
                                            else if (robot.getDirection() == model.Direction.LEFT) robot.setDirection(model.Direction.UP);
                                            else if (robot.getDirection() == model.Direction.UP) robot.setDirection(model.Direction.RIGHT);

                                            System.out.println("Duvar Takip: Köşeye gelindi, dönülüyor...");
                                        }
                                    }

                                    // RASTGELE MODU (Senin stratejin doğrultusunda pas geçildi)
                                    else if (algoName.equals("Rastgele")) {
                                        System.out.println("Rastgele modu mobilyalardan sonra kodlanacak.");
                                    }
                                }

                                // 3. EKRANI GÜNCELLE (Kızların draw metodu çalışsın)
                                if (canvas != null) {
                                    canvas.draw();
                                }

                                // 4. KİRLERİ SİLME EFEKTİ (Senin eklediğin yuvarlaklar)
                                // Robotun yeni piksel koordinatlarını hesaplıyoruz
                                double cellWidth = simulationPane.getWidth() / room.getCols();
                                double cellHeight = simulationPane.getHeight() / room.getRows();
                                double robotVisualX = robot.getPosition().getCol() * cellWidth + (cellWidth/2);
                                double robotVisualY = robot.getPosition().getRow() * cellHeight + (cellHeight/2);

                                // Üzerine bastığı kiri ekrandan uçur
                                simulationPane.getChildren().removeIf(node -> {
                                    if (node instanceof javafx.scene.shape.Circle) {
                                        javafx.scene.shape.Circle dirt = (javafx.scene.shape.Circle) node;
                                        double distance = Math.hypot(dirt.getCenterX() - robotVisualX,
                                                dirt.getCenterY() - robotVisualY);
                                        return distance < cellWidth; // Aynı hücredeyse sil!
                                    }
                                    return false;
                                });
                            }

                            lastUpdate = now;
                        }
                    }
                };
                simulationTimer.start();
                System.out.println("MOTOR ÇALIŞTI! Robot hareket ediyor...");
            }
        });
    }
}

