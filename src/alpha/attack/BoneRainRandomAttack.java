package alpha.attack;

import alpha.GameState;

public class BoneRainRandomAttack implements Attack {
    private final BoneRainAttack delegate0 = new BoneRainAttack(0);
    private final BoneRainAttack delegate1 = new BoneRainAttack(1);
    private final BoneRainAttack delegate2 = new BoneRainAttack(2);
    private final BoneRainAttack delegate3 = new BoneRainAttack(3);

    @Override
    public void tick(GameState gs) {
        int dir = (int) (Math.random() * 4);
        switch (dir) {
            case 0: delegate0.tick(gs); break;
            case 1: delegate1.tick(gs); break;
            case 2: delegate2.tick(gs); break;
            case 3: delegate3.tick(gs); break;
        }
    }
}
