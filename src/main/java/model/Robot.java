package model;

public class Robot {

    private Position position;
    private Direction direction;
    private double batteryLevel;
    private double speed;
    private int cleanedCells;

    public Robot(Position position) {
        this.position = position;
        this.direction = Direction.RIGHT; // Varsayılan yön
        this.batteryLevel = 100.0;
        this.speed = 1.0;
        this.cleanedCells = 0;
    }

    // --- MEVCUT GETTER VE SETTER'LAR  ---

    public Position getPosition() { return position; }
    public void setPosition(Position position) { this.position = position; }
    public Direction getDirection() { return direction; }
    public void setDirection(Direction direction) { this.direction = direction; }
    public double getBatteryLevel() { return batteryLevel; }
    public void setBatteryLevel(double batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public double getSpeed() { return speed; }
    public void setSpeed(double speed) { this.speed = speed; }
    public int getCleanedCells() { return cleanedCells; }

    // --- MEVCUT TEMEL DAVRANIŞLAR ---

    public void decreaseBattery(double amount) {
        batteryLevel -= amount;
        if (batteryLevel < 0) {
            batteryLevel = 0;
        }
    }

    public void chargeBattery() {
        batteryLevel = 100;
    }

    public boolean isBatteryLow() {
        return batteryLevel <= 20;
    }

    public void increaseCleanedCells() {
        cleanedCells++;
    }

    // =========================================================================
    // --- YENİ EKLENEN AKSİYON METOTLARI (Simülasyonun Hareket Motorları) ---
    // =========================================================================

    /**
     * Robotu yeni konuma taşır ve her adımda standart batarya tüketir.
     */
    /**
     * Robotu yeni konuma taşır ve her adımda standart batarya tüketir.
     */
    public void move(Position nextPosition) {

        if (nextPosition.getCol() > this.position.getCol()) this.direction = Direction.RIGHT;
        else if (nextPosition.getCol() < this.position.getCol()) this.direction = Direction.LEFT;
        else if (nextPosition.getRow() > this.position.getRow()) this.direction = Direction.DOWN;
        else if (nextPosition.getRow() < this.position.getRow()) this.direction = Direction.UP;

        // Konumu güncelle
        this.position = nextPosition;

        // Her adımda batarya harcansın
        decreaseBattery(0.5);
    }

    /**
     * Kiri temizler ve kirin zorluğuna göre batarya tüketir.
     */
    public void cleanDirt(DirtType type) {
        increaseCleanedCells();

        // PDF'te "farklı kirler farklı batarya harcar ve süre alır" maddesi için:
        switch (type) {
            case DUST:
                decreaseBattery(2.0); // Toz kolay temizlenir
                break;
            case LIQUID:
                decreaseBattery(2.5); // Sıvı biraz daha zor
                break;
            case STAIN:
                decreaseBattery(3.5); // Leke zor temizlenir, çok şarj yer
                break;
            default:
                decreaseBattery(1.0);
        }
    }
}