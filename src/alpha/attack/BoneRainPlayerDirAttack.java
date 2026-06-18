package alpha.attack;

import alpha.GameState;

public class BoneRainPlayerDirAttack implements Attack {
    private final BoneRainAttack delegate0 = new BoneRainAttack(0);
    private final BoneRainAttack delegate1 = new BoneRainAttack(1);
    private final BoneRainAttack delegate2 = new BoneRainAttack(2);
    private final BoneRainAttack delegate3 = new BoneRainAttack(3);

    @Override
    public void tick(GameState gs) {
        int dir = 0;
        if (gs.player.up) {
            dir = 0;
        } else if (gs.player.right) {
            dir = 1;
        } else if (gs.player.down) {
            dir = 2;
        } else if (gs.player.left) {
            dir = 3;
        }
        switch (dir) {
            case 0: delegate0.tick(gs); break;
            case 1: delegate1.tick(gs); break;
            case 2: delegate2.tick(gs); break;
            case 3: delegate3.tick(gs); break;
        }
    }
}
