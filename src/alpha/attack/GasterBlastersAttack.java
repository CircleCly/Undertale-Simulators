package alpha.attack;

import alpha.GameState;
import alpha.model.*;
import java.util.Vector;
import java.util.Random;

public class GasterBlastersAttack {
    public static void execute(GameState gs) {

        if (gs.ticks % 120 == 0) {
        Bone b1 = new Bone();
        b1.x = 0;
        b1.y = 0;
        b1.width = 500;
        b1.height = 200;
        b1.fadeOut = true;
        b1.duration = 15;
        b1.maxDuration = 15;
        b1.speed = 0;
        Bone b2 = new Bone();
        b2.x = 0;
        b2.y = 0;
        b2.width = 200;
        b2.height = 500;
        b2.fadeOut = true;
        b2.duration = 15;
        b2.maxDuration = 15;
        b2.speed = 0;
        Bone b3 = new Bone();
        b3.x = 0;
        b3.y = 300;
        b3.width = 500;
        b3.height = 200;
        b3.fadeOut = true;
        b3.duration = 15;
        b3.maxDuration = 15;
        b3.speed = 0;
        Bone b4 = new Bone();
        b4.x = 300;
        b4.y = 0;
        b4.width = 200;
        b4.height = 500;
        b4.fadeOut = true;
        b4.duration = 15;
        b4.maxDuration = 15;
        b4.speed = 0;
        gs.bones.add(b1);
        gs.bones.add(b2);
        gs.bones.add(b3);
        gs.bones.add(b4);
        } else if (gs.ticks % 120 == 60) {
        Bone b1 = new Bone();
        b1.x = 0;
        b1.y = 200;
        b1.width = 500;
        b1.height = 100;
        b1.fadeOut = true;
        b1.duration = 15;
        b1.maxDuration = 15;
        b1.speed = 0;
        gs.bones.add(b1);
        }

    }
}
