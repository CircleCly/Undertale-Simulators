package alpha.attack;

import alpha.GameState;
import alpha.model.*;
import java.util.Vector;
import java.util.Random;

public class AugmentedGasterBlastersAttack {
    public static void execute(GameState gs) {

        if (gs.ticks % 30 == 0) {

        Bone b1 = new Bone();

        if ((gs.ticks - 20000) % 120 < 60) {

        b1.x = 60 + ((gs.ticks - 20000) % 60) * 3;
        b1.y = 0;
        b1.width = 50;
        b1.height = 500;
        } else if ((gs.ticks - 20000) % 120 >= 60) {
        b1.x = 0;
        b1.y = 60 + ((gs.ticks - 20000) % 60) * 3;
        b1.width = 500;
        b1.height = 50;
        }

        b1.fadeOut = true;
        b1.duration = 30;
        b1.maxDuration = 30;
        b1.speed = 0;

        gs.bones.add(b1);

        }

    }
}
