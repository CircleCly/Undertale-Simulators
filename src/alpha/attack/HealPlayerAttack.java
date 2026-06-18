package alpha.attack;

import alpha.GameState;
import alpha.system.KarmaSystem;

public class HealPlayerAttack implements Attack {
    private final int interval;

    public HealPlayerAttack(int interval) {
        this.interval = interval;
    }

    @Override
    public void tick(GameState gs) {
        KarmaSystem.healPlayer(gs, interval);
    }
}
