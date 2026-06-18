package alpha.attack;

import alpha.GameState;

public class BoneSpikeRandomAttack implements Attack {
    private final BoneSpikeAttack delegate0 = new BoneSpikeAttack(0);
    private final BoneSpikeAttack delegate1 = new BoneSpikeAttack(1);
    private final BoneSpikeAttack delegate2 = new BoneSpikeAttack(2);
    private final BoneSpikeAttack delegate3 = new BoneSpikeAttack(3);

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
