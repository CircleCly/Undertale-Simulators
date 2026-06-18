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
        g.setColor(Color.YELLOW);
        g.drawString("(" + (int) (gs.player.x - 250) + "," + (int) (250 - gs.player.y) + ")", -350 + gs.deltaX, 160 + gs.deltaY);
        for (int i = 0; i < gs.cSystem.functionAttacks.size(); i++) {
            FunctionAttack f = gs.cSystem.functionAttacks.get(i);
            g.drawString(f.equation, -700 + gs.deltaX, 200 + 40 * i + gs.deltaY);

        }
    
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
                graphics2d.setStroke(new BasicStroke(3));
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

        Graphics2D g2 = (Graphics2D) g.create();
        try {
            // Scale arena + UI to fit window, keep aspect, center
            // Logical canvas: arena 500x500, with ~20px margins, HP bar at y=530..565
            // Total logical size ~540x600
            float scale = Math.min(panelW / 540f, panelH / 600f);
            if (scale < 0.3f) scale = 0.3f; // prevent tiny scale
            float arenaW = 500 * scale;
            float arenaH = 500 * scale;
            float offsetX = (panelW - arenaW) / 2f;
            float offsetY = (panelH - arenaH) / 2f;

            // Fill background
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, panelW, panelH);

            g2.translate(offsetX, offsetY);
            g2.scale(scale, scale);

            // Temporarily set delta to 0 so existing draw code uses world coords directly
            int oldDeltaX = gs.deltaX;
            int oldDeltaY = gs.deltaY;
            gs.deltaX = 0;
            gs.deltaY = 0;

            drawBound(g2, gs, panel);
            drawStatus(g2, gs, panel);

            if (gs.player.hp > 0) {
                drawSoul(g2, gs, panel);
                drawHP(g2, gs, panel);
            }
            drawWarnings(g2, gs, panel, gs.warnings);
            drawBones(g2, gs, panel, gs.bones);
            drawSpear(g2, gs, panel);

            drawGameStatus(g2, gs, panel);
            if (gs.win) {
                drawWin(g2, gs, panel);
            }
            if (gs.cSystem.activated) {
                drawCoordinateSystem(g2, gs, panel);
                for (FunctionAttack a : gs.cSystem.functionAttacks) {
                    if (a.active) {
                        g2.setColor(Color.RED);
                        int[] intxs = new int[50];
                        int[] intys = new int[50];
                        for (int i = 0; i < 50; i++) {
                            intys[i] = (int) (250 - a.ys[i]);
                            intxs[i] = (int) (250 + a.xs[i]);
                        }
                        g2.setStroke(new BasicStroke(3f / scale)); // keep line width constant in screen pixels
                        g2.drawPolyline(intxs, intys, 50);
                        g2.setStroke(new BasicStroke(1f));
                    }
                }
            }
            if (gs.player.soulMode.equals("Blue")) {
                drawGravityDirection(g2, gs, panel);
            }

            gs.deltaX = oldDeltaX;
            gs.deltaY = oldDeltaY;
        } finally {
            g2.dispose();
        }
    }

}
