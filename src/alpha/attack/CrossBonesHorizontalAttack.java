package alpha.attack;

import alpha.GameState;
import alpha.model.*;
import java.util.Vector;
import java.util.Random;

public class CrossBonesHorizontalAttack {
    public static void execute(GameState gs) {

        //大骨头
        if (gs.ticks % 40 == 20) {
        Bone b1 = new Bone();
        b1.direction = 3;
        b1.x = 600;
        b1.y = 0;
        b1.width = 15;
        b1.height = 450;
        b1.speed = 9;
        b1.disappearX = -100;

        gs.bones.add(b1);

        } else if (gs.ticks % 40 == 0) {
        Bone b2 = new Bone();
        b2.direction = 1;
        b2.x = -100;
        b2.y = 450;
        b2.width = 15;
        b2.height = 50;
        b2.speed = 9;
        b2.disappearX = 600;

        gs.bones.add(b2);
        }

    }
}
