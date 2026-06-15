package alpha.system;

import alpha.GameState;
import alpha.Test;
import alpha.model.*;

public class PhysicsSystem {

    public static void gravity(GameState gs) {
        //如果玩家是蓝色模式
        if (gs.player.soulMode.equals("Blue")) {
        //并且玩家的重力已经让玩家运动到边界，那么就把玩家重力速度设置为0
        if (gs.player.gDirection == 0 && gs.player.hitbox.intersects(gs.moveBorder[0].hitbox)) {
        gs.player.gSpeed = 0;

        } else if (gs.player.gDirection == 2 && gs.player.hitbox.intersects(gs.moveBorder[2].hitbox)) {
        gs.player.gSpeed = 0;

        } else if (gs.player.gDirection == 3 && gs.player.hitbox.intersects(gs.moveBorder[3].hitbox)) {
        gs.player.gSpeed = 0;

        } else if (gs.player.gDirection == 1 && gs.player.hitbox.intersects(gs.moveBorder[1].hitbox)) {
        gs.player.gSpeed = 0;

        }
        //			for(Platform p:gs.platforms) {
        //				if(gs.player.hitbox.intersects(p.hitbox)) {
        //					gs.player.gSpeed=0;
        //				}
        //			}
        switch (gs.player.gDirection) {
        case 0:
        gs.player.y -= gs.player.gSpeed / 60;
        break;
        case 1:
        gs.player.x += gs.player.gSpeed / 60;
        break;
        case 2:
        gs.player.y += gs.player.gSpeed / 60;
        break;
        case 3:
        gs.player.x -= gs.player.gSpeed / 60;
        break;

        }

        gs.player.gSpeed += 2.6;


        }

    }

    public static void checkTeleport(GameState gs) {
        for (Teleporter e : gs.bounds) {
        if (e.hitbox.intersects(gs.player.hitbox) && e.activated) {
        e.transport();
        }
        }

    }

    public static void setTeleportersState(GameState gs, boolean value) {
        for (Teleporter t : gs.bounds) {
        t.activated = value;
        }

    }

    public static void setCoordinateSystemState(GameState gs, boolean value) {
        gs.cSystem.activated = value;
        if (!value) {
        gs.cSystem.functionAttacks.removeAllElements();
        }

    }
}
