package alpha.attack;

import alpha.GameState;

public class ClearWarningsAttack implements Attack {
    @Override
    public void tick(GameState gs) {
        gs.warnings.removeAllElements();
    }
}
