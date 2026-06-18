package alpha.attack;

import alpha.GameState;
import alpha.model.*;
import java.util.Vector;
import java.util.Random;

public class FoldBonesAttack implements Attack {
    private final int direction;

    public FoldBonesAttack(int direction) {
        this.direction = direction;
    }

    // Legacy static entry point – remove after Phase 5 scheduler rewrite
    public static void execute(GameState gs, int direction) {
        new FoldBonesAttack(direction).tick(gs);
    }

    @Override
    public void tick(GameState gs) {

        if (gs.ticks % 55 == 0) {

        if (direction == 0) {
        Bone b1 = new Bone();
        b1.y = -150;
        b1.x = 0;
        int spacePosition = (int) (Math.random() * 200 + 150);
        b1.width = spacePosition;
        b1.height = 25;
        b1.direction = 2;
        b1.disappearY = 700;
        b1.speed = 14;
        Bone b2 = new Bone();
        b2.y = -150;
        b2.x = spacePosition + 75;
        b2.width = (int) (500 - b2.x);
        b2.height = 25;
        b2.direction = 2;
        b2.disappearY = 700;
        b2.speed = 14;
        Bone b3 = new Bone();
        b3.y = 650;
        b3.x = 0;
        b3.width = spacePosition;
        b3.height = 25;
        b3.direction = 0;
        b3.disappearY = -200;
        b3.speed = 14;
        Bone b4 = new Bone();
        b4.y = 650;
        b4.x = spacePosition + 75;
        b4.width = (int) (500 - b2.x);
        b4.height = 25;
        b4.direction = 0;
        b4.disappearY = -200;
        b4.speed = 14;
        gs.bones.add(b1);
        gs.bones.add(b2);
        gs.bones.add(b3);
        gs.bones.add(b4);
        } else {
        Bone b1 = new Bone();
        b1.y = 0;
        b1.x = -150;
        int spacePosition = (int) (Math.random() * 200 + 150);
        b1.width = 25;
        b1.height = spacePosition;
        b1.direction = 1;
        b1.disappearX = 700;
        b1.speed = 14;
        Bone b2 = new Bone();
        b2.y = spacePosition + 75;
        b2.x = -150;
        b2.height = (int) (500 - b2.x);
        b2.width = 25;
        b2.direction = 1;
        b2.disappearX = 700;
        b2.speed = 14;
        Bone b3 = new Bone();
        b3.x = 650;
        b3.y = 0;
        b3.height = spacePosition;
        b3.width = 25;
        b3.direction = 3;
        b3.disappearX = -200;
        b3.speed = 14;
        Bone b4 = new Bone();
        b4.x = 650;
        b4.y = spacePosition + 75;
        b4.height = (int) (500 - b2.x);
        b4.width = 25;
        b4.direction = 3;
        b4.disappearX = -200;
        b4.speed = 14;
        gs.bones.add(b1);
        gs.bones.add(b2);
        gs.bones.add(b3);
        gs.bones.add(b4);
        }
        }

    }
}
