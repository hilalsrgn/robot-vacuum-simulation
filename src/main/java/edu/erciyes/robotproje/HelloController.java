package edu.erciyes.robotproje;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import model.*;
import model.Cell;
import view.SimulationCanvas;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.animation.AnimationTimer;



public class HelloController
{

    // Bunu en tepeye, diğer @FXML tanımlarının yanına ekle
    private Position startPosition = new Position(13, 0);
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

    private int elapsedSeconds = 0;
    private long lastSecondUpdate = 0;
    @FXML
    public void initialize()
    {
        room = new Room(14, 20);

        // Odanın gerçek hücre sayısını (toplam alanı) hesapla
        int totalArea = room.getRows() * room.getCols();

// Alt paneli simülasyon açılır açılmaz gerçek değerlerle doldur
        if (totalAreaLabel != null) {
            totalAreaLabel.setText(totalArea + " m²");
            remainingAreaLabel.setText(totalArea + " m² (100%)");
            cleanedAreaLabel.setText("0 m² (0%)");
        }

        Robot robot = new Robot(new Position(13,0));
        // Robot şarj istasyonundan çıkarken yüzü sağa (odanın içine) dönük olsun!
        robot.setDirection(model.Direction.RIGHT);
        room.setRobot(robot);

        room.setRobot(robot);

         canvas =
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
                    double cellWidth =
                            simulationPane.getWidth() / room.getCols();

                    double cellHeight =
                            simulationPane.getHeight() / room.getRows();

                    int col =
                            (int)(mouseX / cellWidth);

                    int row =
                            (int)(mouseY / cellHeight);

                    DirtType dirtType = DirtType.DUST;

                    if(selectedDirt == liquidRadioButton)
                    {
                        dirtType = DirtType.LIQUID;
                    }
                    else if(selectedDirt == stainRadioButton)
                    {
                        dirtType = DirtType.STAIN;
                    }

                    room.getCell(row,col)
                            .setDirt(
                                    new Dirt(
                                            new Position(row,col),
                                            dirtType
                                    )
                            );

                    canvas.draw();
                }
            }
        });
        startButton.setOnAction(e -> {
            if (simulationTimer == null) {
                simulationTimer = new AnimationTimer() {
                    private long lastUpdate = 0;

                    @Override
                    public void handle(long now) {

                        // Zamanlayıcı ilk kez veya duraklatıldıktan sonra çalışıyorsa referansı ayarla
                        if (lastSecondUpdate == 0) {
                            lastSecondUpdate = now;
                        }

                    // Tam 1 saniye (1.000.000.000 nanosaniye) geçtiyse sayacı artır
                        if (now - lastSecondUpdate >= 1_000_000_000L) {
                            elapsedSeconds++;
                            lastSecondUpdate = now;

                            // Saniyeyi 00:00 formatına çevir
                            int minutes = elapsedSeconds / 60;
                            int seconds = elapsedSeconds % 60;
                            String timeString = String.format("%02d:%02d", minutes, seconds);

                            // Ekrana bas
                            if (timeLabel != null) {
                                timeLabel.setText(timeString);
                            }
                        }

                        // 1. Hız çubuğundan değeri al (1.0x, 2.0x vs.)
                        double speedFactor = speedSlider.getValue();

                        // Hıza göre adım atma süresini hesapla (Normali saniyede 1 adım)
                        long interval = (long) (1_000_000_000 / speedFactor);

                        if (now - lastUpdate >= interval) {

                            // Güvenlik kontrolü: Robot veya oda yoksa patlamasın
                            if (room != null && room.getRobot() != null) {
                                Robot robot = room.getRobot();
                                Position currentPos = robot.getPosition();
                                // Eğer visitedCells null ise (yani Sıfırla'ya basılmışsa)
                                // robotun konumunu en baştan başlat!

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
                                    // SPİRAL ALGORİTMASI (Anında Dönen ve Eve Dönüşlü)
                                    // ==========================================
                                     if (algoName.equals("Spiral")) {
                                        int currentRow = currentPos.getRow();
                                        int currentCol = currentPos.getCol();

                                        int nextRow = currentRow;
                                        int nextCol = currentCol;

                                        // 1. Mevcut yöne göre önündeki hücreyi hesapla
                                        if (robot.getDirection() == model.Direction.RIGHT) nextCol++;
                                        else if (robot.getDirection() == model.Direction.UP) nextRow--;
                                        else if (robot.getDirection() == model.Direction.LEFT) nextCol--;
                                        else if (robot.getDirection() == model.Direction.DOWN) nextRow++;

                                        boolean hitWall = nextRow < 0 || nextRow >= room.getRows() || nextCol < 0 || nextCol >= room.getCols();
                                        boolean alreadyCleaned = !hitWall && visitedCells[nextRow][nextCol];

                                        if (!hitWall && !alreadyCleaned) {
                                            // Önü boşsa hiç vakit kaybetmeden dümdüz devam et
                                            robot.move(new Position(nextRow, nextCol));
                                            visitedCells[nextRow][nextCol] = true;
                                        } else {
                                            // 2. ÖNÜ TIKALIYSA: Bir sonraki dönüş yönünü ANINDA belirle (Bekleme/Fır dönme bitti!)
                                            model.Direction nextDir = robot.getDirection();
                                            if (robot.getDirection() == model.Direction.RIGHT) nextDir = model.Direction.UP;
                                            else if (robot.getDirection() == model.Direction.UP) nextDir = model.Direction.LEFT;
                                            else if (robot.getDirection() == model.Direction.LEFT) nextDir = model.Direction.DOWN;
                                            else if (robot.getDirection() == model.Direction.DOWN) nextDir = model.Direction.RIGHT;

                                            int turnRow = currentRow;
                                            int turnCol = currentCol;
                                            if (nextDir == model.Direction.RIGHT) turnCol++;
                                            else if (nextDir == model.Direction.UP) turnRow--;
                                            else if (nextDir == model.Direction.LEFT) turnCol--;
                                            else if (nextDir == model.Direction.DOWN) turnRow++;

                                            boolean turnHitWall = turnRow < 0 || turnRow >= room.getRows() || turnCol < 0 || turnCol >= room.getCols();
                                            boolean turnAlreadyCleaned = !turnHitWall && visitedCells[turnRow][turnCol];

                                            if (!turnHitWall && !turnAlreadyCleaned) {
                                                // Yeni yönü salisaniyede hesapla ve kafayı o yöne çevirip anında adım at!
                                                robot.setDirection(nextDir);
                                                robot.move(new Position(turnRow, turnCol));
                                                visitedCells[turnRow][turnCol] = true;
                                            } else {
                                                // 3. İKİ YÖN DE TIKALIYSA: Spiral tamamen bitti, merkeze ulaşıldı!
                                                // Şimdi robotu akıllıca (13, 0) ŞARJ İSTASYONUNA geri yürütüyoruz
                                                int startRow = room.getRows() - 1;
                                                int startCol = 0;

                                                if (currentRow == startRow && currentCol == startCol) {
                                                    // İstasyona sağ salim vardı, yüzünü odaya dön ve motoru kapat!
                                                    robot.setDirection(model.Direction.RIGHT);
                                                    System.out.println("Spiral temizliği bitti, robot yuvaya dönüp park etti.");
                                                    simulationTimer.stop();
                                                } else {
                                                    // Adım adım istasyona doğru en kısa yoldan geri dönüş rotası
                                                    if (currentRow < startRow) {
                                                        robot.setDirection(model.Direction.DOWN);
                                                        robot.move(new Position(currentRow + 1, currentCol));
                                                    } else if (currentCol > startCol) {
                                                        robot.setDirection(model.Direction.LEFT);
                                                        robot.move(new Position(currentRow, currentCol - 1));
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    // ==========================================
                                    // DUVAR TAKİP ALGORİTMASI (İstasyona Dönüp Duran Versiyon)
                                    // ==========================================
                                    else if (algoName.equals("Duvar Takip")) {
                                        int currentRow = currentPos.getRow();
                                        int currentCol = currentPos.getCol();

                                        int nextRow = currentRow;
                                        int nextCol = currentCol;

                                        // 1. Mevcut yöne göre bir sonraki adımı hesapla
                                        if (robot.getDirection() == model.Direction.RIGHT) nextCol++;
                                        else if (robot.getDirection() == model.Direction.DOWN) nextRow++;
                                        else if (robot.getDirection() == model.Direction.LEFT) nextCol--;
                                        else if (robot.getDirection() == model.Direction.UP) nextRow--;

                                        // Odanın başlangıç noktasını (Şarj İstasyonunu) otomatik bul
                                        int startRow = room.getRows() - 1; // 13. satır
                                        int startCol = 0;                  // 0. sütun

                                        // İSTASYON KONTROLÜ: Bir sonraki adım istasyonsa temizliği bitir!
                                        if (nextRow == startRow && nextCol == startCol) {
                                            robot.move(new Position(startRow, startCol)); // İstasyona tam gir
                                            robot.setDirection(model.Direction.RIGHT);    // Yüzünü odaya dönerek park et

                                            System.out.println("Görev tamamlandı! Robot istasyona döndü ve şarja geçiyor.");
                                            simulationTimer.stop(); // Motoru kapat
                                        }
                                        else {
                                            // 2. Duvara çarptı mı kontrolü
                                            boolean hitWall = nextRow < 0 || nextRow >= room.getRows() || nextCol < 0 || nextCol >= room.getCols();

                                            if (!hitWall) {
                                                // Önü boşsa hiç bozmadan dümdüz ilerle
                                                robot.move(new Position(nextRow, nextCol));
                                                visitedCells[nextRow][nextCol] = true;
                                            } else {
                                                // Köşeye gelince Saat Yönünde (sağdan yukarı, yukarıdan sola) dön!
                                                if (robot.getDirection() == model.Direction.RIGHT) robot.setDirection(model.Direction.UP);
                                                else if (robot.getDirection() == model.Direction.UP) robot.setDirection(model.Direction.LEFT);
                                                else if (robot.getDirection() == model.Direction.LEFT) robot.setDirection(model.Direction.DOWN);
                                                else if (robot.getDirection() == model.Direction.DOWN) robot.setDirection(model.Direction.RIGHT);
                                            }
                                        }
                                    }

                                    // RASTGELE MODU (Senin stratejin doğrultusunda pas geçildi)
                                    else if (algoName.equals("Rastgele")) {
                                        System.out.println("Rastgele modu mobilyalardan sonra kodlanacak.");
                                    }
                                    // ========================================================
                                    // YENİ KODU TAM OLARAK BURAYA, O PARANTEZİN ALTINA YAPIŞTIR:
                                    // ========================================================
                                    int totalCells = room.getRows() * room.getCols();

                                    int cleanedCells = 0;
                                    if (visitedCells != null) {
                                        for (int r = 0; r < room.getRows(); r++) {
                                            for (int c = 0; c < room.getCols(); c++) {
                                                if (visitedCells[r][c]) {
                                                    cleanedCells++;
                                                }
                                            }
                                        }
                                    }

                                    int obstacleCount = 0;
                                    int remainingCells = totalCells - cleanedCells - obstacleCount;

                                    int cleanedPercent = (int) Math.round(((double) cleanedCells / totalCells) * 100);
                                    int remainingPercent = (int) Math.round(((double) remainingCells / totalCells) * 100);

                                    cleanedAreaLabel.setText(cleanedCells + " m² (" + cleanedPercent + "%)");
                                    remainingAreaLabel.setText(remainingCells + " m² (" + remainingPercent + "%)");
                                    // ========================================================

                                }

                                // 3. EKRANI GÜNCELLE
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
                                Cell currentCell =
                                        room.getCell(
                                                robot.getPosition().getRow(),
                                                robot.getPosition().getCol()
                                        );

                                if(currentCell.isDirty())
                                {
                                    currentCell.setDirt(null);
                                }
                            }

                            lastUpdate = now;
                        }
                    }
                };
                simulationTimer.start();
                System.out.println("MOTOR ÇALIŞTI! Robot hareket ediyor...");
            }
            // İf bloğunun dışına çıktık, artık güvenle başlatabiliriz:
            simulationTimer.start();
            System.out.println("Simülasyon başlatıldı / devam ediyor!");

        });

    }
    // ==========================================
    // BUTON KONTROLLERİ
    // ==========================================

    @FXML
    public void onDuraklatClick() {
        // Eğer motor çalışıyorsa anında durdur
        if (simulationTimer != null) {
            simulationTimer.stop();
            System.out.println("Simülasyon duraklatıldı!");
        }
    }

    @FXML
    public void onSifirlaClick()
    {
        simulationPane.getChildren().removeIf(node ->
                node instanceof Circle
        );

        for(int row = 0; row < room.getRows(); row++)
        {
            for(int col = 0; col < room.getCols(); col++)
            {
                room.getCell(row,col)
                        .setDirt(null);
            }
        }

        if(simulationTimer != null)
        {
            simulationTimer.stop();
        }

        room.getRobot().setPosition(
                new Position(13,0)
        );

        room.getRobot().setDirection(
                model.Direction.RIGHT
        );

        visitedCells =
                new boolean[
                        room.getRows()
                        ][
                        room.getCols()
                        ];

        visitedCells[13][0] = true;

        canvas.draw();
        System.out.println(
                "Simülasyon sıfırlandı.");

        elapsedSeconds = 0;
        if (timeLabel != null) timeLabel.setText("00:00");
    }
}

