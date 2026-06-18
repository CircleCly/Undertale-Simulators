package alpha.attack;

import alpha.GameState;

public class FunctionAttackSpawnerAttack implements Attack {
    @Override
    public void tick(GameState gs) {
        if (gs.cSystem != null) {
            gs.cSystem.createFunctionAttack(gs);
        }
    }
}
