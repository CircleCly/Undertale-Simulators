package alpha.model.enums;

public enum Direction {
    UP(0),
    RIGHT(1),
    DOWN(2),
    LEFT(3);

    private final int value;

    Direction(int value) {
        this.value = value;
    }

    public int toInt() {
        return value;
    }

    public static Direction fromInt(int value) {
        switch (value) {
            case 0: return UP;
            case 1: return RIGHT;
            case 2: return DOWN;
            case 3: return LEFT;
            default: throw new IllegalArgumentException("Direction must be 0-3, got " + value);
        }
    }

    public Direction opposite() {
        switch (this) {
            case UP: return DOWN;
            case DOWN: return UP;
            case LEFT: return RIGHT;
            case RIGHT: return LEFT;
            default: throw new IllegalStateException();
        }
    }
}
