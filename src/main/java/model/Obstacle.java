package model;

public class Obstacle {

    private Position position;

    // Görsel boyutu
    private int imageWidth;
    private int imageHeight;

    // Çarpışma boyutu
    private int collisionWidth;
    private int collisionHeight;

    private int collisionRowOffset;
    private int collisionColOffset;

    private String imagePath;

    public Obstacle(
            Position position,
            int imageWidth,
            int imageHeight,
            int collisionWidth,
            int collisionHeight,
            int collisionRowOffset,
            int collisionColOffset,
            String imagePath)
    {
        this.position = position;

        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;

        this.collisionWidth = collisionWidth;
        this.collisionHeight = collisionHeight;

        this.collisionRowOffset = collisionRowOffset;
        this.collisionColOffset = collisionColOffset;

        this.imagePath = imagePath;
    }

    public Position getPosition() {
        return position;
    }

    public int getImageWidth()
    {
        return imageWidth;
    }

    public int getImageHeight()
    {
        return imageHeight;
    }

    public int getCollisionWidth()
    {
        return collisionWidth;
    }

    public int getCollisionHeight()
    {
        return collisionHeight;
    }

    public String getImagePath() {
        return imagePath;
    }
    public int getCollisionRowOffset()
    {
        return collisionRowOffset;
    }

    public int getCollisionColOffset()
    {
        return collisionColOffset;
    }
}