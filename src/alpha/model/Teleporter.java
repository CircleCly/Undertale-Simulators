package alpha.model;

import java.awt.Rectangle;
import alpha.Test;

public class Teleporter extends Entity {
    public boolean activated = false;

    public Teleporter(int x, int y, int width, int height, int direction) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.hitbox = new Rectangle(x, y, width, height);
        this.direction = direction;
    }

    public void transport() {
        // direction： 边界所处的方向
        switch (this.direction) {
            case 0:
                Test.STATE.player.y = 425;
                break;
            case 1:
                Test.STATE.player.x = 75;
                break;
            case 2:
                Test.STATE.player.y = 75;
                break;
            case 3:
                Test.STATE.player.x = 425;
                break;

        }
    }
}
