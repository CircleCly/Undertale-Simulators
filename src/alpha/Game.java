package alpha;

import alpha.model.*;
import alpha.model.enums.SoulMode;
import alpha.view.GameView;
import alpha.input.InputHandler;
import alpha.system.*;
import javax.swing.JFrame;
import java.awt.Toolkit;

public class Game implements Runnable {
    private final GameState gs = new GameState();
    private GameView view;
    private JFrame frame;
    private Thread gameThread;
    private volatile boolean running = false;

    public GameState getState() {
        return gs;
    }

    public void start() {
        // Load sprites
        alpha.render.SpriteLoader.load(gs);

        // Init arena bounds
        gs.bounds = new Teleporter[4];
        gs.bounds[0] = new Teleporter(20, 0, 460, 30, 0);
        gs.bounds[1] = new Teleporter(480, 20, 30, 460, 1);
        gs.bounds[2] = new Teleporter(20, 480, 460, 30, 2);
        gs.bounds[3] = new Teleporter(0, 20, 30, 460, 3);

        gs.moveBorder = new Bound[4];
        gs.moveBorder[0] = new Bound(0, 0, 500, 20);
        gs.moveBorder[1] = new Bound(480, 0, 20, 500);
        gs.moveBorder[2] = new Bound(0, 480, 500, 20);
        gs.moveBorder[3] = new Bound(0, 0, 20, 500);

        gs.cSystem = new CoordinateSystem();
        gs.player = new Soul(225, 225, 25, 25);
        gs.bones = new java.util.Vector<Bone>();
        gs.spears = new java.util.Vector<Spear>();
        gs.warnings = new java.util.Vector<Warning>();
        gs.platforms = new java.util.Vector<Platform>();

        // View
        view = new GameView(gs);
        gs.gp = null; // legacy field, not used in new GameView path

        // Frame
        frame = new JFrame();
        gs.frame = frame;
        frame.add(view);

        frame.setSize(900, 650);
        frame.setMinimumSize(new java.awt.Dimension(600, 620));
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("UNDERTALE");
        frame.setLocationRelativeTo(null);

        // Input
        frame.addKeyListener(new InputHandler(gs));

        // Start loop
        running = true;
        gameThread = new Thread(this);
        gs.t = gameThread;
        gameThread.start();

        gs.deltaX = (frame.getWidth() - 500) / 2;
        gs.deltaY = (frame.getHeight() - 500) / 2;
    }

    @Override
    public void run() {
        final int FPS = 60;
        final long frameTime = 1000 / FPS;
        while (running) {
            frame.setTitle("Undertale");

            if (gs.over && gs.restart) {
                gameRestart();
            }

            if (!gs.paused && !gs.over && !gs.win) {
                try {
                    Thread.sleep(frameTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                gs.player.saveLocation();
                MovementSystem.moveThePlayer(gs);
                PhysicsSystem.checkTeleport(gs);
                if ("Red".equals(gs.player.soulMode)) {
                    MovementSystem.holdsPlayerInBounds(gs);
                }
                PhysicsSystem.gravity(gs);

                gs.player.updateHitbox();

                CollisionSystem.checkHit(gs);
                MovementSystem.moveAttacks(gs);

                AttackScheduler.tick(gs);

                LifetimeSystem.warningDurationSubtract(gs);
                LifetimeSystem.boneDurationSubtract(gs);
                LifetimeSystem.checkBonesDisappear(gs);
                KarmaSystem.karmaHpDecrease(gs);

                gs.ticks += 1;

                gs.cSystem.delayDecrease();
                LifetimeSystem.drainInvincibility(gs);

                if (gs.player.hp <= 0) {
                    gs.over = true;
                }

                view.repaint();
            } else {
                // still repaint to show pause/game over screen
                view.repaint();
                try { Thread.sleep(16); } catch (InterruptedException ignored) {}
            }
        }
    }

    private void gameRestart() {
        ModeSystem.gameRestart(gs);
    }
}
