package alpha.attack;

import alpha.GameState;
import alpha.system.PhysicsSystem;

public class CoordinateSystemToggleAttack implements Attack {
    private final boolean enabled;

    public CoordinateSystemToggleAttack(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void tick(GameState gs) {
        PhysicsSystem.setCoordinateSystemState(gs, enabled);
    }
}
