package alpha;

import javax.swing.*;
import java.util.Vector;
import alpha.model.*;
import alpha.model.enums.*;

public class GameState {
    public JFrame frame;
    public CoordinateSystem cSystem;
    public Soul player;
    public GamePanel gp;
    public float ticks = 0;
    public Vector<Bone> bones;
    public Vector<Spear> spears;
    public Vector<Platform> platforms;
    public Vector<Warning> warnings;
    public int deltaX;
    public int deltaY;
    public boolean paused = true;
    public boolean over = false;
    public boolean win = false;
    public boolean restart = false;
    public Teleporter[] bounds;
    public Bound[] moveBorder;
    public Thread t;
    public static final int fps = 60;
    public ImageIcon soulRed, soulBlue_down, soulBlue_up, soulBlue_left, soulBlue_right, soulGreen;

    public GameState() {
        // fields initialized by Test constructor historically; keep defaults here
    }
}
