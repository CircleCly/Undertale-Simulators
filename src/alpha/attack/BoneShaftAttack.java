package alpha.attack;

import alpha.GameState;
import alpha.model.*;
import java.util.Vector;
import java.util.Random;

public class BoneShaftAttack {
    public static void execute(GameState gs) {


        if (gs.ticks % 4 == 0) {
        Bone b1 = new Bone();
        b1.x = 0;
        b1.y = 0;
        b1.width = 30;
        int x = 0;
        if (gs.ticks <= 450 && gs.ticks >= 300) {
        x = (int) ((gs.ticks - 300) * 2 + 50);
        } else if (gs.ticks <= 600 && gs.ticks > 450) {
        x = (int) (-(gs.ticks - 450) * 2 + 350);
        }
        if (gs.ticks <= 14600 && gs.ticks >= 14300) {
        x = (int) ((gs.ticks - 14300) * 0.75 + 100);
        } else if (gs.ticks <= 14900 && gs.ticks > 14600) {
        x = (int) (-(gs.ticks - 14600) * 0.75 + 325);
        }
        b1.height = x;
        b1.disappearX = 700;

        Bone b2 = new Bone();
        b2.x = 0;
        b2.y = x + 65;
        b2.width = 30;
        b2.height = 500 - (x + 100);
        b2.disappearX = 700;

        b1.direction = 1;
        b2.direction = 1;
        b1.speed = 25;
        b2.speed = 25;

        gs.bones.add(b1);
        gs.bones.add(b2);
        }

    }
}
