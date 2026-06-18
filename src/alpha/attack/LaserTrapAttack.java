package alpha.attack;

import alpha.GameState;
import alpha.model.*;
import java.util.Vector;
import java.util.Random;

public class LaserTrapAttack implements Attack {
    public LaserTrapAttack() {}

    // Legacy static entry point – remove after Phase 5 scheduler rewrite
    public static void execute(GameState gs) {
        new LaserTrapAttack().tick(gs);
    }

    @Override
    public void tick(GameState gs) {

        if (gs.ticks % 55 == 0) {
        for (int i = 1; i <= 2; i++) {
        Warning w1 = new Warning();
        w1.x = (int) (Math.random() * 500);
        w1.y = (int) (Math.random() * 500);
        w1.duration = 60;
        w1.maxDuration = 60;
        w1.width = 30;
        w1.height = 30;
        gs.warnings.add(w1);
        }
        }
        for (int i = 0; i < gs.warnings.size(); i++) {
        Warning warning = gs.warnings.get(i);
        if (warning.duration <= 0) {
        gs.warnings.remove(i);
        Bone b1 = new Bone();
        Bone b = new Bone();// 锟斤拷锟脚碉拷
        b1.x = warning.x;
        b1.y = 0;
        b1.width = 15;
        b1.height = 500;
        b1.maxDuration = 75;
        b1.duration = 75;
        b1.speed = 0;
        b1.fadeOut = true;
        b1.direction = 1;
        gs.bones.add(b1);

        b.x = 0;
        b.y = warning.y;
        b.width = 500;
        b.height = 15;
        b.maxDuration = 75;
        b.duration = 75;
        b.speed = 0;
        b.fadeOut = true;
        b.direction = 0;

        gs.bones.add(b);
        }
        }



    }
}
