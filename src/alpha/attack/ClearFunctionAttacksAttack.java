package alpha.attack;

import alpha.GameState;

public class ClearFunctionAttacksAttack implements Attack {
    @Override
    public void tick(GameState gs) {
        if (gs.cSystem != null && gs.cSystem.functionAttacks != null) {
            gs.cSystem.functionAttacks.removeAllElements();
        }
    }
}
