package alpha.render;

import javax.swing.ImageIcon;
import alpha.GameState;

public class SpriteLoader {
    public static void load(GameState gs) {
        gs.soulRed = new ImageIcon("src/alpha/imgs/soul_red.png");
        gs.soulBlue_down = new ImageIcon("src/alpha/imgs/soul_blue_down.png");
        gs.soulBlue_up = new ImageIcon("src/alpha/imgs/soul_blue_up.png");
        gs.soulBlue_left = new ImageIcon("src/alpha/imgs/soul_blue_left.png");
        gs.soulBlue_right = new ImageIcon("src/alpha/imgs/soul_blue_right.png");
        gs.soulGreen = new ImageIcon("src/alpha/imgs/soul_green.png");
    }
}
