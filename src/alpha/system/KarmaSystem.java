package alpha.system;

import alpha.GameState;
import alpha.model.*;

public class KarmaSystem {

    public static void karmaHpDecrease(GameState gs) {
        // TODO Auto-generated method stub
        if (gs.player.karma > 40) {
        gs.player.karma = 40;
        }
        if (gs.player.karma == 40 && gs.player.hp > 1) {
        gs.player.karma--;
        gs.player.hp--;
        }
        if (gs.player.karma >= 30 && gs.player.karma < 40 && gs.player.hp > 1 && gs.ticks % 4 == 0) {
        gs.player.karma--;
        gs.player.hp -= 1;
        } else if (gs.player.karma >= 20 && gs.player.karma < 30 && gs.ticks % 10 == 0 && gs.player.hp > 1) {
        gs.player.karma--;
        gs.player.hp -= 1;
        } else if (gs.player.karma >= 10 && gs.player.karma < 20 && gs.ticks % 30 == 0 && gs.player.hp > 1) {
        gs.player.karma--;
        gs.player.hp -= 1;
        } else if (gs.player.karma > 0 && gs.player.karma < 10 && gs.ticks % 60 == 0 && gs.player.hp > 1) {
        gs.player.karma--;
        gs.player.hp -= 1;
        }
        //		if (gs.player.karma > 0 && gs.player.hp <= 5) {
        //			gs.player.karma--;
        //		}

    }

    public static void healPlayer(GameState gs, int interval) {
        if (gs.ticks % interval == 0) {
        if (gs.player.hp < gs.player.hpMax && gs.player.hp > 0) {
        gs.player.hp++;
        }
        if (gs.player.karma > 0) {

        gs.player.karma--;
        }
        }


    }
}
