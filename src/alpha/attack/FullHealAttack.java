package alpha.attack;

import alpha.GameState;

public class FullHealAttack implements Attack {
    @Override
    public void tick(GameState gs) {
        gs.player.hp = gs.player.hpMax;
    }
}
