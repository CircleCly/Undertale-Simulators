package alpha.attack;

import alpha.GameState;
import alpha.model.*;
import java.util.Vector;
import java.util.Random;

public class BoneLazerAttack implements Attack {
    private final int direction;

    public BoneLazerAttack(int direction) {
        this.direction = direction;
    }

    // Legacy static entry point – remove after Phase 5 scheduler rewrite
    public static void execute(GameState gs, int direction) {
        new BoneLazerAttack(direction).tick(gs);
    }

    @Override
    public void tick(GameState gs) {

        Bone b1 = new Bone();// 锟斤拷锟脚的癸拷头
        Bone b2 = new Bone(); // 锟斤拷锟脚的癸拷头

        switch (direction) {
        case 0:
        // gs.player.x=285;
        // gs.player.y=215;
        b1.x = 0;
        b1.y = 0;
        b1.width = 500;
        b1.height = 40;
        b1.direction = 2;
        b1.speed = 7;
        b1.disappearY = 600;

        b2.x = 500;
        b2.y = 0;
        b2.width = 40;
        b2.height = 500;
        b2.direction = 3;
        b2.speed = 7;
        b2.disappearX = -100;
        break;
        case 1:
        // gs.player.x=285;
        // gs.player.y=285;
        b1.x = 0;
        b1.y = 500;
        b1.width = 500;
        b1.height = 40;
        b1.direction = 0;
        b1.speed = 7;
        b1.disappearY = -100;

        b2.x = 500;
        b2.y = 0;
        b2.width = 40;
        b2.height = 500;
        b2.direction = 3;
        b2.speed = 7;
        b2.disappearX = -100;
        break;
        case 2:
        // gs.player.x=215;
        // gs.player.y=285;
        b1.x = 0;
        b1.y = 500;
        b1.width = 500;
        b1.height = 40;
        b1.direction = 0;
        b1.speed = 7;
        b1.disappearY = -100;

        b2.x = 0;
        b2.y = 0;
        b2.width = 40;
        b2.height = 600;
        b2.direction = 1;
        b2.speed = 7;
        b2.disappearX = 600;
        break;
        case 3:
        // gs.player.x=215;
        // gs.player.y=215;
        b1.x = 0;
        b1.y = 0;
        b1.width = 500;
        b1.height = 40;
        b1.direction = 2;
        b1.speed = 7;
        b1.disappearY = 600;

        b2.x = 0;
        b2.y = 0;
        b2.width = 40;
        b2.height = 500;
        b2.direction = 1;
        b2.speed = 7;
        b2.disappearX = 600;
        break;
        }
        gs.bones.add(b1);
        gs.bones.add(b2);

    }
}
