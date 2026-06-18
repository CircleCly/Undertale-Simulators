package alpha.system;

import alpha.GameState;
import alpha.model.*;

public class ModeSystem {

    public static void redtify(GameState gs) {
        gs.player.soulMode = "Red";
        gs.player.speed = 9;
        restoreInitialBorder(gs);

    }

    public static void bluetify(GameState gs, int gDirection) {
        gs.player.soulMode = "Blue";
        gs.player.shield.activated = false;
        gs.player.speed = 9;
        gs.player.gDirection = gDirection;
        restoreInitialBorder(gs);

    }

    public static void greentify(GameState gs) {
        gs.player.soulMode = "Green";
        gs.player.speed = 0;
        gs.player.shield.activated = true;

        gs.player.x = 225;
        gs.player.y = 225;
        gs.moveBorder[0].x = 205;
        gs.moveBorder[0].y = 205;
        gs.moveBorder[0].width = 65;
        gs.moveBorder[0].updateHitbox();
        gs.moveBorder[1].x = 250;
        gs.moveBorder[1].y = 205;
        gs.moveBorder[1].height = 65;
        gs.moveBorder[1].updateHitbox();
        gs.moveBorder[2].x = 205;
        gs.moveBorder[2].y = 250;
        gs.moveBorder[2].width = 65;
        gs.moveBorder[2].updateHitbox();
        gs.moveBorder[3].x = 205;
        gs.moveBorder[3].y = 205;
        gs.moveBorder[3].height = 65;
        gs.moveBorder[3].updateHitbox();
        gs.player.updateHitbox();

    }

    public static void restoreInitialBorder(GameState gs) {
        gs.moveBorder[0].x = 0;
        gs.moveBorder[0].y = 0;
        gs.moveBorder[0].width = 500;
        gs.moveBorder[0].height = 20;
        gs.moveBorder[0].updateHitbox();
        gs.moveBorder[1].x = 480;
        gs.moveBorder[1].y = 0;
        gs.moveBorder[1].width = 20;
        gs.moveBorder[1].height = 500;
        gs.moveBorder[1].updateHitbox();
        gs.moveBorder[2].x = 0;
        gs.moveBorder[2].y = 480;
        gs.moveBorder[2].width = 500;
        gs.moveBorder[2].height = 20;
        gs.moveBorder[2].updateHitbox();
        gs.moveBorder[3].x = 0;
        gs.moveBorder[3].y = 0;
        gs.moveBorder[3].width = 20;
        gs.moveBorder[3].height = 500;
        gs.moveBorder[3].updateHitbox();
        gs.player.updateHitbox();

    }

    public static void gameRestart(GameState gs) {
        PhysicsSystem.setTeleportersState(gs, false);
        PhysicsSystem.setCoordinateSystemState(gs, false);

        gs.ticks = 0;
        gs.player.hp = gs.player.hpMax;
        gs.player.x = 250;
        gs.player.y = 250;
        gs.player.karma = 0;
        //去骨
        gs.bones.removeAllElements();
        //去矛
        gs.spears.removeAllElements();
        gs.warnings.removeAllElements();
        redtify(gs);
        gs.restart = false;
        gs.over = false;


    }
}
