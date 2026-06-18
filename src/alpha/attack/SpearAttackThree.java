package alpha.attack;

import alpha.util.GameTools;

import alpha.GameState;
import alpha.model.*;
import java.util.Vector;
import java.util.Random;

public class SpearAttackThree implements Attack {
    public SpearAttackThree() {}

    // Legacy static entry point – remove after Phase 5 scheduler rewrite
    public static void execute(GameState gs) {
        new SpearAttackThree().tick(gs);
    }

    @Override
    public void tick(GameState gs) {

        alpha.system.ModeSystem.greentify(gs);
        if (gs.ticks % 40 == 0) {
        Spear spear = new Spear();
        int direction = GameTools.getOppositeDirection(gs.player.shield.direction);
        float x = 0;
        float y = 0;
        int width = 0;
        int height = 0;
        int speed;

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
        speed = 6;
        spear.x = x;
        spear.y = y;
        spear.width = width;
        spear.height = height;
        spear.direction = direction;
        spear.speed = speed;
        spear.color = "Yellow";

        gs.spears.add(spear);
        }

    }
}
