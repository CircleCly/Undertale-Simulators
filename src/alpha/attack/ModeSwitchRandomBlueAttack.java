package alpha.attack;

import alpha.GameState;
import alpha.model.enums.SoulMode;

public class ModeSwitchRandomBlueAttack implements Attack {
    private final ModeSwitchAttack delegate0 = new ModeSwitchAttack(SoulMode.BLUE, 0);
    private final ModeSwitchAttack delegate1 = new ModeSwitchAttack(SoulMode.BLUE, 1);
    private final ModeSwitchAttack delegate2 = new ModeSwitchAttack(SoulMode.BLUE, 2);
    private final ModeSwitchAttack delegate3 = new ModeSwitchAttack(SoulMode.BLUE, 3);

    @Override
    public void tick(GameState gs) {
        // Original: if (gs.ticks % 60 == 0) { gp.bluetify(random) }
        // ModeSwitchAttack.tick() calls bluetify every tick, which would be wrong.
        // So we need to replicate the 60-tick gating here.
        if (gs.ticks % 60 == 0) {
            int dir = (int) (Math.random() * 4);
            switch (dir) {
                case 0: delegate0.tick(gs); break;
                case 1: delegate1.tick(gs); break;
                case 2: delegate2.tick(gs); break;
                case 3: delegate3.tick(gs); break;
            }
        }
    }
}
