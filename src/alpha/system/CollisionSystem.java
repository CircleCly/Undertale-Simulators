package alpha.system;

import alpha.GameState;
import alpha.Test;
import alpha.model.*;

public class CollisionSystem {

    public static void checkHit(GameState gs) {
        // Check hit
        for (int i = 0; i < gs.bones.size(); i++) {

        if (gs.player.hitbox.intersects(gs.bones.get(i).hitbox) && gs.player.invincibleFrames <= 0) {
        if (gs.bones.get(i).color.equals("White") || (gs.bones.get(i).color.equals("Blue") && gs.player.isMoving()) || (gs.bones.get(i).color.equals("Orange") && !gs.player.isMoving())) {
        gs.player.hp--;
        gs.player.invincibleFrames += 2;
        if (gs.player.karma == 0) {
        gs.player.karma += 6;
        } else {
        gs.player.karma++;
        }

        }
        if (gs.player.karma > gs.player.hp) {
        gs.player.karma = gs.player.hp;
        }
        }
        }
        if (gs.cSystem.checkIfPlayerHit()) {

        if (gs.player.invincibleFrames <= 0) {
        gs.player.hp /= 2;
        gs.player.invincibleFrames = 120;
        }

        }
        for (int i = 0; i < gs.spears.size(); i++) {
        //System.out.println(gs.player.hitbox.x+" ,"+gs.player.hitbox.y);
        Spear spear = gs.spears.get(i);
        if (spear.color.equals("Magenta")) {


        if (gs.player.hitbox.intersects(spear.hitbox)) {
        if (gs.player.invincibleFrames <= 0) {
        gs.player.hp -= gs.spears.get(i).damage;
        gs.player.invincibleFrames = 40;
        }
        gs.spears.remove(i);
        continue;
        }

        } else {

        }
        }
        for (int i = 0; i < gs.spears.size(); i++) {
        if (gs.player.shield.activated && gs.player.shield.hitbox.intersects(gs.spears.get(i).hitbox)) {
        if (gs.spears.get(i).color.equals("Magenta")) {
        gs.spears.remove(i);
        continue;
        } else {
        if (gs.player.invincibleFrames <= 0) {
        gs.player.hp -= gs.spears.get(i).damage;
        gs.player.invincibleFrames = 20;
        }
        gs.spears.remove(i);
        continue;
        }
        }
        }

    }
}
