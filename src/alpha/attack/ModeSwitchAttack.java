package alpha.attack;

import alpha.GameState;
import alpha.model.enums.SoulMode;
import alpha.system.ModeSystem;

public class ModeSwitchAttack implements Attack {
    private final SoulMode mode;
    private final int gDirection;

    public ModeSwitchAttack(SoulMode mode) {
        this(mode, 0);
    }

    public ModeSwitchAttack(SoulMode mode, int gDirection) {
        this.mode = mode;
        this.gDirection = gDirection;
    }

    @Override
    public void tick(GameState gs) {
        switch (mode) {
            case RED:
                ModeSystem.redtify(gs);
                break;
            case BLUE:
                ModeSystem.bluetify(gs, gDirection);
                break;
            case GREEN:
                ModeSystem.greentify(gs);
                break;
        }
    }
}
