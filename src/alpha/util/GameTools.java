package alpha.util;

import alpha.model.enums.Direction;

public class GameTools {
    public static int getOppositeDirection(int direction) {
        if (direction == 0) {
            return 2;
        } else if (direction == 1) {
            return 3;
        } else if (direction == 2) {
            return 0;
        } else if (direction == 3) {
            return 1;
        } else {
            throw new IllegalArgumentException("The direction must be 0 or 1 or 2 or 3!");
        }
    }

    public static Direction getOppositeDirection(Direction direction) {
        return direction.opposite();
    }
}
