package alpha.attack;

import alpha.GameState;

public class BoneLazerRandomAttack implements Attack {
    private final BoneLazerAttack delegate0 = new BoneLazerAttack(0);
    private final BoneLazerAttack delegate1 = new BoneLazerAttack(1);
    private final BoneLazerAttack delegate2 = new BoneLazerAttack(2);
    private final BoneLazerAttack delegate3 = new BoneLazerAttack(3);

    @Override
    public void tick(GameState gs) {
        if (gs.ticks % 240 == 0) {
            int dir = (int) (Math.random() * 4);
            switch (dir) {
                case 0: delegate0.tick(gs); break;
                case 1: delegate1.tick(gs); break;
                case 2: delegate2.tick(gs); break;
                case 3: delegate3.tick(gs); break;
            }
        }
    }
}
