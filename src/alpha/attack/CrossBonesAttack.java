package alpha.attack;

import alpha.GameState;
import alpha.model.*;
import java.util.Vector;
import java.util.Random;

public class CrossBonesAttack implements Attack {
    public CrossBonesAttack() {}

    // Legacy static entry point – remove after Phase 5 scheduler rewrite
    public static void execute(GameState gs) {
        new CrossBonesAttack().tick(gs);
    }

    @Override
    public void tick(GameState gs) {


        if (gs.ticks % 40 == 28) {
        Bone b1 = new Bone();
        b1.direction = 0;
        b1.x = 0;
        b1.y = 600;
        b1.width = 250;
        b1.height = 30;
        b1.speed = 14;
        b1.disappearY = 0;
        b1.randomizeColor();
        gs.bones.add(b1);

        } else if (gs.ticks % 40 == 0) {
        Bone b2 = new Bone();
        b2.direction = 2;
        b2.x = 250;
        b2.y = 0;
        b2.width = 250;
        b2.height = 30;
        b2.speed = 14;
        b2.disappearY = 600;
        b2.randomizeColor();
        gs.bones.add(b2);
        }

    }
}
