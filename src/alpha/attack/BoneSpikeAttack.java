package alpha.attack;

import alpha.GameState;
import alpha.model.*;
import java.util.Vector;
import java.util.Random;

public class BoneSpikeAttack {
    public static void execute(GameState gs, int direction) {


        if (gs.ticks % 60 == 0) {

        switch (direction) {
        case 0:
        Bone b = new Bone();
        b.x = 0;
        b.y = -330;
        b.width = 600;
        b.height = 150;
        b.direction = 2;
        b.speed = 10;
        b.disappearY = 0;

        gs.bones.add(b);
        alpha.system.ModeSystem.bluetify(gs, 0);
        //			gs.player.y=20;
        gs.player.gSpeed += 1000.0;
        break;
        case 1:
        Bone b1 = new Bone();
        b1.x = 680;
        b1.y = 0;
        b1.width = 150;
        b1.height = 600;
        b1.direction = 3;
        b1.speed = 10;
        b1.disappearX = 350;

        gs.bones.add(b1);
        //	gs.player.x = 480-gs.player.width;
        alpha.system.ModeSystem.bluetify(gs, 1);
        gs.player.gSpeed += 1000.0;
        break;
        case 2:
        Bone b2 = new Bone();
        b2.x = 0;
        b2.y = 680;
        b2.width = 600;
        b2.height = 150;
        b2.direction = 0;
        b2.speed = 10;
        b2.disappearY = 350;

        gs.bones.add(b2);
        //	gs.player.y = 480-gs.player.height;
        alpha.system.ModeSystem.bluetify(gs, 2);
        gs.player.gSpeed += 1000.0;
        break;
        case 3:
        Bone b3 = new Bone();
        b3.x = -330;
        b3.y = 0;
        b3.width = 150;
        b3.height = 600;
        b3.direction = 1;
        b3.speed = 10;
        b3.disappearX = 0;

        gs.bones.add(b3);
        //gs.player.x = 20;
        alpha.system.ModeSystem.bluetify(gs, 3);
        gs.player.gSpeed += 1000.0;
        break;

        }
        }

    }
}
