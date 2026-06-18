package alpha.view;

import alpha.GameState;
import javax.swing.JPanel;
import java.awt.Graphics;

public class GameView extends JPanel {
    private final GameState gs;

    public GameView(GameState gs) {
        this.gs = gs;
        setFocusable(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        alpha.render.Renderer.render(g, gs, this);
    }

    public GameState getGameState() {
        return gs;
    }
}
