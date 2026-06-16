package alpha.attack;

import alpha.GameState;
import alpha.model.*;
import java.util.Vector;
import java.util.Random;

public class PlatformOneAttack {
    public static void execute(GameState gs) {

        Platform platform = new Platform();
        platform.direction = 3;
        platform.x = 600;
        platform.y = 150;
        platform.speed = 6;
        gs.platforms.add(platform);


    }
}
