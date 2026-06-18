package alpha.system;

import alpha.GameState;
import alpha.model.Platform;

public class MovementSystem {

    public static void moveAttacks(GameState gs) {
        for (int i = 0; i < gs.bones.size(); i++) {
            gs.bones.get(i).move();
        }
        for (int i = 0; i < gs.spears.size(); i++) {
            gs.spears.get(i).move();
        }
    }

    public static void movePlatforms(GameState gs) {
        for (Platform p : gs.platforms) {
            p.move();
        }
    }

    public static void movePlatform(GameState gs) {
        for (Platform p : gs.platforms) {
            p.move();
        }
    }

    public static void holdsPlayerInBounds(GameState gs) {
        if (gs.player.x < gs.moveBorder[3].x + gs.moveBorder[3].width) {
            gs.player.x = gs.moveBorder[3].x + gs.moveBorder[3].width;
        }
        if (gs.player.x > gs.moveBorder[1].x - gs.player.width) {
            gs.player.x = gs.moveBorder[1].x - gs.player.width;
        }
        if (gs.player.y < gs.moveBorder[0].y + gs.moveBorder[0].height) {
            gs.player.y = gs.moveBorder[0].y + gs.moveBorder[0].height;
        }
        if (gs.player.y > gs.moveBorder[2].y - gs.player.height) {
            gs.player.y = gs.moveBorder[2].y - gs.player.height;
        }
    }

    public static void moveThePlayer(GameState gs) {
        if (gs.player.up && !gs.player.hitbox.intersects(gs.moveBorder[0].hitbox)) {
            gs.player.y -= Math.ceil((double) gs.player.speed / 2);
            gs.player.directShield(0);
        }
        if (gs.player.down && !gs.player.hitbox.intersects(gs.moveBorder[2].hitbox)) {
            gs.player.y += Math.ceil((double) gs.player.speed / 2);
            gs.player.directShield(2);
        }
        if (gs.player.left && !gs.player.hitbox.intersects(gs.moveBorder[3].hitbox)) {
            gs.player.x -= Math.ceil((double) gs.player.speed / 2);
            gs.player.directShield(3);
        }
        if (gs.player.right && !gs.player.hitbox.intersects(gs.moveBorder[1].hitbox)) {
            gs.player.x += Math.ceil((double) gs.player.speed / 2);
            gs.player.directShield(1);
        }
    }
}
