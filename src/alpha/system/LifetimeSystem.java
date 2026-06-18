package alpha.system;

import alpha.GameState;
import alpha.model.*;

public class LifetimeSystem {

    public static void drainInvincibility(GameState gs) {
        if (gs.player.invincibleFrames > 0) {
        gs.player.invincibleFrames--;
        }

    }

    public static void boneDurationSubtract(GameState gs) {
        for (int i = 0; i < gs.bones.size(); i++) {
        if (gs.bones.get(i).fadeOut) {
        gs.bones.get(i).duration--;
        }
        }

    }

    public static void warningDurationSubtract(GameState gs) {
        for (int i = 0; i < gs.warnings.size(); i++) {
        if (gs.warnings.get(i).duration > 0) {
        gs.warnings.get(i).duration--;
        }
        }

    }

    public static void checkBonesDisappear(GameState gs) {
        for (int i = 0; i < gs.bones.size(); i++) {
        if (gs.bones.get(i).duration <= 0 && gs.bones.get(i).fadeOut) {
        gs.bones.remove(i);
        if (gs.bones.size() == 0) {
        break;
        }
        } else if (!gs.bones.get(i).fadeOut
        && (gs.bones.get(i).direction == 3 && gs.bones.get(i).x <= gs.bones.get(i).disappearX
        || gs.bones.get(i).direction == 1 && gs.bones.get(i).x >= gs.bones.get(i).disappearX
        || gs.bones.get(i).direction == 2 && gs.bones.get(i).y >= gs.bones.get(i).disappearY
        || gs.bones.get(i).direction == 0
        && gs.bones.get(i).y <= gs.bones.get(i).disappearY)) {
        gs.bones.remove(i);
        }
        }
        for (int i = 0; i < gs.spears.size(); i++) {
        if ((gs.spears.get(i).direction == 3 && gs.spears.get(i).x <= gs.spears.get(i).disappearX
        || gs.spears.get(i).direction == 1 && gs.spears.get(i).x >= gs.spears.get(i).disappearX
        || gs.spears.get(i).direction == 2 && gs.spears.get(i).y >= gs.spears.get(i).disappearY
        || gs.spears.get(i).direction == 0
        && gs.spears.get(i).y <= gs.spears.get(i).disappearY)) {
        gs.spears.remove(i);
        }
        }

    }
}
