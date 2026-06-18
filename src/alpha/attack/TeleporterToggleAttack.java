package alpha.attack;

import alpha.GameState;
import alpha.system.PhysicsSystem;

public class TeleporterToggleAttack implements Attack {
    private final boolean enabled;

    public TeleporterToggleAttack(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void tick(GameState gs) {
        PhysicsSystem.setTeleportersState(gs, enabled);
    }
}
