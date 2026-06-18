package alpha.render;

import java.awt.*;
import javax.swing.*;
import alpha.GameState;
import alpha.model.*;
import alpha.attack.function.*;
import java.util.Vector;

public class Renderer {

    public static void drawBound(Graphics g, GameState gs, JPanel panel) {
        // draw the boundaries
        g.setColor(Color.WHITE);
        for (Bound bound : gs.moveBorder) {
            g.fillRect((int) bound.x + gs.deltaX, (int) bound.y + gs.deltaY, bound.width, bound.height);
        }

        for (Teleporter e : gs.bounds) {
            if (e.activated) {
                g.setColor(Color.BLUE);
                g.fillRect((int) (e.x + gs.deltaX), (int) (e.y + gs.deltaY), (int) e.width, (int) e.height);
            }
        }
    
    }

    public static void drawStatus(Graphics g, GameState gs, JPanel panel) {
        g.setColor(Color.YELLOW);
        g.setFont(FontRegistry.getFont("宋体", Font.BOLD, 20));

        //g.drawString("游戏时间:" + Math.floorDiv(gs.ticks, 30) + "", 200 + gs.deltaX, 520 + gs.deltaY);
        //g.drawString("1.通过↑↓←→控制SOUL", -500 + gs.deltaX, gs.deltaY);
        //g.drawString("2.躲避灰色方块", -500 + gs.deltaX, gs.deltaY + 25);
        //g.drawString("3.当生命值为0时，游戏结束", -500 + gs.deltaX, gs.deltaY + 50);
        //g.drawString("4. 蓝色的攻击-> 不要动  橙色攻击->动",-500 + gs.deltaX, gs.deltaY + 75);
        //g.drawString("5. 红色：正常 蓝色：重力 绿色：护盾", -500+gs.deltaX, gs.deltaY+100);
        //g.drawString("6. 遇到黄色的矛，不要格挡。",-500 + gs.deltaX, gs.deltaY + 125);
    
    }

    public static void drawSoul(Graphics g, GameState gs, JPanel panel) {
        if (gs.player.soulMode.equals("Red")) {
            if ((gs.ticks % 5 != 0 && gs.player.invincibleFrames > 0) || gs.player.invincibleFrames <= 0)
                g.drawImage(gs.soulRed.getImage(), (int) (gs.player.x + gs.deltaX), (int) (gs.player.y + gs.deltaY), (int) gs.player.width, (int) gs.player.height, panel);
        } else if (gs.player.soulMode.equals("Blue")) {
            if ((gs.ticks % 5 != 0 && gs.player.invincibleFrames > 0) || gs.player.invincibleFrames <= 0)
                switch (gs.player.gDirection) {
                    case 0:
                        g.drawImage(gs.soulBlue_up.getImage(), (int) (gs.player.x + gs.deltaX), (int) (gs.player.y + gs.deltaY), (int) gs.player.width, (int) gs.player.height, panel);
                        break;
                    case 1:
                        g.drawImage(gs.soulBlue_right.getImage(), (int) (gs.player.x + gs.deltaX), (int) (gs.player.y + gs.deltaY), (int) gs.player.width, (int) gs.player.height, panel);
                        break;
                    case 2:
                        g.drawImage(gs.soulBlue_down.getImage(), (int) (gs.player.x + gs.deltaX), (int) (gs.player.y + gs.deltaY), (int) gs.player.width, (int) gs.player.height, panel);
                        break;
                    case 3:
                        g.drawImage(gs.soulBlue_left.getImage(), (int) (gs.player.x + gs.deltaX), (int) (gs.player.y + gs.deltaY), (int) gs.player.width, (int) gs.player.height, panel);
                        break;
                }


        } else if (gs.player.soulMode.equals("Green")) {
            if ((gs.ticks % 5 != 0 && gs.player.invincibleFrames > 0) || gs.player.invincibleFrames <= 0)
                g.drawImage(gs.soulGreen.getImage(), (int) (gs.player.x + gs.deltaX), (int) (gs.player.y + gs.deltaY), (int) gs.player.width, (int) gs.player.height, panel);

            g.setColor(Color.CYAN);
            g.fillRect((int) (gs.player.shield.x + gs.deltaX), (int) (gs.player.shield.y + gs.deltaY), gs.player.shield.width, gs.player.shield.height);

        }

    
    }

    public static void drawHP(Graphics g, GameState gs, JPanel panel) {
        g.setColor(Color.YELLOW);
        g.fillRect(gs.deltaX + 300, 530 + gs.deltaY, gs.player.hp * 2, 35);
        g.setFont(FontRegistry.getFont("Monster Friend Back", Font.BOLD, 25));
        if (gs.player.karma == 0) {
            g.setColor(Color.WHITE);
        } else {
            g.setColor(Color.MAGENTA);
        }
        g.drawString("      " + gs.player.hp + " / " + gs.player.hpMax, 120 + gs.deltaX, 530 + gs.deltaY);
        g.setColor(Color.MAGENTA);
        g.fillRect(gs.deltaX + 300 + gs.player.hp * 2 - gs.player.karma * 2, 530 + gs.deltaY, gs.player.karma * 2,
            35);

    
    }

    public static void drawBones(Graphics g, GameState gs, JPanel panel, Vector<Bone> b) {

        for (int i = 0; i < b.size(); i++) {

            if (b.get(i).fadeOut) {
                Color color = new Color((int) (((double) (b.get(i).duration) / b.get(i).maxDuration) * 220) + 20, (int) (((double) (b.get(i).duration) / b.get(i).maxDuration) * 220) + 20, (int) (((double) (b.get(i).duration) / b.get(i).maxDuration) * 220) + 20);
                g.setColor(color);
            } else if (b.get(i).color.equals("White")) {

                g.setColor(Color.GRAY);
            } else if (b.get(i).color.equals("Blue")) {
                g.setColor(Color.CYAN);
            } else if (b.get(i).color.equals("Orange")) {
                g.setColor(Color.ORANGE);
            }
            g.fillRect((int) (b.get(i).x + gs.deltaX), (int) (b.get(i).y + gs.deltaY), (int) b.get(i).width, (int) b.get(i).height);

        }
    
    }

    public static void drawPlatform(Graphics g, GameState gs, JPanel panel) {
        for (Platform p : gs.platforms) {
            g.setColor(Color.GREEN);
            g.fillRect((int) (p.x + gs.deltaX), (int) (p.y + gs.deltaY), p.width, p.height);
        }
    
    }

    public static void drawWarnings(Graphics g, GameState gs, JPanel panel, Vector<Warning> w) {

        for (int i = 0; i < w.size(); i++) {
            g.setColor(new Color((int) (250 - (w.get(i).duration / (double) w.get(i).maxDuration) * 250), (int) ((w.get(i).duration / (double) w.get(i).maxDuration) * 250), 0));
            g.fillRect((int) (w.get(i).x + gs.deltaX), (int) (w.get(i).y + gs.deltaY), (int) w.get(i).width, (int) w.get(i).height);
        }
    
    }

    public static void drawGravityDirection(Graphics g, GameState gs, JPanel panel) {
        g.setFont(FontRegistry.getFont("宋体", Font.BOLD, 100));
        switch (gs.player.gDirection) {
            case 0:
                g.drawString("↑", -500 + gs.deltaX, 300 + gs.deltaY);
                break;
            case 1:
                g.drawString("→", -500 + gs.deltaX, 300 + gs.deltaY);
                break;
            case 2:
                g.drawString("↓", -500 + gs.deltaX, 300 + gs.deltaY);
                break;
            case 3:
                g.drawString("←", -500 + gs.deltaX, 300 + gs.deltaY);
                break;
        }

    
    }

    public static void drawGameStatus(Graphics g, GameState gs, JPanel panel) {
        g.setColor(Color.YELLOW);
        g.setFont(FontRegistry.getFont("Determination Sans", Font.BOLD, 40));
        if (gs.paused && !gs.over) {
            g.drawString("Press P to Continue", gs.deltaX + 75, -20 + gs.deltaY);
        } else if (!gs.paused && !gs.over) {
            g.drawString("Press P to Pause", gs.deltaX + 75, -20 + gs.deltaY);
        } else if (gs.over) {
            g.setColor(Color.WHITE);
            g.drawString("Press R to Restart", gs.deltaX + 75, -20 + gs.deltaY);

        }
    
    }

    public static void drawWin(Graphics g, GameState gs, JPanel panel) {

        g.setColor(Color.YELLOW);
        g.setFont(FontRegistry.getFont("宋体", Font.BOLD, 100));
        g.drawString("你赢了!!!!!!", gs.deltaX - 150, gs.deltaY + 450);

    
    }

    public static void drawCoordinateSystem(Graphics g, GameState gs, JPanel panel) {
        g.setColor(Color.GREEN);
        g.drawLine(0 + gs.deltaX, 250 + gs.deltaY, 500 + gs.deltaX, 250 + gs.deltaY);
        g.drawLine(250 + gs.deltaX, 0 + gs.deltaY, 250 + gs.deltaX, 500 + gs.deltaY);
        // Player coordinate and function equations are now drawn in HUD overlay
        // (see drawHUD), keeping world rendering clean
    }

    public static void drawFunctionAttacks(Graphics g, GameState gs, JPanel panel) {
        for (FunctionAttack a : gs.cSystem.functionAttacks) {
            if (a.active) {
                g.setColor(Color.RED);
                //将每一个平面直角坐标系点都转化为屏幕直角坐标系点
                int[] intxs = new int[50];
                int[] intys = new int[50];
                for (int i = 0; i < 50; i++) {
                    intys[i] = (int) (250 - a.ys[i] + gs.deltaY);
                    intxs[i] = (int) (250 + a.xs[i] + gs.deltaX);
                }
                Graphics2D graphics2d = (Graphics2D) g;
                // Note: when called from scaled world pass, caller should adjust stroke for scale.
                // When called standalone (legacy), use default stroke.
                g.drawPolyline(intxs, intys, 50);
            }
        }
    
    }

    public static void drawSpear(Graphics g, GameState gs, JPanel panel) {

        for (Spear s : gs.spears) {
            if (s.color.equals("Magenta")) {
                g.setColor(Color.MAGENTA);
            } else if (s.color.equals("Yellow")) {
                g.setColor(Color.YELLOW);
            }
            g.fillRect((int) (s.x + gs.deltaX), (int) (s.y + gs.deltaY), s.width, s.height);
        }

    
    }

    public static void render(Graphics g, GameState gs, JPanel panel) {
        int panelW = panel.getWidth();
        int panelH = panel.getHeight();
        if (panelW <= 0 || panelH <= 0) return;

        // Compute world-to-screen transform – arena 500x500 centered
        float scale = Math.min(panelW / 540f, panelH / 600f);
        if (scale < 0.3f) scale = 0.3f;
        float arenaW = 500 * scale;
        float arenaH = 500 * scale;
        float offsetX = (panelW - arenaW) / 2f;
        float offsetY = (panelH - arenaH) / 2f;

        // Fill background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, panelW, panelH);

        // ---- World pass (scaled) ----
        Graphics2D gWorld = (Graphics2D) g.create();
        try {
            gWorld.translate(offsetX, offsetY);
            gWorld.scale(scale, scale);

            int oldDeltaX = gs.deltaX;
            int oldDeltaY = gs.deltaY;
            gs.deltaX = 0;
            gs.deltaY = 0;

            drawBound(gWorld, gs, panel);
            // drawStatus is empty – skip
            if (gs.player.hp > 0) {
                drawSoul(gWorld, gs, panel);
            }
            drawWarnings(gWorld, gs, panel, gs.warnings);
            drawBones(gWorld, gs, panel, gs.bones);
            drawSpear(gWorld, gs, panel);

            if (gs.cSystem.activated) {
                drawCoordinateSystem(gWorld, gs, panel);
                // function attack polylines
                for (FunctionAttack a : gs.cSystem.functionAttacks) {
                    if (a.active) {
                        gWorld.setColor(Color.RED);
                        int[] intxs = new int[50];
                        int[] intys = new int[50];
                        for (int i = 0; i < 50; i++) {
                            intys[i] = (int) (250 - a.ys[i]);
                            intxs[i] = (int) (250 + a.xs[i]);
                        }
                        gWorld.setStroke(new BasicStroke(3f / scale));
                        gWorld.drawPolyline(intxs, intys, 50);
                        gWorld.setStroke(new BasicStroke(1f));
                    }
                }
            }
            // Gravity arrow – world-space, off-screen left per design
            if (gs.player.soulMode.equals("Blue")) {
                drawGravityDirection(gWorld, gs, panel);
            }

            gs.deltaX = oldDeltaX;
            gs.deltaY = oldDeltaY;
        } finally {
            gWorld.dispose();
        }

        // ---- HUD pass (unscaled, screen coordinates) ----
        // HP bar / text, pause/restart/win text, function equations overlay
        drawHUD(g, gs, panel, offsetX, offsetY, scale);
    }

    private static void drawHUD(Graphics g, GameState gs, JPanel panel, float offsetX, float offsetY, float scale) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            // HP bar – positioned below arena, scaled width to match arena
            int hpBarX = (int) offsetX;
            int hpBarY = (int) (offsetY + 500 * scale + 8);
            int hpBarW = (int) (500 * scale);
            int hpBarH = 22;

            // HP fill
            g2.setColor(Color.YELLOW);
            int hpW = (int) (hpBarW * gs.player.hp / (float) gs.player.hpMax);
            g2.fillRect(hpBarX, hpBarY, hpW, hpBarH);

            // Karma overlay
            if (gs.player.karma > 0) {
                g2.setColor(new Color(160, 0, 160));
                int karmaW = (int) (hpBarW * gs.player.karma / (float) gs.player.hpMax);
                int karmaX = hpBarX + hpW - karmaW;
                if (karmaX < hpBarX) karmaX = hpBarX;
                g2.fillRect(karmaX, hpBarY, karmaW, hpBarH);
            }

            // HP text – fixed screen-pixel size
            g2.setFont(FontRegistry.getFont("SansSerif", Font.BOLD, 16));
            g2.setColor(gs.player.karma == 0 ? Color.WHITE : new Color(255, 128, 255));
            String hpText = gs.player.hp + " / " + gs.player.hpMax;
            g2.drawString(hpText, hpBarX + 6, hpBarY + 16);

            // Game status – centered above arena
            g2.setFont(FontRegistry.getFont("SansSerif", Font.BOLD, 18));
            g2.setColor(Color.YELLOW);
            String status;
            if (gs.paused && !gs.over) status = "Press P to Continue";
            else if (!gs.paused && !gs.over) status = "Press P to Pause";
            else if (gs.over) status = "Press R to Restart";
            else status = "";
            if (!status.isEmpty()) {
                int sw = g2.getFontMetrics().stringWidth(status);
                int sx = (int) (offsetX + (500 * scale - sw) / 2);
                int sy = (int) (offsetY - 8);
                if (sy < 18) sy = 18;
                g2.drawString(status, sx, sy);
            }

            // Win text
            if (gs.win) {
                g2.setColor(Color.YELLOW);
                g2.setFont(FontRegistry.getFont("SansSerif", Font.BOLD, 48));
                String win = "You Win!";
                int sw = g2.getFontMetrics().stringWidth(win);
                g2.drawString(win, (int) (offsetX + (500 * scale - sw) / 2), (int) (offsetY + 500 * scale / 2));
            }

            // Function equations – top-left overlay
            if (gs.cSystem.activated && !gs.cSystem.functionAttacks.isEmpty()) {
                g2.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
                int y = 20;
                g2.setColor(new Color(0, 0, 0, 140));
                int maxW = 0;
                for (FunctionAttack f : gs.cSystem.functionAttacks) {
                    int w = g2.getFontMetrics().stringWidth(f.equation);
                    if (w > maxW) maxW = w;
                }
                if (maxW > 0) {
                    g2.fillRect(6, 6, maxW + 12, gs.cSystem.functionAttacks.size() * 16 + 8);
                }
                g2.setColor(Color.YELLOW);
                for (FunctionAttack f : gs.cSystem.functionAttacks) {
                    g2.drawString(f.equation, 12, y);
                    y += 16;
                }
                // Player coordinate
                g2.setColor(Color.GREEN);
                String coord = String.format("(%.0f, %.0f)", gs.player.x - 250, 250 - gs.player.y);
                g2.drawString(coord, 12, y + 4);
            }
        } finally {
            g2.dispose();
        }
    }

}
