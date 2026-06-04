package model;

public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT; // <-- Sihirli noktalı virgülümüz!

    public Direction getRightDirection() {
        if (this == RIGHT) return DOWN;
        if (this == DOWN) return LEFT;
        if (this == LEFT) return UP;
        return RIGHT;
    }

    public Direction getLeftDirection() {
        if (this == RIGHT) return UP;
        if (this == UP) return LEFT;
        if (this == LEFT) return DOWN;
        return RIGHT;
    }
}