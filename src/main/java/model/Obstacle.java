package model;

public class Obstacle {

    private Position position;
    private int width;
    private int height;
    private String imagePath;

    public Obstacle(
            Position position,
            int width,
            int height,
            String imagePath)
    {
        this.position = position;
        this.width = width;
        this.height = height;
        this.imagePath = imagePath;
    }

    public Position getPosition() {
        return position;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getImagePath() {
        return imagePath;
    }
}