package alpha.attack;

import alpha.GameState;
import alpha.model.*;
import java.util.Vector;
import java.util.Random;

public class SpearAttackTwo implements Attack {
    public SpearAttackTwo() {}

    // Legacy static entry point – remove after Phase 5 scheduler rewrite
    public static void execute(GameState gs) {
        new SpearAttackTwo().tick(gs);
    }

    @Override
    public void tick(GameState gs) {

        if (gs.ticks % 40 == 0) {
        alpha.system.ModeSystem.greentify(gs);

        Spear spear = new Spear();

        float x = 0;
        float y = 0;
        int width = 0;
        int height = 0;
        int speed;
        int direction = new Random().nextInt(4);
        if (Math.random() > 0.6) {
        spear.color = "Yellow";

        }

        switch (direction) {
        case 2:
        x = gs.player.x;
        y = -100;
        width = 15;
        height = 45;
        spear.disappearY = 600;
        break;
        case 3:
        x = 600;
        y = gs.player.y;
        width = 45;
        height = 15;
        spear.disappearX = -100;
        break;
        case 0:
        x = gs.player.x;
        y = 600;
        width = 15;
        height = 45;
        spear.disappearY = -100;
        break;
        case 1:
        x = -100;
        y = gs.player.y;
        width = 45;
        height = 15;
        spear.disappearX = 600;
        break;
        }
        speed = 10;
        spear.x = x;
        spear.y = y;
        spear.width = width;
        spear.height = height;
        spear.direction = direction;
        spear.speed = speed;

        gs.spears.add(spear);
        }

    }
}
