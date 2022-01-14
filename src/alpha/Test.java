package alpha;

/**
 * @author air
 * Version: Alpha 1.1
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.math.BigDecimal;
import java.math.MathContext;

import java.util.*;

public class Test {
    static JFrame frame;
    static CoordinateSystem cSystem;
    static Soul player;
    static GamePanel gp;
    static float ticks = 0;
    static Vector<Bone> bones;
    static Vector<Spear> spears;
    static Vector<Platform> platforms;
    static Vector<Warning> warnings;
    static int deltaX;
    static int deltaY;
    static boolean paused = true;
    static boolean over = false;
    static boolean win = false;
    static boolean restart = false;
    static Teleporter[] bounds;
    static Bound[] moveBorder;
    static Thread t;
    static final int fps = 60;
    static ImageIcon soulRed, soulBlue_down, soulBlue_up, soulBlue_left, soulBlue_right, soulGreen;

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        new Test();
    }

    public Test() {
        soulRed = new ImageIcon("src/alpha/imgs/soul_red.png");
        soulBlue_down = new ImageIcon("src/alpha/imgs/soul_blue_down.png");
        soulBlue_up = new ImageIcon("src/alpha/imgs/soul_blue_up.png");
        soulBlue_left = new ImageIcon("src/alpha/imgs/soul_blue_left.png");
        soulBlue_right = new ImageIcon("src/alpha/imgs/soul_blue_right.png");
        soulGreen = new ImageIcon("src/alpha/imgs/soul_green.png");
        bounds = new Teleporter[4];
        bounds[0] = new Teleporter(20, 0, 460, 30, 0);
        bounds[1] = new Teleporter(480, 20, 30, 460, 1);
        bounds[2] = new Teleporter(20, 480, 460, 30, 2);
        bounds[3] = new Teleporter(0, 20, 30, 460, 3);
        moveBorder = new Bound[4];
        moveBorder[0] = new Bound(0, 0, 500, 20);
        moveBorder[1] = new Bound(480, 0, 20, 500);
        moveBorder[2] = new Bound(0, 480, 500, 20);
        moveBorder[3] = new Bound(0, 0, 20, 500);
        cSystem = new CoordinateSystem();
        frame = new JFrame();
        gp = new GamePanel();
        player = new Soul(225, 225, 25, 25);
        bones = new Vector<Bone>();
        spears = new Vector<Spear>();
        warnings = new Vector<Warning>();
        platforms = new Vector<Platform>();
        frame.add(gp);

        Toolkit tk = Toolkit.getDefaultToolkit();

        frame.setSize(tk.getScreenSize().width, tk.getScreenSize().height);
        frame.setVisible(true);
        frame.setFocusable(true);

        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        if (frame.isFocused()) {
            frame.setTitle("UNDERTALE");
        } else {
            frame.setTitle("UNDERTALE: CONCENTRATE!");
        }
        frame.addKeyListener(gp);
        t = new Thread(gp);
        t.start();

        deltaX = (Test.frame.getWidth() - 500) / 2;
        deltaY = (Test.frame.getHeight() - 500) / 2;
    }
}

class GamePanel extends JPanel implements Runnable, KeyListener {
    public void paint(Graphics g) {
        g.fillRect(0, 0, Test.frame.getWidth(), Test.frame.getHeight());
        this.drawBound(g);
        this.drawStatus(g);

        if (Test.player.hp > 0) {
            this.drawSoul(g);
            this.drawHP(g);
        }
        this.drawWarnings(Test.warnings, g);
        this.drawBones(Test.bones, g);
        this.drawSpear(g);

        this.drawGameStatus(g);
        if (Test.win) {
            this.drawWin(g);
        }
        if (Test.cSystem.activated) {
            this.drawCoordinateSystem(g);
            // Draw All functions
            for (FunctionAttack a : Test.cSystem.functionAttacks) {
                if (a.active) {
                    g.setColor(Color.RED);
                    //将每一个平面直角坐标系点都转化为屏幕直角坐标系点
                    int[] intxs = new int[50];
                    int[] intys = new int[50];
                    for (int i = 0; i < 50; i++) {
                        intys[i] = (int) (250 - a.ys[i] + Test.deltaY);
                        intxs[i] = (int) (250 + a.xs[i] + Test.deltaX);
                    }
                    g.drawPolyline(intxs, intys, 50);
                }
            }
        }
        if (Test.player.soulMode.equals("Blue")) {
            this.drawGravityDirection(g);
        }
    }

    public void drawBound(Graphics g) {
        // draw the boundaries
        g.setColor(Color.WHITE);
        for (Bound bound : Test.moveBorder) {
            g.fillRect((int) bound.x + Test.deltaX, (int) bound.y + Test.deltaY, bound.width, bound.height);
        }

        for (Teleporter e : Test.bounds) {
            if (e.activated) {
                g.setColor(Color.BLUE);
                g.fillRect((int) (e.x + Test.deltaX), (int) (e.y + Test.deltaY), (int) e.width, (int) e.height);
            }
        }
    }

    public void drawSoul(Graphics g) {
        if (Test.player.soulMode.equals("Red")) {
            if ((Test.ticks % 5 != 0 && Test.player.invincibleFrames > 0) || Test.player.invincibleFrames <= 0)
                g.drawImage(Test.soulRed.getImage(), (int) (Test.player.x + Test.deltaX), (int) (Test.player.y + Test.deltaY), (int) Test.player.width, (int) Test.player.height, this);
        } else if (Test.player.soulMode.equals("Blue")) {
            if ((Test.ticks % 5 != 0 && Test.player.invincibleFrames > 0) || Test.player.invincibleFrames <= 0)
                switch (Test.player.gDirection) {
                    case 0:
                        g.drawImage(Test.soulBlue_up.getImage(), (int) (Test.player.x + Test.deltaX), (int) (Test.player.y + Test.deltaY), (int) Test.player.width, (int) Test.player.height, this);
                        break;
                    case 1:
                        g.drawImage(Test.soulBlue_right.getImage(), (int) (Test.player.x + Test.deltaX), (int) (Test.player.y + Test.deltaY), (int) Test.player.width, (int) Test.player.height, this);
                        break;
                    case 2:
                        g.drawImage(Test.soulBlue_down.getImage(), (int) (Test.player.x + Test.deltaX), (int) (Test.player.y + Test.deltaY), (int) Test.player.width, (int) Test.player.height, this);
                        break;
                    case 3:
                        g.drawImage(Test.soulBlue_left.getImage(), (int) (Test.player.x + Test.deltaX), (int) (Test.player.y + Test.deltaY), (int) Test.player.width, (int) Test.player.height, this);
                        break;
                }


        } else if (Test.player.soulMode.equals("Green")) {
            if ((Test.ticks % 5 != 0 && Test.player.invincibleFrames > 0) || Test.player.invincibleFrames <= 0)
                g.drawImage(Test.soulGreen.getImage(), (int) (Test.player.x + Test.deltaX), (int) (Test.player.y + Test.deltaY), (int) Test.player.width, (int) Test.player.height, this);

            g.setColor(Color.CYAN);
            g.fillRect((int) (Test.player.shield.x + Test.deltaX), (int) (Test.player.shield.y + Test.deltaY), Test.player.shield.width, Test.player.shield.height);

        }

    }

    public void drawHP(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(Test.deltaX + 300, 530 + Test.deltaY, Test.player.hp * 2, 35);
        g.setFont(new Font("Monster Friend Back", Font.BOLD, 25));
        if (Test.player.karma == 0) {
            g.setColor(Color.WHITE);
        } else {
            g.setColor(Color.MAGENTA);
        }
        g.drawString("      " + Test.player.hp + "  /", 120 + Test.deltaX, 530 + Test.deltaY);
        g.setColor(Color.MAGENTA);
        g.fillRect(Test.deltaX + 300 + Test.player.hp * 2 - Test.player.karma * 2, 530 + Test.deltaY, Test.player.karma * 2,
            35);

    }

    public void drawBones(Vector<Bone> b, Graphics g) {

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
            g.fillRect((int) (b.get(i).x + Test.deltaX), (int) (b.get(i).y + Test.deltaY), (int) b.get(i).width, (int) b.get(i).height);

        }
    }

    public void drawPlatform(Graphics g) {
        for (Platform p : Test.platforms) {
            g.setColor(Color.GREEN);
            g.fillRect((int) (p.x + Test.deltaX), (int) (p.y + Test.deltaY), p.width, p.height);
        }
    }

    public void drawWarnings(Vector<Warning> w, Graphics g) {

        for (int i = 0; i < w.size(); i++) {
            g.setColor(new Color((int) (250 - (w.get(i).duration / (double) w.get(i).maxDuration) * 250), (int) ((w.get(i).duration / (double) w.get(i).maxDuration) * 250), 0));
            g.fillRect((int) (w.get(i).x + Test.deltaX), (int) (w.get(i).y + Test.deltaY), (int) w.get(i).width, (int) w.get(i).height);
        }
    }

    public void drawStatus(Graphics g) {
        g.setColor(Color.YELLOW);
        g.setFont(new Font("宋体", Font.BOLD, 20));

        //g.drawString("游戏时间:" + Math.floorDiv(Test.ticks, 30) + "", 200 + Test.deltaX, 520 + Test.deltaY);
        //g.drawString("1.通过↑↓←→控制SOUL", -500 + Test.deltaX, Test.deltaY);
        //g.drawString("2.躲避灰色方块", -500 + Test.deltaX, Test.deltaY + 25);
        //g.drawString("3.当生命值为0时，游戏结束", -500 + Test.deltaX, Test.deltaY + 50);
        //g.drawString("4. 蓝色的攻击-> 不要动  橙色攻击->动",-500 + Test.deltaX, Test.deltaY + 75);
        //g.drawString("5. 红色：正常 蓝色：重力 绿色：护盾", -500+Test.deltaX, Test.deltaY+100);
        //g.drawString("6. 遇到黄色的矛，不要格挡。",-500 + Test.deltaX, Test.deltaY + 125);
    }

    public void drawGravityDirection(Graphics g) {
        g.setFont(new Font("宋体", Font.BOLD, 100));
        switch (Test.player.gDirection) {
            case 0:
                g.drawString("↑", -500 + Test.deltaX, 300 + Test.deltaY);
                break;
            case 1:
                g.drawString("→", -500 + Test.deltaX, 300 + Test.deltaY);
                break;
            case 2:
                g.drawString("↓", -500 + Test.deltaX, 300 + Test.deltaY);
                break;
            case 3:
                g.drawString("←", -500 + Test.deltaX, 300 + Test.deltaY);
                break;
        }

    }

    public void drawGameStatus(Graphics g) {
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Determination Sans", Font.BOLD, 40));
        if (Test.paused && !Test.over) {
            g.drawString("Press P to Continue", Test.deltaX + 75, -20 + Test.deltaY);
        } else if (!Test.paused && !Test.over) {
            g.drawString("Press P to Pause", Test.deltaX + 75, -20 + Test.deltaY);
        } else if (Test.over) {
            g.setColor(Color.WHITE);
            g.drawString("Press R to Restart", Test.deltaX + 75, -20 + Test.deltaY);

        }
    }

    public void drawWin(Graphics g) {

        g.setColor(Color.YELLOW);
        g.setFont(new Font("宋体", Font.BOLD, 100));
        g.drawString("你赢了!!!!!!", Test.deltaX - 150, Test.deltaY + 450);

    }

    public void drawCoordinateSystem(Graphics g) {
        g.setColor(Color.GREEN);
        g.drawLine(0 + Test.deltaX, 250 + Test.deltaY, 500 + Test.deltaX, 250 + Test.deltaY);
        g.drawLine(250 + Test.deltaX, 0 + Test.deltaY, 250 + Test.deltaX, 500 + Test.deltaY);
        g.setColor(Color.YELLOW);
        g.drawString("(" + (int) (Test.player.x - 250) + "," + (int) (250 - Test.player.y) + ")", -350 + Test.deltaX, 160 + Test.deltaY);
        for (int i = 0; i < Test.cSystem.functionAttacks.size(); i++) {
            FunctionAttack f = Test.cSystem.functionAttacks.get(i);
            g.drawString(f.equation, -700 + Test.deltaX, 200 + 40 * i + Test.deltaY);

        }
    }

    public void drawFunctionAttacks(Graphics g) {
        for (FunctionAttack a : Test.cSystem.functionAttacks) {
            if (a.active) {
                g.setColor(Color.RED);
                //将每一个平面直角坐标系点都转化为屏幕直角坐标系点
                int[] intxs = new int[50];
                int[] intys = new int[50];
                for (int i = 0; i < 50; i++) {
                    intys[i] = (int) (250 - a.ys[i] + Test.deltaY);
                    intxs[i] = (int) (250 + a.xs[i] + Test.deltaX);
                }
                Graphics2D graphics2d = (Graphics2D) g;
                graphics2d.setStroke(new BasicStroke(3));
                g.drawPolyline(intxs, intys, 50);
            }
        }
    }

    public void drawSpear(Graphics g) {

        for (Spear s : Test.spears) {
            if (s.color.equals("Magenta")) {
                g.setColor(Color.MAGENTA);
            } else if (s.color.equals("Yellow")) {
                g.setColor(Color.YELLOW);
            }
            g.fillRect((int) (s.x + Test.deltaX), (int) (s.y + Test.deltaY), s.width, s.height);
        }

    }

    // 主循环
    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (true) {

            // 防止死循环不执行？？
            Test.frame.setTitle("Undertale");
            if (Test.over && Test.restart) {
                this.gameRestart();
            }

            if (!Test.paused && !Test.over && !Test.win) {

                try {
                    Thread.sleep(1000 / 60);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Test.player.saveLocation();
                this.moveThePlayer();
                this.checkTeleport();
                if (Test.player.soulMode == "Red") {
                    this.holdsPlayerInBounds();
                }
                this.gravity();

                // 更新玩家碰撞箱
                Test.player.updateHitbox();

                this.checkHit();
                // 移动所有的攻击
                this.moveAttacks();


                // 攻击时间表

                this.scheduleAttack();

                //检查击中

                //警告持续时间减少
                this.warningDurationSubtract();
                // 减少骨头时间
                this.boneDurationSubtract();

                //检查骨头是不是该消失了
                this.checkBonesDisappear();
                // Karma减血
                this.karmaHpDecrease();

                Test.ticks += 1;

                //减少函数延迟
                Test.cSystem.delayDecrease();
                //减少无敌时间
                this.drainInvincibility();
                //检测游戏结束
                if (Test.player.hp <= 0) {
                    Test.over = true;

                }


                this.repaint();
            }
        }
    }

    public void drainInvincibility() {
        if (Test.player.invincibleFrames > 0) {
            Test.player.invincibleFrames--;
        }
    }

    public void moveAttacks() {
        // TODO Auto-generated method stub
        for (int i = 0; i < Test.bones.size(); i++) {

            Test.bones.get(i).move();
        }
        for (int i = 0; i < Test.spears.size(); i++) {
            Test.spears.get(i).move();
        }
    }

    public void movePlatforms() {
        for (Platform p : Test.platforms) {
            p.move();
        }
    }

    public void holdsPlayerInBounds() {
        if (Test.player.x < Test.moveBorder[3].x + Test.moveBorder[3].width) {
            Test.player.x = Test.moveBorder[3].x + Test.moveBorder[3].width;
        }
        if (Test.player.x > Test.moveBorder[1].x - Test.player.width) {
            Test.player.x = Test.moveBorder[1].x - Test.player.width;
        }
        if (Test.player.y < Test.moveBorder[0].y + Test.moveBorder[0].height) {
            Test.player.y = Test.moveBorder[0].y + Test.moveBorder[0].height;
        }
        if (Test.player.y > Test.moveBorder[2].y - Test.player.height) {
            Test.player.y = Test.moveBorder[2].y - Test.player.height;
        }
    }

    public void moveThePlayer() {
        if (Test.player.up && !Test.player.hitbox.intersects(Test.moveBorder[0].hitbox)) {

            Test.player.y -= Math.ceil((double) Test.player.speed / 2);

            Test.player.directShield(0);
        }
        if (Test.player.down && !Test.player.hitbox.intersects(Test.moveBorder[2].hitbox)) {

            Test.player.y += Math.ceil((double) Test.player.speed / 2);

            Test.player.directShield(2);
        }
        if (Test.player.left && !Test.player.hitbox.intersects(Test.moveBorder[3].hitbox)) {

            Test.player.x -= Math.ceil((double) Test.player.speed / 2);

            Test.player.directShield(3);
        }
        if (Test.player.right && !Test.player.hitbox.intersects(Test.moveBorder[1].hitbox)) {

            Test.player.x += Math.ceil((double) Test.player.speed / 2);

            Test.player.directShield(1);
        }
    }

    public void movePlatform() {
        for (Platform p : Test.platforms) {
            p.move();
        }
    }

    public void scheduleAttack() {
        if (Test.ticks >= 60 && Test.ticks < 120) {

            this.boneSpike(2);
        } else if (Test.ticks >= 120 && Test.ticks < 300) {
            this.redtify();
        } else if (Test.ticks >= 300 && Test.ticks <= 600) {

            this.boneShaft();
        } else if (Test.ticks > 600 && Test.ticks <= 900) {
            this.healPlayer(16);
        } else if (Test.ticks > 900 && Test.ticks <= 1500) {
            this.crossBones();
        } else if (Test.ticks > 1500 && Test.ticks <= 1800) {
            this.healPlayer(20);

        } else if (Test.ticks > 1800 && Test.ticks <= 2760) {
            this.boneSpike((int) (Math.random() * 4));
        } else if (Test.ticks > 2760 && Test.ticks <= 3060) {
            this.healPlayer(20);
            this.redtify();
        } else if (Test.ticks > 3060 && Test.ticks <= 3690) {
            this.boneRain(1);
            //this.bluetify(2);
        } else if (Test.ticks > 3690 && Test.ticks <= 3900) {
            this.healPlayer(20);
        } else if (Test.ticks > 3900 && Test.ticks <= 4200) {

            int dir = 0;
            if (Test.player.up) {
                dir = 0;
            } else if (Test.player.right) {
                dir = 1;
            } else if (Test.player.down) {
                dir = 2;
            } else if (Test.player.left) {
                dir = 3;
            }
            this.boneRain(dir);
            //this.bluetify(2);
        } else if (Test.ticks > 4200 && Test.ticks <= 4500) {
            this.healPlayer(20);
        } else if (Test.ticks > 4500 && Test.ticks <= 4800) {
            this.boneRain(3);
            //this.bluetify(0);
        } else if (Test.ticks > 4800 && Test.ticks <= 5100) {
            this.healPlayer(20);
        } else if (Test.ticks > 5100 && Test.ticks <= 5400) {
            int dir = 0;
            if (Test.player.up) {
                dir = 2;
            } else if (Test.player.right) {
                dir = 3;
            } else if (Test.player.down) {
                dir = 0;
            } else if (Test.player.left) {
                dir = 1;
            }
            this.boneRain(dir);
            //this.bluetify(0);
        } else if (Test.ticks > 5400 && Test.ticks <= 5700) {
            this.healPlayer(20);
        } else if (Test.ticks > 5700 && Test.ticks <= 6000) {
            this.boneRain(0);
            //this.bluetify(1);
        } else if (Test.ticks > 6000 && Test.ticks <= 6300) {
            this.healPlayer(20);
        } else if (Test.ticks > 6300 && Test.ticks <= 7200) {
            if (Test.ticks % 60 == 0) {
                this.bluetify((int) (Math.random() * 4));
            }
            this.sniperBone(90);
        } else if (Test.ticks > 7200 && Test.ticks <= 7800) {
            this.healPlayer(20);
            this.redtify();
        } else if (Test.ticks > 7800 && Test.ticks <= 9000) {
            if (Math.random() < 0.5) {
                this.foldBones(0);
            } else {
                this.foldBones(1);
            }
        } else if (Test.ticks > 9000 && Test.ticks <= 9600) {
            this.healPlayer(12);
        } else if (Test.ticks > 9600 && Test.ticks <= 10500) {
            this.setTeleportersState(true);
            if (Test.ticks % 240 == 0) {
                this.boneLazer((int) (Math.random() * 4));
            }
        } else if (Test.ticks > 10500 && Test.ticks <= 10800) {
            this.setTeleportersState(false);
            this.healPlayer(7);
        } else if (Test.ticks > 10800 && Test.ticks <= 12000) {
            this.setTeleportersState(true);
            this.laserTrap();

        } else if (Test.ticks > 12000 && Test.ticks <= 12600) {
            Test.warnings.removeAllElements();
            this.setTeleportersState(false);
            this.healPlayer(7);

        } else if (Test.ticks > 12600 && Test.ticks <= 12900) {
            this.gasterBlasters();
            this.sniperBone(90);
        } else if (Test.ticks > 12900 && Test.ticks <= 13200) {
            this.sniperBone((int) (Math.random() * 30 + 10));
        } else if (Test.ticks > 13200 && Test.ticks <= 13500) {
            this.healPlayer(12);
        } else if (Test.ticks > 13500 && Test.ticks <= 14000) {
            this.boneRain(2);
            this.sniperBone(180);

        } else if (Test.ticks > 14000 && Test.ticks <= 14300) {
            this.healPlayer(3);
        } else if (Test.ticks > 14300 && Test.ticks <= 14900) {
            this.foldBones(0);
            this.boneShaft();

        } else if (Test.ticks >= 14900 && Test.ticks <= 15200) {
            this.healPlayer(7);
        } else if (Test.ticks > 15200 && Test.ticks <= 15680) {
            this.boneRain((int) (Math.random() * 4));
            //this.bluetify((int) (Math.random()*  4));
        } else if (Test.ticks > 15680 && Test.ticks <= 16000) {
            this.healPlayer(8);
            this.redtify();
        } else if (Test.ticks > 16000 && Test.ticks <= 19600) {
            this.setCoordinateSystemState(true);
            Test.cSystem.createFunctionAttack();


        } else if (Test.ticks > 19600 && Test.ticks <= 20000) {
            this.setCoordinateSystemState(false);
            Test.cSystem.functionAttacks.removeAllElements();
            this.healPlayer(12);
        } else if (Test.ticks > 20000 && Test.ticks <= 20600) {

            this.augmentedGasterBlasters();
            this.sniperBone(60);
        } else if (Test.ticks >= 20600 && Test.ticks < 21000) {
            Test.player.hp = Test.player.hpMax;
        } else if (Test.ticks > 21000 && Test.ticks <= 21300) {
            this.bluetify(2);
            this.crossBonesHorizontal();
        } else if (Test.ticks > 21300 && Test.ticks <= 22000) {
            this.healPlayer(13);
        } else if (Test.ticks > 22000 && Test.ticks <= 22420) {
            this.bluetify(2);
            this.blueBone();
        } else if (Test.ticks > 22420 && Test.ticks <= 23020) {
            this.healPlayer(16);
        } else if (Test.ticks > 23020 && Test.ticks <= 23620) {
            this.spearAttack();
        } else if (Test.ticks > 23620 && Test.ticks <= 23920) {
            this.healPlayer(12);
        } else if (Test.ticks > 23920 && Test.ticks <= 25000) {
            this.spearAttackTwo();
        } else if (Test.ticks > 25000 && Test.ticks <= 25400) {
            this.healPlayer(20);
        } else if (Test.ticks > 25400 && Test.ticks <= 26000) {
            this.spearAttackThree();
        } else if (Test.ticks >= 26000 && Test.ticks < 26400) {
            this.healPlayer(20);
            this.bluetify(2);
        } else if (Test.ticks >= 26400 && Test.ticks < 27000) {
            this.platformOne();
        }

    }

    public void karmaHpDecrease() {
        // TODO Auto-generated method stub
        if (Test.player.karma > 40) {
            Test.player.karma = 40;
        }
        if (Test.player.karma == 40 && Test.player.hp > 1) {
            Test.player.karma--;
            Test.player.hp--;
        }
        if (Test.player.karma >= 30 && Test.player.karma < 40 && Test.player.hp > 1 && Test.ticks % 4 == 0) {
            Test.player.karma--;
            Test.player.hp -= 1;
        } else if (Test.player.karma >= 20 && Test.player.karma < 30 && Test.ticks % 10 == 0 && Test.player.hp > 1) {
            Test.player.karma--;
            Test.player.hp -= 1;
        } else if (Test.player.karma >= 10 && Test.player.karma < 20 && Test.ticks % 30 == 0 && Test.player.hp > 1) {
            Test.player.karma--;
            Test.player.hp -= 1;
        } else if (Test.player.karma > 0 && Test.player.karma < 10 && Test.ticks % 60 == 0 && Test.player.hp > 1) {
            Test.player.karma--;
            Test.player.hp -= 1;
        }
//		if (Test.player.karma > 0 && Test.player.hp <= 5) {
//			Test.player.karma--;
//		}
    }

    @Override
    public void keyPressed(KeyEvent arg0) {
        if (arg0.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            Test.ticks += 300;
        }
        if (arg0.getKeyCode() == KeyEvent.VK_P) {

            Test.paused = !Test.paused;

        }
        // TODO Auto-generated method stub
        if (arg0.getKeyCode() == KeyEvent.VK_UP && (Test.player.gDirection != 0 || !Test.player.soulMode.equals("Blue"))) {

            Test.player.up = true;

        }
        if (arg0.getKeyCode() == KeyEvent.VK_RIGHT && (Test.player.gDirection != 1 || !Test.player.soulMode.equals("Blue"))) {

            Test.player.right = true;

        }
        if (arg0.getKeyCode() == KeyEvent.VK_DOWN && (Test.player.gDirection != 2 || !Test.player.soulMode.equals("Blue"))) {

            Test.player.down = true;

        }
        if (arg0.getKeyCode() == KeyEvent.VK_LEFT && (Test.player.gDirection != 3 || !Test.player.soulMode.equals("Blue"))) {

            Test.player.left = true;

        }
        if (Test.over && arg0.getKeyCode() == KeyEvent.VK_R) {

            Test.restart = true;
        }
        if (arg0.getKeyCode() == KeyEvent.VK_F8) {
            Test.player.hp = Test.player.hpMax;
        }


    }

    @Override
    public void keyReleased(KeyEvent arg0) {
        // TODO Auto-generated method stub
        if (arg0.getKeyCode() == KeyEvent.VK_UP) {

            Test.player.up = false;

        } else if (arg0.getKeyCode() == KeyEvent.VK_RIGHT) {

            Test.player.right = false;

        } else if (arg0.getKeyCode() == KeyEvent.VK_DOWN) {

            Test.player.down = false;

        } else if (arg0.getKeyCode() == KeyEvent.VK_LEFT) {

            Test.player.left = false;

        }
    }

    public void checkHit() {
        // Check hit
        for (int i = 0; i < Test.bones.size(); i++) {

            if (Test.player.hitbox.intersects(Test.bones.get(i).hitbox) && Test.player.invincibleFrames <= 0) {
                if (Test.bones.get(i).color.equals("White") || (Test.bones.get(i).color.equals("Blue") && Test.player.isMoving()) || (Test.bones.get(i).color.equals("Orange") && !Test.player.isMoving())) {
                    Test.player.hp--;
                    Test.player.invincibleFrames += 2;
                    if (Test.player.karma == 0) {
                        Test.player.karma += 6;
                    } else {
                        Test.player.karma++;
                    }

                }
                if (Test.player.karma > Test.player.hp) {
                    Test.player.karma = Test.player.hp;
                }
            }
        }
        if (Test.cSystem.checkIfPlayerHit()) {

            if (Test.player.invincibleFrames <= 0) {
                Test.player.hp /= 2;
                Test.player.invincibleFrames = 120;
            }

        }
        for (int i = 0; i < Test.spears.size(); i++) {
            //System.out.println(Test.player.hitbox.x+" ,"+Test.player.hitbox.y);
            Spear spear = Test.spears.get(i);
            if (spear.color.equals("Magenta")) {


                if (Test.player.hitbox.intersects(spear.hitbox)) {
                    if (Test.player.invincibleFrames <= 0) {
                        Test.player.hp -= Test.spears.get(i).damage;
                        Test.player.invincibleFrames = 40;
                    }
                    Test.spears.remove(i);
                    continue;
                }

            } else {

            }
        }
        for (int i = 0; i < Test.spears.size(); i++) {
            if (Test.player.shield.activated && Test.player.shield.hitbox.intersects(Test.spears.get(i).hitbox)) {
                if (Test.spears.get(i).color.equals("Magenta")) {
                    Test.spears.remove(i);
                    continue;
                } else {
                    if (Test.player.invincibleFrames <= 0) {
                        Test.player.hp -= Test.spears.get(i).damage;
                        Test.player.invincibleFrames = 20;
                    }
                    Test.spears.remove(i);
                    continue;
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub

    }

    // 锟斤拷锟斤拷

    /**
     * @param interval 执行的间隔，最小为1.
     */
    public void healPlayer(int interval) {

        if (Test.ticks % interval == 0) {
            if (Test.player.hp < Test.player.hpMax && Test.player.hp > 0) {
                Test.player.hp++;
            }
            if (Test.player.karma > 0) {

                Test.player.karma--;
            }
        }

    }

    // 让骨头消失
    public void checkBonesDisappear() {
        for (int i = 0; i < Test.bones.size(); i++) {
            if (Test.bones.get(i).duration <= 0 && Test.bones.get(i).fadeOut) {
                Test.bones.remove(i);
                if (Test.bones.size() == 0) {
                    break;
                }
            } else if (!Test.bones.get(i).fadeOut
                && (Test.bones.get(i).direction == 3 && Test.bones.get(i).x <= Test.bones.get(i).disappearX
                || Test.bones.get(i).direction == 1 && Test.bones.get(i).x >= Test.bones.get(i).disappearX
                || Test.bones.get(i).direction == 2 && Test.bones.get(i).y >= Test.bones.get(i).disappearY
                || Test.bones.get(i).direction == 0
                && Test.bones.get(i).y <= Test.bones.get(i).disappearY)) {
                Test.bones.remove(i);
            }
        }
        for (int i = 0; i < Test.spears.size(); i++) {
            if ((Test.spears.get(i).direction == 3 && Test.spears.get(i).x <= Test.spears.get(i).disappearX
                || Test.spears.get(i).direction == 1 && Test.spears.get(i).x >= Test.spears.get(i).disappearX
                || Test.spears.get(i).direction == 2 && Test.spears.get(i).y >= Test.spears.get(i).disappearY
                || Test.spears.get(i).direction == 0
                && Test.spears.get(i).y <= Test.spears.get(i).disappearY)) {
                Test.spears.remove(i);
            }
        }
    }

    public void boneDurationSubtract() {
        for (int i = 0; i < Test.bones.size(); i++) {
            if (Test.bones.get(i).fadeOut) {
                Test.bones.get(i).duration--;
            }
        }
    }

    public void warningDurationSubtract() {
        for (int i = 0; i < Test.warnings.size(); i++) {
            if (Test.warnings.get(i).duration > 0) {
                Test.warnings.get(i).duration--;
            }
        }
    }

    // 设置传送工作情况
    public void setTeleportersState(boolean value) {
        for (Teleporter t : Test.bounds) {
            t.activated = value;
        }
    }

    public void setCoordinateSystemState(boolean value) {
        Test.cSystem.activated = value;
        if (!value) {
            Test.cSystem.functionAttacks.removeAllElements();
        }
    }

    // 检查玩家传送
    public void checkTeleport() {
        for (Teleporter e : Test.bounds) {
            if (e.hitbox.intersects(Test.player.hitbox) && e.activated) {
                e.transport();
            }
        }
    }

    //重启游戏
    public void gameRestart() {
        this.setTeleportersState(false);
        this.setCoordinateSystemState(false);

        Test.ticks = 0;
        Test.player.hp = Test.player.hpMax;
        Test.player.x = 250;
        Test.player.y = 250;
        Test.player.karma = 0;
        //去骨
        Test.bones.removeAllElements();
        //去矛
        Test.spears.removeAllElements();
        Test.warnings.removeAllElements();
        this.redtify();
        Test.restart = false;
        Test.over = false;

    }

    public void restoreInitialBorder() {
        Test.moveBorder[0].x = 0;
        Test.moveBorder[0].y = 0;
        Test.moveBorder[0].width = 500;
        Test.moveBorder[0].height = 20;
        Test.moveBorder[0].updateHitbox();
        Test.moveBorder[1].x = 480;
        Test.moveBorder[1].y = 0;
        Test.moveBorder[1].width = 20;
        Test.moveBorder[1].height = 500;
        Test.moveBorder[1].updateHitbox();
        Test.moveBorder[2].x = 0;
        Test.moveBorder[2].y = 480;
        Test.moveBorder[2].width = 500;
        Test.moveBorder[2].height = 20;
        Test.moveBorder[2].updateHitbox();
        Test.moveBorder[3].x = 0;
        Test.moveBorder[3].y = 0;
        Test.moveBorder[3].width = 20;
        Test.moveBorder[3].height = 500;
        Test.moveBorder[3].updateHitbox();
        Test.player.updateHitbox();
    }

    //切换成蓝色模式
    public void bluetify(int gDirection) {
        Test.player.soulMode = "Blue";
        Test.player.shield.activated = false;
        Test.player.speed = 9;
        Test.player.gDirection = gDirection;
        restoreInitialBorder();
    }

    //切换成红色模式
    public void redtify() {
        Test.player.soulMode = "Red";
        Test.player.speed = 9;
        restoreInitialBorder();
    }

    //切换成绿色模式
    public void greentify() {
        Test.player.soulMode = "Green";
        Test.player.speed = 0;
        Test.player.shield.activated = true;

        Test.player.x = 225;
        Test.player.y = 225;
        Test.moveBorder[0].x = 205;
        Test.moveBorder[0].y = 205;
        Test.moveBorder[0].width = 65;
        Test.moveBorder[0].updateHitbox();
        Test.moveBorder[1].x = 250;
        Test.moveBorder[1].y = 205;
        Test.moveBorder[1].height = 65;
        Test.moveBorder[1].updateHitbox();
        Test.moveBorder[2].x = 205;
        Test.moveBorder[2].y = 250;
        Test.moveBorder[2].width = 65;
        Test.moveBorder[2].updateHitbox();
        Test.moveBorder[3].x = 205;
        Test.moveBorder[3].y = 205;
        Test.moveBorder[3].height = 65;
        Test.moveBorder[3].updateHitbox();
        Test.player.updateHitbox();
    }

    public void gravity() {
        //如果玩家是蓝色模式
        if (Test.player.soulMode.equals("Blue")) {
            //并且玩家的重力已经让玩家运动到边界，那么就把玩家重力速度设置为0
            if (Test.player.gDirection == 0 && Test.player.hitbox.intersects(Test.moveBorder[0].hitbox)) {
                Test.player.gSpeed = 0;

            } else if (Test.player.gDirection == 2 && Test.player.hitbox.intersects(Test.moveBorder[2].hitbox)) {
                Test.player.gSpeed = 0;

            } else if (Test.player.gDirection == 3 && Test.player.hitbox.intersects(Test.moveBorder[3].hitbox)) {
                Test.player.gSpeed = 0;

            } else if (Test.player.gDirection == 1 && Test.player.hitbox.intersects(Test.moveBorder[1].hitbox)) {
                Test.player.gSpeed = 0;

            }
//			for(Platform p:Test.platforms) {
//				if(Test.player.hitbox.intersects(p.hitbox)) {
//					Test.player.gSpeed=0;
//				}
//			}
            switch (Test.player.gDirection) {
                case 0:
                    Test.player.y -= Test.player.gSpeed / 60;
                    break;
                case 1:
                    Test.player.x += Test.player.gSpeed / 60;
                    break;
                case 2:
                    Test.player.y += Test.player.gSpeed / 60;
                    break;
                case 3:
                    Test.player.x -= Test.player.gSpeed / 60;
                    break;

            }

            Test.player.gSpeed += 2.6;


        }
    }
    // Attacks

    /**
     * 开门杀：上下移动的长骨头
     */
    public void boneShaft() {

        if (Test.ticks % 4 == 0) {
            Bone b1 = new Bone();
            b1.x = 0;
            b1.y = 0;
            b1.width = 30;
            int x = 0;
            if (Test.ticks <= 450 && Test.ticks >= 300) {
                x = (int) ((Test.ticks - 300) * 2 + 50);
            } else if (Test.ticks <= 600 && Test.ticks > 450) {
                x = (int) (-(Test.ticks - 450) * 2 + 350);
            }
            if (Test.ticks <= 14600 && Test.ticks >= 14300) {
                x = (int) ((Test.ticks - 14300) * 0.75 + 100);
            } else if (Test.ticks <= 14900 && Test.ticks > 14600) {
                x = (int) (-(Test.ticks - 14600) * 0.75 + 325);
            }
            b1.height = x;
            b1.disappearX = 700;

            Bone b2 = new Bone();
            b2.x = 0;
            b2.y = x + 65;
            b2.width = 30;
            b2.height = 500 - (x + 100);
            b2.disappearX = 700;

            b1.direction = 1;
            b2.direction = 1;
            b1.speed = 25;
            b2.speed = 25;

            Test.bones.add(b1);
            Test.bones.add(b2);
        }
    }


    /**
     * 锟斤拷锟斤拷模式锟斤拷锟斤拷锟斤拷锟脚癸拷头锟斤拷锟斤拷锟斤拷
     */
    public void crossBones() {

        if (Test.ticks % 40 == 28) {
            Bone b1 = new Bone();
            b1.direction = 0;
            b1.x = 0;
            b1.y = 600;
            b1.width = 250;
            b1.height = 30;
            b1.speed = 14;
            b1.disappearY = 0;
            b1.randomizeColor();
            Test.bones.add(b1);

        } else if (Test.ticks % 40 == 0) {
            Bone b2 = new Bone();
            b2.direction = 2;
            b2.x = 250;
            b2.y = 0;
            b2.width = 250;
            b2.height = 30;
            b2.speed = 14;
            b2.disappearY = 600;
            b2.randomizeColor();
            Test.bones.add(b2);
        }
    }

    public void crossBonesHorizontal() {
        //大骨头
        if (Test.ticks % 40 == 20) {
            Bone b1 = new Bone();
            b1.direction = 3;
            b1.x = 600;
            b1.y = 0;
            b1.width = 15;
            b1.height = 450;
            b1.speed = 9;
            b1.disappearX = -100;

            Test.bones.add(b1);

        } else if (Test.ticks % 40 == 0) {
            Bone b2 = new Bone();
            b2.direction = 1;
            b2.x = -100;
            b2.y = 450;
            b2.width = 15;
            b2.height = 50;
            b2.speed = 9;
            b2.disappearX = 600;

            Test.bones.add(b2);
        }
    }

    /**
     * 锟斤拷锟斤拷模式锟斤拷锟斤拷瞬锟狡骨刺癸拷锟斤拷
     *
     * @param direction 锟斤拷锟斤拷锟斤拷锟街凤拷锟斤拷 0锟斤拷 1锟斤拷 2锟斤拷 3锟斤拷
     */
    public void boneSpike(int direction) {

        if (Test.ticks % 60 == 0) {

            switch (direction) {
                case 0:
                    Bone b = new Bone();
                    b.x = 0;
                    b.y = -330;
                    b.width = 600;
                    b.height = 150;
                    b.direction = 2;
                    b.speed = 10;
                    b.disappearY = 0;

                    Test.bones.add(b);
                    this.bluetify(0);
                    //			Test.player.y=20;
                    Test.player.gSpeed += 1000.0;
                    break;
                case 1:
                    Bone b1 = new Bone();
                    b1.x = 680;
                    b1.y = 0;
                    b1.width = 150;
                    b1.height = 600;
                    b1.direction = 3;
                    b1.speed = 10;
                    b1.disappearX = 350;

                    Test.bones.add(b1);
                    //	Test.player.x = 480-Test.player.width;
                    this.bluetify(1);
                    Test.player.gSpeed += 1000.0;
                    break;
                case 2:
                    Bone b2 = new Bone();
                    b2.x = 0;
                    b2.y = 680;
                    b2.width = 600;
                    b2.height = 150;
                    b2.direction = 0;
                    b2.speed = 10;
                    b2.disappearY = 350;

                    Test.bones.add(b2);
                    //	Test.player.y = 480-Test.player.height;
                    this.bluetify(2);
                    Test.player.gSpeed += 1000.0;
                    break;
                case 3:
                    Bone b3 = new Bone();
                    b3.x = -330;
                    b3.y = 0;
                    b3.width = 150;
                    b3.height = 600;
                    b3.direction = 1;
                    b3.speed = 10;
                    b3.disappearX = 0;

                    Test.bones.add(b3);
                    //Test.player.x = 20;
                    this.bluetify(3);
                    Test.player.gSpeed += 1000.0;
                    break;

            }
        }
    }

    /**
     * 锟斤拷锟斤拷模式锟侥ｏ拷散锟揭骨点攻锟斤拷 锟斤拷锟斤拷锟斤拷锟斤拷
     *
     * @param direction 锟斤拷头锟侥凤拷锟斤拷
     */

    public void boneRain(int direction) {

        if (Test.ticks % 10 == 0) {
            Bone b = new Bone();
            switch (direction) {
                case 0:
                    b.y = 550;
                    b.x = (int) (Math.random() * 500);
                    b.width = 50;
                    b.height = 50;
                    b.speed = 11;
                    b.direction = 0;
                    b.disappearY = -100;

                    break;
                case 1:
                    b.y = (int) (Math.random() * 500);
                    b.x = -50;
                    b.width = 50;
                    b.height = 50;
                    b.speed = 11;
                    b.direction = 1;
                    b.disappearX = 650;

                    break;
                case 2:
                    b.y = -50;
                    b.x = (int) (Math.random() * 500);
                    b.width = 50;
                    b.height = 50;
                    b.speed = 11;
                    b.direction = 2;
                    b.disappearY = 650;

                    break;
                case 3:
                    b.y = (int) (Math.random() * 500);
                    b.x = 550;
                    b.width = 50;
                    b.height = 50;
                    b.speed = 11;
                    b.direction = 3;
                    b.disappearX = -100;

                    break;
            }

            Test.bones.add(b);
        }

    }

    /**
     * 锟斤拷锟斤拷模式锟藉：锟斤拷位锟斤拷头锟斤拷锟�
     */
    public void sniperBone(int interval) {

        if (Test.ticks % interval == 0) {

            Bone b1 = new Bone();
            b1.x = Test.player.x;
            b1.y = -300;
            b1.width = 15;
            b1.height = 75;
            b1.direction = 2;
            b1.speed = 60;
            b1.disappearY = 700;

            Bone b2 = new Bone();
            b2.y = Test.player.y;
            b2.x = 800;
            b2.width = 75;
            b2.height = 15;
            b2.direction = 3;
            b2.speed = 60;
            b2.disappearX = -150;
            Bone b3 = new Bone();
            b3.x = Test.player.x;
            b3.y = 800;
            b3.width = 15;
            b3.height = 75;
            b3.direction = 0;
            b3.speed = 60;
            b3.disappearY = -150;
            Bone b4 = new Bone();
            b4.y = Test.player.y;
            b4.x = -300;
            b4.width = 75;
            b4.height = 15;
            b4.direction = 1;
            b4.speed = 60;
            b4.disappearX = 750;
            Test.bones.add(b1);
            Test.bones.add(b2);
            Test.bones.add(b3);
            Test.bones.add(b4);
        }
    }

    /**
     * 锟斤拷锟斤拷模式锟斤拷锟斤拷锟斤拷头双锟津交达拷锟斤拷展锟斤拷锟�(锟斤拷锟斤拷锟斤拷锟斤拷锟�)
     *
     * @param direction 0锟斤拷锟斤拷 1锟斤拷锟斤拷
     */
    public void foldBones(int direction) {
        if (Test.ticks % 55 == 0) {

            if (direction == 0) {
                Bone b1 = new Bone();
                b1.y = -150;
                b1.x = 0;
                int spacePosition = (int) (Math.random() * 200 + 150);
                b1.width = spacePosition;
                b1.height = 25;
                b1.direction = 2;
                b1.disappearY = 700;
                b1.speed = 14;
                Bone b2 = new Bone();
                b2.y = -150;
                b2.x = spacePosition + 75;
                b2.width = (int) (500 - b2.x);
                b2.height = 25;
                b2.direction = 2;
                b2.disappearY = 700;
                b2.speed = 14;
                Bone b3 = new Bone();
                b3.y = 650;
                b3.x = 0;
                b3.width = spacePosition;
                b3.height = 25;
                b3.direction = 0;
                b3.disappearY = -200;
                b3.speed = 14;
                Bone b4 = new Bone();
                b4.y = 650;
                b4.x = spacePosition + 75;
                b4.width = (int) (500 - b2.x);
                b4.height = 25;
                b4.direction = 0;
                b4.disappearY = -200;
                b4.speed = 14;
                Test.bones.add(b1);
                Test.bones.add(b2);
                Test.bones.add(b3);
                Test.bones.add(b4);
            } else {
                Bone b1 = new Bone();
                b1.y = 0;
                b1.x = -150;
                int spacePosition = (int) (Math.random() * 200 + 150);
                b1.width = 25;
                b1.height = spacePosition;
                b1.direction = 1;
                b1.disappearX = 700;
                b1.speed = 14;
                Bone b2 = new Bone();
                b2.y = spacePosition + 75;
                b2.x = -150;
                b2.height = (int) (500 - b2.x);
                b2.width = 25;
                b2.direction = 1;
                b2.disappearX = 700;
                b2.speed = 14;
                Bone b3 = new Bone();
                b3.x = 650;
                b3.y = 0;
                b3.height = spacePosition;
                b3.width = 25;
                b3.direction = 3;
                b3.disappearX = -200;
                b3.speed = 14;
                Bone b4 = new Bone();
                b4.x = 650;
                b4.y = spacePosition + 75;
                b4.height = (int) (500 - b2.x);
                b4.width = 25;
                b4.direction = 3;
                b4.disappearX = -200;
                b4.speed = 14;
                Test.bones.add(b1);
                Test.bones.add(b2);
                Test.bones.add(b3);
                Test.bones.add(b4);
            }
        }
    }

    /**
     * 锟斤拷锟斤拷模式锟竭ｏ拷十锟街硷拷锟斤拷
     *
     * @param direction 锟斤拷锟斤拷锟斤拷锟斤拷亩越恰锟�0锟斤拷锟较ｏ拷1锟斤拷锟铰ｏ拷2锟斤拷锟铰ｏ拷3锟斤拷锟斤拷
     */
    public void boneLazer(int direction) {
        Bone b1 = new Bone();// 锟斤拷锟脚的癸拷头
        Bone b2 = new Bone(); // 锟斤拷锟脚的癸拷头

        switch (direction) {
            case 0:
                // Test.player.x=285;
                // Test.player.y=215;
                b1.x = 0;
                b1.y = 0;
                b1.width = 500;
                b1.height = 40;
                b1.direction = 2;
                b1.speed = 7;
                b1.disappearY = 600;

                b2.x = 500;
                b2.y = 0;
                b2.width = 40;
                b2.height = 500;
                b2.direction = 3;
                b2.speed = 7;
                b2.disappearX = -100;
                break;
            case 1:
                // Test.player.x=285;
                // Test.player.y=285;
                b1.x = 0;
                b1.y = 500;
                b1.width = 500;
                b1.height = 40;
                b1.direction = 0;
                b1.speed = 7;
                b1.disappearY = -100;

                b2.x = 500;
                b2.y = 0;
                b2.width = 40;
                b2.height = 500;
                b2.direction = 3;
                b2.speed = 7;
                b2.disappearX = -100;
                break;
            case 2:
                // Test.player.x=215;
                // Test.player.y=285;
                b1.x = 0;
                b1.y = 500;
                b1.width = 500;
                b1.height = 40;
                b1.direction = 0;
                b1.speed = 7;
                b1.disappearY = -100;

                b2.x = 0;
                b2.y = 0;
                b2.width = 40;
                b2.height = 600;
                b2.direction = 1;
                b2.speed = 7;
                b2.disappearX = 600;
                break;
            case 3:
                // Test.player.x=215;
                // Test.player.y=215;
                b1.x = 0;
                b1.y = 0;
                b1.width = 500;
                b1.height = 40;
                b1.direction = 2;
                b1.speed = 7;
                b1.disappearY = 600;

                b2.x = 0;
                b2.y = 0;
                b2.width = 40;
                b2.height = 500;
                b2.direction = 1;
                b2.speed = 7;
                b2.disappearX = 600;
                break;
        }
        Test.bones.add(b1);
        Test.bones.add(b2);
    }

    /**
     * 锟斤拷锟斤拷模式锟剿ｏ拷锟斤拷头锟斤拷锟斤拷锟斤拷锟�
     */
    public void laserTrap() {
        if (Test.ticks % 55 == 0) {
            for (int i = 1; i <= 2; i++) {
                Warning w1 = new Warning();
                w1.x = (int) (Math.random() * 500);
                w1.y = (int) (Math.random() * 500);
                w1.duration = 60;
                w1.maxDuration = 60;
                w1.width = 30;
                w1.height = 30;
                Test.warnings.add(w1);
            }
        }
        for (int i = 0; i < Test.warnings.size(); i++) {
            Warning warning = Test.warnings.get(i);
            if (warning.duration <= 0) {
                Test.warnings.remove(i);
                Bone b1 = new Bone();
                Bone b = new Bone();// 锟斤拷锟脚碉拷
                b1.x = warning.x;
                b1.y = 0;
                b1.width = 15;
                b1.height = 500;
                b1.maxDuration = 75;
                b1.duration = 75;
                b1.speed = 0;
                b1.fadeOut = true;
                b1.direction = 1;
                Test.bones.add(b1);

                b.x = 0;
                b.y = warning.y;
                b.width = 500;
                b.height = 15;
                b.maxDuration = 75;
                b.duration = 75;
                b.speed = 0;
                b.fadeOut = true;
                b.direction = 0;

                Test.bones.add(b);
            }
        }


    }

    /**
     * GB炮开门杀
     */
    public void gasterBlasters() {
        if (Test.ticks % 120 == 0) {
            Bone b1 = new Bone();
            b1.x = 0;
            b1.y = 0;
            b1.width = 500;
            b1.height = 200;
            b1.fadeOut = true;
            b1.duration = 15;
            b1.maxDuration = 15;
            b1.speed = 0;
            Bone b2 = new Bone();
            b2.x = 0;
            b2.y = 0;
            b2.width = 200;
            b2.height = 500;
            b2.fadeOut = true;
            b2.duration = 15;
            b2.maxDuration = 15;
            b2.speed = 0;
            Bone b3 = new Bone();
            b3.x = 0;
            b3.y = 300;
            b3.width = 500;
            b3.height = 200;
            b3.fadeOut = true;
            b3.duration = 15;
            b3.maxDuration = 15;
            b3.speed = 0;
            Bone b4 = new Bone();
            b4.x = 300;
            b4.y = 0;
            b4.width = 200;
            b4.height = 500;
            b4.fadeOut = true;
            b4.duration = 15;
            b4.maxDuration = 15;
            b4.speed = 0;
            Test.bones.add(b1);
            Test.bones.add(b2);
            Test.bones.add(b3);
            Test.bones.add(b4);
        } else if (Test.ticks % 120 == 60) {
            Bone b1 = new Bone();
            b1.x = 0;
            b1.y = 200;
            b1.width = 500;
            b1.height = 100;
            b1.fadeOut = true;
            b1.duration = 15;
            b1.maxDuration = 15;
            b1.speed = 0;
            Test.bones.add(b1);
        }
    }

    /**
     * 加强GB炮
     */
    public void augmentedGasterBlasters() {
        if (Test.ticks % 30 == 0) {

            Bone b1 = new Bone();

            if ((Test.ticks - 20000) % 120 < 60) {

                b1.x = 60 + ((Test.ticks - 20000) % 60) * 3;
                b1.y = 0;
                b1.width = 50;
                b1.height = 500;
            } else if ((Test.ticks - 20000) % 120 >= 60) {
                b1.x = 0;
                b1.y = 60 + ((Test.ticks - 20000) % 60) * 3;
                b1.width = 500;
                b1.height = 50;
            }

            b1.fadeOut = true;
            b1.duration = 30;
            b1.maxDuration = 30;
            b1.speed = 0;

            Test.bones.add(b1);

        }
    }

    public void blueBone() {


        if (Test.ticks > 22000 && Test.ticks <= 22180) {
            if (Test.ticks % 60 == 30) {
                //长蓝色骨头
                Bone b1 = new Bone();
                b1.color = "Blue";
                b1.x = 600;
                b1.y = 200;
                b1.width = 16;
                b1.height = 300;
                b1.speed = 20;
                b1.direction = 3;
                b1.disappearX = -100;
                Test.bones.add(b1);
            } else if (Test.ticks % 60 == 0) {
                //白色短骨头
                Bone b2 = new Bone();
                b2.color = "White";
                b2.x = 600;
                b2.y = 450;
                b2.width = 16;
                b2.height = 50;
                b2.speed = 20;
                b2.direction = 3;
                b2.disappearX = -100;
                Test.bones.add(b2);
            }
        } else if (Test.ticks > 22240 && Test.ticks <= 22420) {
            if (Test.ticks % 60 == 30) {
                //长蓝色骨头
                Bone b1 = new Bone();
                b1.color = "Blue";
                b1.x = -100;
                b1.y = 200;
                b1.width = 16;
                b1.height = 300;
                b1.speed = 20;
                b1.direction = 1;
                b1.disappearX = 600;
                Test.bones.add(b1);
            } else if (Test.ticks % 60 == 0) {
                //白色短骨头
                Bone b2 = new Bone();
                b2.color = "White";
                b2.x = -100;
                b2.y = 450;
                b2.width = 16;
                b2.height = 50;
                b2.speed = 20;
                b2.direction = 1;
                b2.disappearX = 600;
                Test.bones.add(b2);
            }
        }


    }

    public void spearAttack() {
        if (Test.ticks % 30 == 0) {
            this.greentify();

            Spear spear = new Spear();
            int direction = new Random().nextInt(4);
            float x = 0;
            float y = 0;
            int width = 0;
            int height = 0;
            int speed;

            switch (direction) {
                case 2:
                    x = Test.player.x;
                    y = -100;
                    width = 15;
                    height = 45;
                    spear.disappearY = 600;
                    break;
                case 3:
                    x = 600;
                    y = Test.player.y;
                    width = 45;
                    height = 15;
                    spear.disappearX = -100;
                    break;
                case 0:
                    x = Test.player.x;
                    y = 600;
                    width = 15;
                    height = 45;
                    spear.disappearY = -100;
                    break;
                case 1:
                    x = -100;
                    y = Test.player.y;
                    width = 45;
                    height = 15;
                    spear.disappearX = 600;
                    break;
            }
            speed = (int) (Math.random() * 15) + 1;
            spear.x = x;
            spear.y = y;
            spear.width = width;
            spear.height = height;
            spear.direction = direction;
            spear.speed = speed;
            Test.spears.add(spear);
        }
    }

    public void spearAttackTwo() {
        if (Test.ticks % 40 == 0) {
            this.greentify();

            Spear spear = new Spear();

            float x = 0;
            float y = 0;
            int width = 0;
            int height = 0;
            int speed;
            int direction = new Random().nextInt(4);
            if (Math.random() > 0.6) {
                spear.color = "Yellow";

            }

            switch (direction) {
                case 2:
                    x = Test.player.x;
                    y = -100;
                    width = 15;
                    height = 45;
                    spear.disappearY = 600;
                    break;
                case 3:
                    x = 600;
                    y = Test.player.y;
                    width = 45;
                    height = 15;
                    spear.disappearX = -100;
                    break;
                case 0:
                    x = Test.player.x;
                    y = 600;
                    width = 15;
                    height = 45;
                    spear.disappearY = -100;
                    break;
                case 1:
                    x = -100;
                    y = Test.player.y;
                    width = 45;
                    height = 15;
                    spear.disappearX = 600;
                    break;
            }
            speed = 10;
            spear.x = x;
            spear.y = y;
            spear.width = width;
            spear.height = height;
            spear.direction = direction;
            spear.speed = speed;

            Test.spears.add(spear);
        }
    }

    public void spearAttackThree() {
        this.greentify();
        if (Test.ticks % 40 == 0) {
            Spear spear = new Spear();
            int direction = GameTools.getOppositeDirection(Test.player.shield.direction);
            float x = 0;
            float y = 0;
            int width = 0;
            int height = 0;
            int speed;

            switch (direction) {
                case 2:
                    x = Test.player.x;
                    y = -100;
                    width = 15;
                    height = 45;
                    spear.disappearY = 600;
                    break;
                case 3:
                    x = 600;
                    y = Test.player.y;
                    width = 45;
                    height = 15;
                    spear.disappearX = -100;
                    break;
                case 0:
                    x = Test.player.x;
                    y = 600;
                    width = 15;
                    height = 45;
                    spear.disappearY = -100;
                    break;
                case 1:
                    x = -100;
                    y = Test.player.y;
                    width = 45;
                    height = 15;
                    spear.disappearX = 600;
                    break;
            }
            speed = 6;
            spear.x = x;
            spear.y = y;
            spear.width = width;
            spear.height = height;
            spear.direction = direction;
            spear.speed = speed;
            spear.color = "Yellow";

            Test.spears.add(spear);
        }
    }

    public void platformOne() {
        Platform platform = new Platform();
        platform.direction = 3;
        platform.x = 600;
        platform.y = 150;
        platform.speed = 6;
        Test.platforms.add(platform);

    }
}


class Entity {
    float x;
    float y;
    int width;
    int height;
    int direction;
    int speed;
    Rectangle hitbox = new Rectangle();

    public void move() {
        switch (this.direction) {
            case 0:
                this.y -= this.speed;
                break;
            case 1:
                this.x += this.speed;
                break;
            case 2:
                this.y += this.speed;
                break;
            case 3:
                this.x -= this.speed;
                break;
        }
        this.updateHitbox();
    }

    public Entity() {

    }

    public Entity(float x2, float y2, int width, int height) {
        this.x = x2;
        this.y = y2;
        this.width = width;
        this.height = height;
        this.hitbox = new Rectangle((int) this.x, (int) this.y, this.width, this.height);
    }

    public void updateHitbox() {
        this.hitbox = new Rectangle((int) this.x, (int) this.y, (int) this.width, (int) this.height);
    }

    public boolean checkHit(Entity e) {
        return this.hitbox.intersects(e.hitbox);
    }

}

class Soul extends Entity {
    int hp = 92;
    int hpMax = 92;
    int karma = 0;

    boolean up;
    boolean down;
    boolean left;
    boolean right;
    String soulMode = "Red";
    int gDirection = 2;
    float gSpeed = 0;
    int speed = 12;

    //无敌时间
    int invincibleFrames = 0;

    //上一帧的位置
    float lastX;
    float lastY;
    //护盾（仅限绿色模式）
    Shield shield = new Shield();

    public void saveLocation() {
        this.lastX = this.x;
        this.lastY = this.y;
    }

    public void directShield(int direction) {
        Test.player.shield.direction = direction;
        switch (direction) {
            case 0:
                if (this.soulMode.equals("Green")) {
                    this.shield.x = this.x - 25;
                    this.shield.y = this.y - 25;
                    this.shield.width = 75;
                    this.shield.height = 10;
                    this.shield.updateHitbox();
                }

                break;
            case 1:
                if (this.soulMode.equals("Green")) {
                    this.shield.x = this.x + 40;
                    this.shield.y = this.y - 25;
                    this.shield.width = 10;
                    this.shield.height = 75;
                    this.shield.updateHitbox();
                }

                break;
            case 2:
                if (this.soulMode.equals("Green")) {
                    this.shield.x = this.x - 25;
                    this.shield.y = this.y + 40;
                    this.shield.width = 75;
                    this.shield.height = 10;
                    this.shield.updateHitbox();
                }

                break;
            case 3:
                if (this.soulMode.equals("Green")) {
                    this.shield.x = this.x - 25;
                    this.shield.y = this.y - 25;
                    this.shield.width = 10;
                    this.shield.height = 75;
                    this.shield.updateHitbox();
                }

                break;
        }
        this.updateHitbox();
    }

    public Soul(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.hitbox = new Rectangle((int) this.x, (int) this.y, (int) this.width, (int) this.height);
    }

    public boolean isMoving() {
        // TODO Auto-generated method stub
        return (lastX != this.x) || (lastY != this.y);
    }

}

class Bone extends Entity {
    int damage;
    int speed = 20;
    // 攻击消失的X坐标
    int disappearX;

    // 攻击消失的Y坐标
    int disappearY;
    // 是否是可持续攻击
    boolean fadeOut = false;
    // 可持续攻击的持续时间
    int duration = 10;
    int maxDuration = 10;
    String color = "White";

    public void move() {
        switch (this.direction) {
            case 0:
                this.y -= Math.ceil((double) this.speed / 2);
                break;
            case 1:
                this.x += Math.ceil((double) this.speed / 2);
                break;
            case 2:
                this.y += Math.ceil((double) this.speed / 2);
                break;
            case 3:
                this.x -= Math.ceil((double) this.speed / 2);
                break;
        }
        this.updateHitbox();
    }

    public void randomizeColor() {
        if (Math.random() > 0.5) {
            this.color = "Blue";
        } else {
            this.color = "Orange";
        }
    }
}

class Teleporter extends Entity {
    boolean activated = false;

    public Teleporter(int x, int y, int width, int height, int direction) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.hitbox = new Rectangle(x, y, width, height);
        this.direction = direction;
    }

    public void transport() {
        // direction： 边界所处的方向
        switch (this.direction) {
            case 0:
                Test.player.y = 425;
                break;
            case 1:
                Test.player.x = 75;
                break;
            case 2:
                Test.player.y = 75;
                break;
            case 3:
                Test.player.x = 425;
                break;

        }
    }
}

class CoordinateSystem {
    boolean activated = false;
    Vector<FunctionAttack> functionAttacks = new Vector<FunctionAttack>();

    public void createFunctionAttack() {
        LinearFunction lFunction = null;
        QuadraticFunction qFunction = null;
        ExponentialFunction eFunction = null;
        TrigFunction tFunction = null;
        double value = Math.random();
        if (Test.ticks % 300 == 0) {
            if (value < 0.25) {
                lFunction = new LinearFunction((float) ((int) (Math.random() * 20 - 10) * 0.5),
                    (int) (Math.random() * 300 - 150));
                this.functionAttacks.add(lFunction);
            } else if (value >= 0.25 && value < 0.5) {
                qFunction = new QuadraticFunction((float) (((int) (Math.random() * 50 - 25)) * 0.001),
                    (int) (Math.random() * 6 - 3), (int) (Math.random() * 300 - 150));

                this.functionAttacks.add(qFunction);
            } else if (value >= 0.5 && value < 0.65) {
                eFunction = new ExponentialFunction((float) (((int) (Math.random() * 40 + 80)) * 0.01));
                this.functionAttacks.add(eFunction);
            } else {
                String type;
                if (Math.random() <= 0.5) {
                    type = "sin";
                } else {
                    type = "cos";
                }
                tFunction = new TrigFunction(type, (float) ((int) (Math.random() * 300 - 150)), (float) (((int) (Math.random() * 50 - 25)) * 0.002), (float) ((int) (Math.random() * 300 - 150)), (float) ((int) (Math.random() * 300 - 150)));
                this.functionAttacks.add(tFunction);
            }
        }
    }

    public boolean checkIfPlayerHit() {
        float playerX = Test.player.x - 250;
        float playerY = 250 - Test.player.y;
        float y = 0f;
        for (int i = 0; i < this.functionAttacks.size(); i++) {


            for (int j = 0; j <= 24; j++) {

                if (this.functionAttacks.get(i) instanceof LinearFunction) {
                    y = ((LinearFunction) (this.functionAttacks.get(i))).calculateY(playerX + j);
                } else if (this.functionAttacks.get(i) instanceof QuadraticFunction) {
                    y = ((QuadraticFunction) (this.functionAttacks.get(i))).calculateY(playerX + j);
                } else if (this.functionAttacks.get(i) instanceof ExponentialFunction) {
                    y = ((ExponentialFunction) (this.functionAttacks.get(i))).calculateY(playerX + j);
                } else if (this.functionAttacks.get(i) instanceof TrigFunction) {
                    y = ((TrigFunction) (this.functionAttacks.get(i))).calculateY(playerX + j);
                }
                //System.out.println(this.functionAttacks.size());

                if ((y <= playerY) && y >= (playerY - 25) && this.functionAttacks.get(i).active) {
                    return true;
                }
            }
        }
        return false;
    }

    public void delayDecrease() {
        for (int i = 0; i < this.functionAttacks.size(); i++) {
            if (this.functionAttacks.get(i).delay > 0) {
                this.functionAttacks.get(i).delay--;
            } else if (this.functionAttacks.get(i).delay == 0) {
                this.functionAttacks.get(i).active = true;
            }
        }
    }
}

class FunctionAttack {
    int delay = 300;
    float[] xs = new float[50];
    float[] ys = new float[50];
    boolean active = false;
    String equation = null;

    public FunctionAttack() {

        for (int i = -250, j = 0; i <= 250 && j < 50; i += 10, j++) {
            this.xs[j] = i;
        }
    }

}

class LinearFunction extends FunctionAttack {
    float k;
    float b;

    public float calculateY(float x) {

        return x * k + b;

    }

    public LinearFunction(float k, float b) {
        super();
        this.k = k;
        this.b = b;
        for (int i = 0; i < this.xs.length; i++) {
            this.ys[i] = this.calculateY(this.xs[i]);
        }
        BigDecimal kBigDecimal = new BigDecimal(this.k);


        this.equation = "f(x)=" + kBigDecimal.round(new MathContext(2)) + "x+(" + (int) this.b + ")";
    }
}

class QuadraticFunction extends FunctionAttack {
    float a;
    float b;
    float c;

    public float calculateY(float x) {

        return a * x * x + b * x + c;

    }

    public QuadraticFunction(float a, float b, float c) {
        super();
        this.a = a;
        this.b = b;
        this.c = c;
        for (int i = 0; i < this.xs.length; i++) {
            this.ys[i] = this.calculateY(this.xs[i]);
        }
        BigDecimal abd = new BigDecimal(this.a);

        BigDecimal bbd = new BigDecimal(this.b);


        this.equation = "f(x)=" + abd.round(new MathContext(1)) + "x^2+(" + bbd.round(new MathContext(1)) + "x)+(" + (int) this.c + ")";
    }
}

class ExponentialFunction extends FunctionAttack {
    float a;


    public float calculateY(float x) {

        return (float) Math.pow(a, x);

    }

    public ExponentialFunction(float a) {
        super();
        this.a = a;

        for (int i = 0; i < this.xs.length; i++) {
            this.ys[i] = this.calculateY(this.xs[i]);
        }
        BigDecimal abd = new BigDecimal(this.a);
        this.equation = "f(x)=" + abd.round(new MathContext(3)) + "^x";
    }
}

class TrigFunction extends FunctionAttack {
    //y=Asin(omega x + phi);
    //"sin" is sine function
    //"cos" is cosine function
    String type;
    float amplitude;
    float frequency;
    float phaseShift;
    float hShift;

    public float calculateY(float x) {
        switch (type) {
            case "sin":
                return (float) (amplitude * Math.sin(frequency * x + phaseShift) + hShift);
            case "cos":
                return (float) (amplitude * Math.cos(frequency * x + phaseShift) + hShift);
            default:
                return 0.0f;
        }
    }

    public TrigFunction(String type, float A, float f, float p, float h) {
        super();
        this.type = type;
        amplitude = A;
        frequency = f;
        phaseShift = p;
        hShift = h;
        for (int i = 0; i < this.xs.length; i++) {
            this.ys[i] = this.calculateY(this.xs[i]);
        }
        BigDecimal abd = new BigDecimal(this.amplitude);
        BigDecimal bDecimal = new BigDecimal(this.frequency);
        BigDecimal cDecimal = new BigDecimal(this.phaseShift);
        BigDecimal dDecimal = new BigDecimal(this.hShift);
        switch (this.type) {
            case "sin":
                this.equation = "f(x)=" + abd.round(new MathContext(3)) + "sin(" + bDecimal.round(new MathContext(3)) + "x+(" + cDecimal.round(new MathContext(3)) + "))+(" + dDecimal.round(new MathContext(3)) + ")";
                break;
            case "cos":
                this.equation = "f(x)=" + abd.round(new MathContext(3)) + "cos(" + bDecimal.round(new MathContext(3)) + "x+(" + cDecimal.round(new MathContext(3)) + "))+(" + dDecimal.round(new MathContext(3)) + ")";
                break;
        }

    }

}

class Bound extends Entity {
    public Bound(int x, int y, int width, int height) {
        super(x, y, width, height);
    }
}

class Spear extends Entity {
    // 攻击消失的X坐标
    int disappearX;

    // 攻击消失的Y坐标
    int disappearY;
    int damage = 11;
    String color = "Magenta";

    public void move() {
        switch (this.direction) {
            case 0:
                this.y -= Math.ceil((double) this.speed / 2);
                break;
            case 1:
                this.x += Math.ceil((double) this.speed / 2);
                break;
            case 2:
                this.y += Math.ceil((double) this.speed / 2);
                break;
            case 3:
                this.x -= Math.ceil((double) this.speed / 2);
                break;
        }
        this.updateHitbox();
    }

    public Spear(float x, float y, int width, int height) {
        super(x, y, width, height);
    }

    public Spear() {
        // TODO Auto-generated constructor stub
    }
}

class Shield extends Entity {
    boolean activated;

}

class Warning extends Entity {
    int duration;
    int maxDuration;
}

class GameTools {
    public static int getOppositeDirection(int direction) {
        if (direction == 0) {
            return 2;
        } else if (direction == 1) {
            return 3;
        } else if (direction == 2) {
            return 0;
        } else if (direction == 3) {
            return 1;
        } else {
            throw new IllegalArgumentException("The direction must be 0 or 1 or 2 or 3!");
        }
    }
}

class Platform extends Entity {
    int disappearX;
    int disappearY;

}