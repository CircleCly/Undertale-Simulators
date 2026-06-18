package alpha.attack;

import alpha.GameState;

public class FoldBonesRandomAttack implements Attack {
    private final FoldBonesAttack delegate0 = new FoldBonesAttack(0);
    private final FoldBonesAttack delegate1 = new FoldBonesAttack(1);

    @Override
    public void tick(GameState gs) {
        if (Math.random() < 0.5) {
            delegate0.tick(gs);
        } else {
            delegate1.tick(gs);
        }
    }
}
