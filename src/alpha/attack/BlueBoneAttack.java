package alpha.attack;

import alpha.GameState;
import alpha.model.*;
import java.util.Vector;
import java.util.Random;

public class BlueBoneAttack implements Attack {
    public BlueBoneAttack() {}

    // Legacy static entry point – remove after Phase 5 scheduler rewrite
    public static void execute(GameState gs) {
        new BlueBoneAttack().tick(gs);
    }

    @Override
    public void tick(GameState gs) {



        if (gs.ticks > 22000 && gs.ticks <= 22180) {
        if (gs.ticks % 60 == 30) {
        //长蓝色骨头
        Bone b1 = new Bone();
        b1.color = "Blue";
        b1.x = 600;
        b1.y = 200;
        b1.width = 16;
        b1.height = 300;
        b1.speed = 20;
        b1.direction = 3;
        b1.disappearX = -100;
        gs.bones.add(b1);
        } else if (gs.ticks % 60 == 0) {
        //白色短骨头
        Bone b2 = new Bone();
        b2.color = "White";
        b2.x = 600;
        b2.y = 450;
        b2.width = 16;
        b2.height = 50;
        b2.speed = 20;
        b2.direction = 3;
        b2.disappearX = -100;
        gs.bones.add(b2);
        }
        } else if (gs.ticks > 22240 && gs.ticks <= 22420) {
        if (gs.ticks % 60 == 30) {
        //长蓝色骨头
        Bone b1 = new Bone();
        b1.color = "Blue";
        b1.x = -100;
        b1.y = 200;
        b1.width = 16;
        b1.height = 300;
        b1.speed = 20;
        b1.direction = 1;
        b1.disappearX = 600;
        gs.bones.add(b1);
        } else if (gs.ticks % 60 == 0) {
        //白色短骨头
        Bone b2 = new Bone();
        b2.color = "White";
        b2.x = -100;
        b2.y = 450;
        b2.width = 16;
        b2.height = 50;
        b2.speed = 20;
        b2.direction = 1;
        b2.disappearX = 600;
        gs.bones.add(b2);
        }
        }



    }
}
