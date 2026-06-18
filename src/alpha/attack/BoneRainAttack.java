package alpha.attack;

import alpha.GameState;
import alpha.model.*;
import java.util.Vector;
import java.util.Random;

public class BoneRainAttack implements Attack {
    private final int direction;

    public BoneRainAttack(int direction) {
        this.direction = direction;
    }

    // Legacy static entry point – remove after Phase 5 scheduler rewrite
    public static void execute(GameState gs, int direction) {
        new BoneRainAttack(direction).tick(gs);
    }

    @Override
    public void tick(GameState gs) {


        if (gs.ticks % 10 == 0) {
        Bone b = new Bone();
        switch (direction) {
        case 0:
        b.y = 550;
        b.x = (int) (Math.random() * 500);
        b.width = 50;
        b.height = 50;
        b.speed = 11;
        b.direction = 0;
        b.disappearY = -100;

        break;
        case 1:
        b.y = (int) (Math.random() * 500);
        b.x = -50;
        b.width = 50;
        b.height = 50;
        b.speed = 11;
        b.direction = 1;
        b.disappearX = 650;

        break;
        case 2:
        b.y = -50;
        b.x = (int) (Math.random() * 500);
        b.width = 50;
        b.height = 50;
        b.speed = 11;
        b.direction = 2;
        b.disappearY = 650;

        break;
        case 3:
        b.y = (int) (Math.random() * 500);
        b.x = 550;
        b.width = 50;
        b.height = 50;
        b.speed = 11;
        b.direction = 3;
        b.disappearX = -100;

        break;
        }

        gs.bones.add(b);
        }


    }
}
