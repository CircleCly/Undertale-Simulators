package alpha.attack;

import alpha.GameState;
import alpha.model.*;
import java.util.Vector;
import java.util.Random;

public class SniperBoneAttack implements Attack {
    private final int interval;

    public SniperBoneAttack(int interval) {
        this.interval = interval;
    }

    // Legacy static entry point – remove after Phase 5 scheduler rewrite
    public static void execute(GameState gs, int interval) {
        new SniperBoneAttack(interval).tick(gs);
    }

    @Override
    public void tick(GameState gs) {


        if (gs.ticks % interval == 0) {

        Bone b1 = new Bone();
        b1.x = gs.player.x;
        b1.y = -300;
        b1.width = 15;
        b1.height = 75;
        b1.direction = 2;
        b1.speed = 60;
        b1.disappearY = 700;

        Bone b2 = new Bone();
        b2.y = gs.player.y;
        b2.x = 800;
        b2.width = 75;
        b2.height = 15;
        b2.direction = 3;
        b2.speed = 60;
        b2.disappearX = -150;
        Bone b3 = new Bone();
        b3.x = gs.player.x;
        b3.y = 800;
        b3.width = 15;
        b3.height = 75;
        b3.direction = 0;
        b3.speed = 60;
        b3.disappearY = -150;
        Bone b4 = new Bone();
        b4.y = gs.player.y;
        b4.x = -300;
        b4.width = 75;
        b4.height = 15;
        b4.direction = 1;
        b4.speed = 60;
        b4.disappearX = 750;
        gs.bones.add(b1);
        gs.bones.add(b2);
        gs.bones.add(b3);
        gs.bones.add(b4);
        }

    }
}
