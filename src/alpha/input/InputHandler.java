package alpha.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import alpha.GameState;

public class InputHandler implements KeyListener {

    private final GameState gs;

    public InputHandler(GameState gs) {
        this.gs = gs;
    }

    @Override
    public void keyPressed(KeyEvent arg0) {
        if (arg0.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            gs.ticks += 300;
        }
        if (arg0.getKeyCode() == KeyEvent.VK_P) {
            gs.paused = !gs.paused;
        }
        if (arg0.getKeyCode() == KeyEvent.VK_UP && (gs.player.gDirection != 0 || !gs.player.soulMode.equals("Blue"))) {
            gs.player.up = true;
        }
        if (arg0.getKeyCode() == KeyEvent.VK_RIGHT && (gs.player.gDirection != 1 || !gs.player.soulMode.equals("Blue"))) {
            gs.player.right = true;
        }
        if (arg0.getKeyCode() == KeyEvent.VK_DOWN && (gs.player.gDirection != 2 || !gs.player.soulMode.equals("Blue"))) {
            gs.player.down = true;
        }
        if (arg0.getKeyCode() == KeyEvent.VK_LEFT && (gs.player.gDirection != 3 || !gs.player.soulMode.equals("Blue"))) {
            gs.player.left = true;
        }
        if (gs.over && arg0.getKeyCode() == KeyEvent.VK_R) {
            gs.restart = true;
        }
        if (arg0.getKeyCode() == KeyEvent.VK_F8) {
            gs.player.hp = gs.player.hpMax;
        }
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
        if (arg0.getKeyCode() == KeyEvent.VK_UP) {
            gs.player.up = false;
        } else if (arg0.getKeyCode() == KeyEvent.VK_RIGHT) {
            gs.player.right = false;
        } else if (arg0.getKeyCode() == KeyEvent.VK_DOWN) {
            gs.player.down = false;
        } else if (arg0.getKeyCode() == KeyEvent.VK_LEFT) {
            gs.player.left = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub
    }
}
