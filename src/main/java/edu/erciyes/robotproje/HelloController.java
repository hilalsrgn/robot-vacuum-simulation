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
import controller.PathFinder;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;


public class HelloController
{

    // Bunu en tepeye, diğer @FXML tanımlarının yanına ekle
    private Position startPosition = new Position(13, 0);
    private boolean[][] visitedCells;
    private List<Position> returnPath;
    private int returnPathIndex = 0;
    private boolean returningToStation = false;
    private Random random = new Random();

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

    @FXML private Label batteryPercentageLabel;

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
    private RadioButton armchairRadioButton;

    @FXML
    private RadioButton plantRadioButton;

    private int initialDirtCount = 0;
    private boolean[][] initiallyDirty;

    private boolean isRunning = false;
    private int elapsedSeconds = 0;
    private long lastSecondUpdate = 0;
    @FXML
    public void initialize()
    {
        room = new Room(14, 20);

        room.getObstacles().add(
                new Obstacle(
                        new Position(1,7),

                        4,5,

                        3,2,

                        1,1,

                        "/images/sofa.png"
                )
        );
        room.getObstacles().add(
                new Obstacle(
                        new Position(6,8),

                        4,4,

                        4,2,

                        1,0,

                        "/images/table.png"
                )
        );


        room.getObstacles().add(
                new Obstacle(
                        new Position(5,3),

                        4,4,

                        2,2,

                        1,1,

                        "/images/armchair.png"
                )
        );

        room.getObstacles().add(
                new Obstacle(
                        new Position(1,1),

                        1,1,   // görsel boyutu

                        1,1,   // çarpışma boyutu

                        0,0,   // offset

                        "/images/plant.png"
                )
        );
        room.getObstacles().add(
                new Obstacle(
                        new Position(11,17),

                        1,1,

                        1,1,

                        0,0,

                        "/images/plant.png"
                )
        );

        room.getObstacles().add(
                new Obstacle(
                        new Position(4,17),

                        3,6,      // görsel boyutu

                        2,5,      // çarpışma alanı

                        0,0,      // offset

                        "/images/shelf.png"
                )
        );



        for(Obstacle obstacle :
                room.getObstacles())
        {
            int startRow =
                    obstacle.getPosition().getRow()
                            + obstacle.getCollisionRowOffset();

            int startCol =
                    obstacle.getPosition().getCol()
                            + obstacle.getCollisionColOffset();

            for(int r = startRow;
                r < startRow + obstacle.getCollisionHeight();
                r++)
            {
                for(int c = startCol;
                    c < startCol + obstacle.getCollisionWidth();
                    c++)
                {
                    room.getCell(r,c)
                            .setObstacle(true);
                }
            }
        }

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
        PathFinder pathFinder =
                new PathFinder();

        List<Position> path =
                pathFinder.findShortestPath(
                        room,
                        new Position(13,0),
                        new Position(0,19)
                );

        System.out.println("Yol bulundu:");
        for(Position p : path)
        {
            System.out.println(
                    p.getRow() + "," +
                            p.getCol()
            );
        }
        room.setRobot(robot);

        canvas =
                new SimulationCanvas(room);

        canvas.setMouseTransparent(true);

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

        // MOBİLYA TÜRLERİ İÇİN
        ToggleGroup furnitureGroup = new ToggleGroup();

        armchairRadioButton.setToggleGroup(
                furnitureGroup
        );

        plantRadioButton.setToggleGroup(
                furnitureGroup
        );

        armchairRadioButton.setSelected(true);

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
        simulationPane.setOnMousePressed(event ->
        {
            System.out.println("MOUSE BASILDI");

            startReturnToStation();
        });
        // SADECE KİR EKLEME VE GÖRSELLEŞTİRME KODU
        simulationPane.setOnMouseClicked(event -> {
            System.out.println("TIKLAMA ALGILANDI");

            // Simülasyon çalışıyorsa kir eklemeyi engelle
            if (isRunning) {
                return;
            }

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

                    if(room.getCell(row,col).isObstacle())
                    {
                        System.out.println(
                                "Mobilyanın üzerine kir eklenemez!"
                        );
                        return;
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

            else if(currentInteractionMode.equals("OBSTACLE"))
            {
                double cellWidth =
                        simulationPane.getWidth() / room.getCols();

                double cellHeight =
                        simulationPane.getHeight() / room.getRows();

                int col =
                        (int)(mouseX / cellWidth);

                int row =
                        (int)(mouseY / cellHeight);

                Obstacle obstacle;

                if(armchairRadioButton.isSelected())
                {
                    obstacle =
                            new Obstacle(
                                    new Position(row,col),

                                    2,2,

                                    2,2,

                                    0,0,

                                    "/images/puf.png"
                            );
                }
                else
                {
                    obstacle =
                            new Obstacle(
                                    new Position(row,col),

                                    1,1,

                                    1,1,

                                    0,0,

                                    "/images/plant.png"
                            );
                }

                room.getObstacles().add(obstacle);

                markObstacleCells(obstacle);

                canvas.draw();

                System.out.println(
                        "Yeni mobilya eklendi: "
                                + row + "," + col
                );
            }

        });
        startButton.setOnAction(e -> {

            isRunning = true;
            // --- BAŞLANGIÇTAKİ KİRLERİ HAFIZAYA ALMA KODU ---
            initialDirtCount = 0;
            initiallyDirty = new boolean[room.getRows()][room.getCols()];
            visitedCells = new boolean[room.getRows()][room.getCols()];
            visitedCells[room.getRows() - 1][0] = true;

            for (int r = 0; r < room.getRows(); r++) {
                for (int c = 0; c < room.getCols(); c++) {
                    if (room.getCell(r, c).getDirt() != null) {
                        initiallyDirty[r][c] = true;
                        initialDirtCount++;
                    }
                }
            }
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
                                if(returningToStation)
                                {
                                    if(returnPath != null &&
                                            returnPathIndex < returnPath.size())
                                    {
                                        Position nextPos =
                                                returnPath.get(returnPathIndex);

                                        room.getRobot().move(nextPos);

                                        returnPathIndex++;

                                        canvas.draw();
                                        return;
                                    }
                                    else
                                    {
                                        returningToStation = false;

                                        room.getRobot().chargeBattery();

                                        System.out.println(
                                                "Robot istasyona ulaştı ve şarj oldu."
                                        );
                                    }
                                }
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

                                        boolean hitWall =
                                                !isValidMove(nextRow, nextCol);
                                        boolean alreadyCleaned = !hitWall && visitedCells[nextRow][nextCol];

                                        if (!hitWall && !alreadyCleaned) {
                                            // Önü boşsa hiç vakit kaybetmeden dümdüz devam et
                                            robot.move(new Position(nextRow, nextCol));
                                            visitedCells[nextRow][nextCol] = true;
                                        } else {
                                            // 2. ÖNÜ TIKALIYSA: Bir sonraki dönüş yönünü ANINDA belirle (Bekleme/Fır dönme bitti!)
                                            model.Direction nextDir = robot.getDirection();
                                            if (robot.getDirection() == model.Direction.RIGHT)
                                                nextDir = model.Direction.UP;
                                            else if (robot.getDirection() == model.Direction.UP)
                                                nextDir = model.Direction.LEFT;
                                            else if (robot.getDirection() == model.Direction.LEFT)
                                                nextDir = model.Direction.DOWN;
                                            else if (robot.getDirection() == model.Direction.DOWN)
                                                nextDir = model.Direction.RIGHT;

                                            int turnRow = currentRow;
                                            int turnCol = currentCol;
                                            if (nextDir == model.Direction.RIGHT) turnCol++;
                                            else if (nextDir == model.Direction.UP) turnRow--;
                                            else if (nextDir == model.Direction.LEFT) turnCol--;
                                            else if (nextDir == model.Direction.DOWN) turnRow++;

                                            boolean turnHitWall =
                                                    !isValidMove(turnRow, turnCol);
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
                                        } else {
                                            // 2. Duvara çarptı mı kontrolü
                                            boolean hitWall =
                                                    !isValidMove(nextRow, nextCol);

                                            if (!hitWall) {
                                                // Önü boşsa hiç bozmadan dümdüz ilerle
                                                robot.move(new Position(nextRow, nextCol));
                                                visitedCells[nextRow][nextCol] = true;
                                            } else {
                                                // Köşeye gelince Saat Yönünde (sağdan yukarı, yukarıdan sola) dön!
                                                if (robot.getDirection() == model.Direction.RIGHT)
                                                    robot.setDirection(model.Direction.UP);
                                                else if (robot.getDirection() == model.Direction.UP)
                                                    robot.setDirection(model.Direction.LEFT);
                                                else if (robot.getDirection() == model.Direction.LEFT)
                                                    robot.setDirection(model.Direction.DOWN);
                                                else if (robot.getDirection() == model.Direction.DOWN)
                                                    robot.setDirection(model.Direction.RIGHT);
                                            }
                                        }
                                    }

                                    // RASTGELE MODU (Senin stratejin doğrultusunda pas geçildi)
                                    else if (algoName.equals("Rastgele")) {
                                        System.out.println("Rastgele modu mobilyalardan sonra kodlanacak.");
                                    }
                                    // ========================================================
                                    // MERKEZİ PANEL GÜNCELLEME KODU (ALAN VE TOZ HESABI)
                                    // ========================================================
                                    int totalCells = room.getRows() * room.getCols();
                                    int cleanedCells = 0;
                                    int cleanedDirtCount = 0; // Temizlenen kirleri sayacağımız sayaç

                                    if (visitedCells != null) {
                                        for (int r = 0; r < room.getRows(); r++) {
                                            for (int c = 0; c < room.getCols(); c++) {
                                                if (visitedCells[r][c]) {
                                                    cleanedCells++;

                                                    // Eğer robotun bastığı bu hücrede başlangıçta kir varsa, temizlendi sayıyoruz
                                                    if (initiallyDirty != null && initiallyDirty[r][c]) {
                                                        cleanedDirtCount++;
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    int obstacleCount = 0;
                                    int remainingCells = totalCells - cleanedCells - obstacleCount;

                                    int cleanedPercent = (int) Math.round(((double) cleanedCells / totalCells) * 100);
                                    int remainingPercent = (int) Math.round(((double) remainingCells / totalCells) * 100);

                                    // M² Etiketlerini Güncelle
                                    cleanedAreaLabel.setText(cleanedCells + " m² (" + cleanedPercent + "%)");
                                    remainingAreaLabel.setText(remainingCells + " m² (" + remainingPercent + "%)");

                                    // --- YENİ: TOZ YÜZDESİNİ HESAPLA VE EKRANA BAS ---
                                    int dirtPercent = 0;
                                    if (initialDirtCount > 0) {
                                        dirtPercent = (int) Math.round(((double) cleanedDirtCount / initialDirtCount) * 100);
                                    }
                                    if (dustLabel != null) {
                                        dustLabel.setText(cleanedDirtCount + " / " + initialDirtCount + " (" + dirtPercent + "%)");
                                    }

                                    // ========================================================
                                    // ROBOT DURUMU VE GERÇEKÇİ BATARYA GÜNCELLEMESİ
                                    // ========================================================
                                    Position pos = room.getRobot().getPosition();

                                    // 1. Konumu Ekrana Yazdır
                                    if (positionLabel != null) {
                                        positionLabel.setText("Konum: (" + pos.getRow() + "," + pos.getCol() + ")");
                                    }

                                    // 2. Yönü Ekrana Yazdır
                                    if (directionLabel != null) {
                                        String yonStr = "Bilinmiyor";
                                        switch (room.getRobot().getDirection()) {
                                            case UP:
                                                yonStr = "Yukarı";
                                                break;
                                            case DOWN:
                                                yonStr = "Aşağı";
                                                break;
                                            case LEFT:
                                                yonStr = "Sol";
                                                break;
                                            case RIGHT:
                                                yonStr = "Sağ";
                                                break;
                                        }
                                        directionLabel.setText("Yön: " + yonStr);
                                    }

                                    // 3. Batarya ve Gerçekçi Temizlik Mantığı
                                    int r = pos.getRow();
                                    int c = pos.getCol();

                                    if (room.getCell(r, c).getDirt() != null) {
                                        DirtType currentDirtType = room.getCell(r, c).getDirt().getType();
                                        room.getRobot().cleanDirt(currentDirtType); // Kir zorluğuna göre şarj yer
                                        room.getCell(r, c).setDirt(null);
                                    }

                                    // --- TOZ YÜZDESİ KODUMUZ ---
                                    cleanedDirtCount = 0;
                                    if (visitedCells != null && initiallyDirty != null) {
                                        for (int satir = 0; satir < room.getRows(); satir++) {
                                            for (int sutun = 0; sutun < room.getCols(); sutun++) {
                                                if (visitedCells[satir][sutun] && initiallyDirty[satir][sutun]) {
                                                    cleanedDirtCount++;
                                                }
                                            }
                                        }
                                    }

                                    dirtPercent = 0;
                                    if (initialDirtCount > 0) {
                                        dirtPercent = (int) Math.round(((double) cleanedDirtCount / initialDirtCount) * 100);
                                    }
                                    if (dustLabel != null) {
                                        dustLabel.setText(cleanedDirtCount + " / " + initialDirtCount + " (" + dirtPercent + "%)");
                                    }

                                 // 4. Batarya Çubuğunu ve Yüzde Yazısını Güncelle
                                    if(robot.isBatteryLow()
                                            && !returningToStation)
                                    {
                                        System.out.println(
                                                "Batarya düşük! İstasyona dönülüyor..."
                                        );

                                        startReturnToStation();
                                    }
                                    if (batteryBar != null) {
                                        double guncelBatarya = room.getRobot().getBatteryLevel();
                                        batteryBar.setProgress(guncelBatarya / 100.0);

                                        // Küsuratı kaybetmeden yüzdeyi yazdır (Örn: %96.5)
                                        if (batteryPercentageLabel != null) {
                                            batteryPercentageLabel.setText("%" + String.format("%.1f", guncelBatarya));
                                        }
                                    }
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
                                double robotVisualX = robot.getPosition().getCol() * cellWidth + (cellWidth / 2);
                                double robotVisualY = robot.getPosition().getRow() * cellHeight + (cellHeight / 2);

                                // Üzerine bastığı kiri ekrandan uçur
                                Cell currentCell =
                                        room.getCell(
                                                robot.getPosition().getRow(),
                                                robot.getPosition().getCol()
                                        );

                                if (currentCell.isDirty()) {
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

            isRunning = false;
        }
    }

    @FXML
    public void onSifirlaClick()
    {
        isRunning = false;

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

        initialDirtCount = 0;
        initiallyDirty = null;
        if (dustLabel != null) {
            dustLabel.setText("0 / 0 (0%)");
        }

        int totalCells = room.getRows() * room.getCols();
        if (cleanedAreaLabel != null) cleanedAreaLabel.setText("0 m² (0%)");
        if (remainingAreaLabel != null) remainingAreaLabel.setText(totalCells + " m² (100%)");

        // Robotun gerçek bataryasını fulle
        room.getRobot().setBatteryLevel(100.0);

        if (batteryBar != null) batteryBar.setProgress(1.0);
        if (positionLabel != null) positionLabel.setText("Konum: (13,0)");
        if (directionLabel != null) directionLabel.setText("Yön: Sağ");

        if (batteryPercentageLabel != null) {
            batteryPercentageLabel.setText("%100.0");
        }

    }
    private void startReturnToStation()
    {
        PathFinder pathFinder = new PathFinder();

        returnPath = pathFinder.findShortestPath(
                room,
                room.getRobot().getPosition(),
                new Position(13,0)
        );

        returnPathIndex = 1;
        returningToStation = true;

        System.out.println(
                "İstasyona dönüş başlatıldı. Yol uzunluğu: "
                        + returnPath.size()
        );
    }

    private boolean isValidMove(int row, int col)
    {
        // Oda dışına çıkma
        if(row < 0 || row >= room.getRows())
            return false;

        if(col < 0 || col >= room.getCols())
            return false;

        // Mobilyaya girme
        if(room.getCell(row,col).isObstacle())
            return false;

        return true;
    }

    private void markObstacleCells(Obstacle obstacle)
    {
        int startRow =
                obstacle.getPosition().getRow()
                        + obstacle.getCollisionRowOffset();

        int startCol =
                obstacle.getPosition().getCol()
                        + obstacle.getCollisionColOffset();

        for(int r = startRow;
            r < startRow + obstacle.getCollisionHeight();
            r++)
        {
            for(int c = startCol;
                c < startCol + obstacle.getCollisionWidth();
                c++)
            {
                if(r >= 0 &&
                        r < room.getRows() &&
                        c >= 0 &&
                        c < room.getCols())
                {
                    room.getCell(r,c)
                            .setObstacle(true);
                }
            }
        }
    }
}

