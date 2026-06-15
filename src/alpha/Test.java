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

import alpha.model.*;
import alpha.model.enums.*;
import alpha.attack.function.*;
import alpha.util.GameTools;

public class Test {
    public static GameState STATE = new GameState();
    public static final int fps = 60;

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        new Test();
    }

    public Test() {
        alpha.render.SpriteLoader.load(STATE);
        STATE.bounds = new Teleporter[4];
        STATE.bounds[0] = new Teleporter(20, 0, 460, 30, 0);
        STATE.bounds[1] = new Teleporter(480, 20, 30, 460, 1);
        STATE.bounds[2] = new Teleporter(20, 480, 460, 30, 2);
        STATE.bounds[3] = new Teleporter(0, 20, 30, 460, 3);
        STATE.moveBorder = new Bound[4];
        STATE.moveBorder[0] = new Bound(0, 0, 500, 20);
        STATE.moveBorder[1] = new Bound(480, 0, 20, 500);
        STATE.moveBorder[2] = new Bound(0, 480, 500, 20);
        STATE.moveBorder[3] = new Bound(0, 0, 20, 500);
        STATE.cSystem = new CoordinateSystem();
        STATE.frame = new JFrame();
        STATE.gp = new GamePanel();
        STATE.player = new Soul(225, 225, 25, 25);
        STATE.bones = new Vector<Bone>();
        STATE.spears = new Vector<Spear>();
        STATE.warnings = new Vector<Warning>();
        STATE.platforms = new Vector<Platform>();
        STATE.frame.add(STATE.gp);

        Toolkit tk = Toolkit.getDefaultToolkit();

        STATE.frame.setSize(tk.getScreenSize().width, tk.getScreenSize().height);
        STATE.frame.setVisible(true);
        STATE.frame.setFocusable(true);

        STATE.frame.setResizable(false);
        STATE.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        if (STATE.frame.isFocused()) {
            STATE.frame.setTitle("UNDERTALE");
        } else {
            STATE.frame.setTitle("UNDERTALE: CONCENTRATE!");
        }
        STATE.frame.addKeyListener(new alpha.input.InputHandler(Test.STATE));
        STATE.t = new Thread(STATE.gp);
        STATE.t.start();

        STATE.deltaX = (Test.STATE.frame.getWidth() - 500) / 2;
        STATE.deltaY = (Test.STATE.frame.getHeight() - 500) / 2;
    }
}

class GamePanel extends JPanel implements Runnable {
    public void paint(Graphics g) {
        alpha.render.Renderer.render(g, Test.STATE, this);
    }














    // 主循环
    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (true) {

            // 防止死循环不执行？？
            Test.STATE.frame.setTitle("Undertale");
            if (Test.STATE.over && Test.STATE.restart) {
                this.gameRestart();
            }

            if (!Test.STATE.paused && !Test.STATE.over && !Test.STATE.win) {

                try {
                    Thread.sleep(1000 / 60);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Test.STATE.player.saveLocation();
                this.moveThePlayer();
                this.checkTeleport();
                if (Test.STATE.player.soulMode == "Red") {
                    this.holdsPlayerInBounds();
                }
                this.gravity();

                // 更新玩家碰撞箱
                Test.STATE.player.updateHitbox();

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

                Test.STATE.ticks += 1;

                //减少函数延迟
                Test.STATE.cSystem.delayDecrease();
                //减少无敌时间
                this.drainInvincibility();
                //检测游戏结束
                if (Test.STATE.player.hp <= 0) {
                    Test.STATE.over = true;

                }


                this.repaint();
            }
        }
    }

    public void drainInvincibility() {
        alpha.system.LifetimeSystem.drainInvincibility(Test.STATE);
    }

    public void moveAttacks() {
        alpha.system.MovementSystem.moveAttacks(Test.STATE);
    }

    public void movePlatforms() {
        alpha.system.MovementSystem.movePlatforms(Test.STATE);
    }

    public void holdsPlayerInBounds() {
        alpha.system.MovementSystem.holdsPlayerInBounds(Test.STATE);
    }

    public void moveThePlayer() {
        alpha.system.MovementSystem.moveThePlayer(Test.STATE);
    }

    public void movePlatform() {
        alpha.system.MovementSystem.movePlatform(Test.STATE);
    }

    public void scheduleAttack() {
        if (Test.STATE.ticks >= 60 && Test.STATE.ticks < 120) {

            this.boneSpike(2);
        } else if (Test.STATE.ticks >= 120 && Test.STATE.ticks < 300) {
            this.redtify();
        } else if (Test.STATE.ticks >= 300 && Test.STATE.ticks <= 600) {

            this.boneShaft();
        } else if (Test.STATE.ticks > 600 && Test.STATE.ticks <= 900) {
            this.healPlayer(16);
        } else if (Test.STATE.ticks > 900 && Test.STATE.ticks <= 1500) {
            this.crossBones();
        } else if (Test.STATE.ticks > 1500 && Test.STATE.ticks <= 1800) {
            this.healPlayer(20);

        } else if (Test.STATE.ticks > 1800 && Test.STATE.ticks <= 2760) {
            this.boneSpike((int) (Math.random() * 4));
        } else if (Test.STATE.ticks > 2760 && Test.STATE.ticks <= 3060) {
            this.healPlayer(20);
            this.redtify();
        } else if (Test.STATE.ticks > 3060 && Test.STATE.ticks <= 3690) {
            this.boneRain(1);
            //this.bluetify(2);
        } else if (Test.STATE.ticks > 3690 && Test.STATE.ticks <= 3900) {
            this.healPlayer(20);
        } else if (Test.STATE.ticks > 3900 && Test.STATE.ticks <= 4200) {

            int dir = 0;
            if (Test.STATE.player.up) {
                dir = 0;
            } else if (Test.STATE.player.right) {
                dir = 1;
            } else if (Test.STATE.player.down) {
                dir = 2;
            } else if (Test.STATE.player.left) {
                dir = 3;
            }
            this.boneRain(dir);
            //this.bluetify(2);
        } else if (Test.STATE.ticks > 4200 && Test.STATE.ticks <= 4500) {
            this.healPlayer(20);
        } else if (Test.STATE.ticks > 4500 && Test.STATE.ticks <= 4800) {
            this.boneRain(3);
            //this.bluetify(0);
        } else if (Test.STATE.ticks > 4800 && Test.STATE.ticks <= 5100) {
            this.healPlayer(20);
        } else if (Test.STATE.ticks > 5100 && Test.STATE.ticks <= 5400) {
            int dir = 0;
            if (Test.STATE.player.up) {
                dir = 2;
            } else if (Test.STATE.player.right) {
                dir = 3;
            } else if (Test.STATE.player.down) {
                dir = 0;
            } else if (Test.STATE.player.left) {
                dir = 1;
            }
            this.boneRain(dir);
            //this.bluetify(0);
        } else if (Test.STATE.ticks > 5400 && Test.STATE.ticks <= 5700) {
            this.healPlayer(20);
        } else if (Test.STATE.ticks > 5700 && Test.STATE.ticks <= 6000) {
            this.boneRain(0);
            //this.bluetify(1);
        } else if (Test.STATE.ticks > 6000 && Test.STATE.ticks <= 6300) {
            this.healPlayer(20);
        } else if (Test.STATE.ticks > 6300 && Test.STATE.ticks <= 7200) {
            if (Test.STATE.ticks % 60 == 0) {
                this.bluetify((int) (Math.random() * 4));
            }
            this.sniperBone(90);
        } else if (Test.STATE.ticks > 7200 && Test.STATE.ticks <= 7800) {
            this.healPlayer(20);
            this.redtify();
        } else if (Test.STATE.ticks > 7800 && Test.STATE.ticks <= 9000) {
            if (Math.random() < 0.5) {
                this.foldBones(0);
            } else {
                this.foldBones(1);
            }
        } else if (Test.STATE.ticks > 9000 && Test.STATE.ticks <= 9600) {
            this.healPlayer(12);
        } else if (Test.STATE.ticks > 9600 && Test.STATE.ticks <= 10500) {
            this.setTeleportersState(true);
            if (Test.STATE.ticks % 240 == 0) {
                this.boneLazer((int) (Math.random() * 4));
            }
        } else if (Test.STATE.ticks > 10500 && Test.STATE.ticks <= 10800) {
            this.setTeleportersState(false);
            this.healPlayer(7);
        } else if (Test.STATE.ticks > 10800 && Test.STATE.ticks <= 12000) {
            this.setTeleportersState(true);
            this.laserTrap();

        } else if (Test.STATE.ticks > 12000 && Test.STATE.ticks <= 12600) {
            Test.STATE.warnings.removeAllElements();
            this.setTeleportersState(false);
            this.healPlayer(7);

        } else if (Test.STATE.ticks > 12600 && Test.STATE.ticks <= 12900) {
            this.gasterBlasters();
            this.sniperBone(90);
        } else if (Test.STATE.ticks > 12900 && Test.STATE.ticks <= 13200) {
            this.sniperBone((int) (Math.random() * 30 + 10));
        } else if (Test.STATE.ticks > 13200 && Test.STATE.ticks <= 13500) {
            this.healPlayer(12);
        } else if (Test.STATE.ticks > 13500 && Test.STATE.ticks <= 14000) {
            this.boneRain(2);
            this.sniperBone(180);

        } else if (Test.STATE.ticks > 14000 && Test.STATE.ticks <= 14300) {
            this.healPlayer(3);
        } else if (Test.STATE.ticks > 14300 && Test.STATE.ticks <= 14900) {
            this.foldBones(0);
            this.boneShaft();

        } else if (Test.STATE.ticks >= 14900 && Test.STATE.ticks <= 15200) {
            this.healPlayer(7);
        } else if (Test.STATE.ticks > 15200 && Test.STATE.ticks <= 15680) {
            this.boneRain((int) (Math.random() * 4));
            //this.bluetify((int) (Math.random()*  4));
        } else if (Test.STATE.ticks > 15680 && Test.STATE.ticks <= 16000) {
            this.healPlayer(8);
            this.redtify();
        } else if (Test.STATE.ticks > 16000 && Test.STATE.ticks <= 19600) {
            this.setCoordinateSystemState(true);
            Test.STATE.cSystem.createFunctionAttack();


        } else if (Test.STATE.ticks > 19600 && Test.STATE.ticks <= 20000) {
            this.setCoordinateSystemState(false);
            Test.STATE.cSystem.functionAttacks.removeAllElements();
            this.healPlayer(12);
        } else if (Test.STATE.ticks > 20000 && Test.STATE.ticks <= 20600) {

            this.augmentedGasterBlasters();
            this.sniperBone(60);
        } else if (Test.STATE.ticks >= 20600 && Test.STATE.ticks < 21000) {
            Test.STATE.player.hp = Test.STATE.player.hpMax;
        } else if (Test.STATE.ticks > 21000 && Test.STATE.ticks <= 21300) {
            this.bluetify(2);
            this.crossBonesHorizontal();
        } else if (Test.STATE.ticks > 21300 && Test.STATE.ticks <= 22000) {
            this.healPlayer(13);
        } else if (Test.STATE.ticks > 22000 && Test.STATE.ticks <= 22420) {
            this.bluetify(2);
            this.blueBone();
        } else if (Test.STATE.ticks > 22420 && Test.STATE.ticks <= 23020) {
            this.healPlayer(16);
        } else if (Test.STATE.ticks > 23020 && Test.STATE.ticks <= 23620) {
            this.spearAttack();
        } else if (Test.STATE.ticks > 23620 && Test.STATE.ticks <= 23920) {
            this.healPlayer(12);
        } else if (Test.STATE.ticks > 23920 && Test.STATE.ticks <= 25000) {
            this.spearAttackTwo();
        } else if (Test.STATE.ticks > 25000 && Test.STATE.ticks <= 25400) {
            this.healPlayer(20);
        } else if (Test.STATE.ticks > 25400 && Test.STATE.ticks <= 26000) {
            this.spearAttackThree();
        } else if (Test.STATE.ticks >= 26000 && Test.STATE.ticks < 26400) {
            this.healPlayer(20);
            this.bluetify(2);
        } else if (Test.STATE.ticks >= 26400 && Test.STATE.ticks < 27000) {
            this.platformOne();
        }

    }

    public void karmaHpDecrease() {
        alpha.system.KarmaSystem.karmaHpDecrease(Test.STATE);
    }



    public void checkHit() {
        alpha.system.CollisionSystem.checkHit(Test.STATE);
    }


    // 锟斤拷锟斤拷

    /**
     * @param interval 执行的间隔，最小为1.
     */
    public void healPlayer(int interval) {
        alpha.system.KarmaSystem.healPlayer(Test.STATE, interval);
    }

    // 让骨头消失
    public void checkBonesDisappear() {
        alpha.system.LifetimeSystem.checkBonesDisappear(Test.STATE);
    }

    public void boneDurationSubtract() {
        alpha.system.LifetimeSystem.boneDurationSubtract(Test.STATE);
    }

    public void warningDurationSubtract() {
        alpha.system.LifetimeSystem.warningDurationSubtract(Test.STATE);
    }

    // 设置传送工作情况
    public void setTeleportersState(boolean value) {
        alpha.system.PhysicsSystem.setTeleportersState(Test.STATE, value);
    }

    public void setCoordinateSystemState(boolean value) {
        alpha.system.PhysicsSystem.setCoordinateSystemState(Test.STATE, value);
    }

    // 检查玩家传送
    public void checkTeleport() {
        alpha.system.PhysicsSystem.checkTeleport(Test.STATE);
    }

    //重启游戏
    public void gameRestart() {
        alpha.system.ModeSystem.gameRestart(Test.STATE);
    }

    public void restoreInitialBorder() {
        alpha.system.ModeSystem.restoreInitialBorder(Test.STATE);
    }

    //切换成蓝色模式
    public void bluetify(int gDirection) {
        alpha.system.ModeSystem.bluetify(Test.STATE, gDirection);
    }

    //切换成红色模式
    public void redtify() {
        alpha.system.ModeSystem.redtify(Test.STATE);
    }

    //切换成绿色模式
    public void greentify() {
        alpha.system.ModeSystem.greentify(Test.STATE);
    }

    public void gravity() {
        alpha.system.PhysicsSystem.gravity(Test.STATE);
    }
    // Attacks

    /**
     * 开门杀：上下移动的长骨头
     */
    public void boneShaft() {

        if (Test.STATE.ticks % 4 == 0) {
            Bone b1 = new Bone();
            b1.x = 0;
            b1.y = 0;
            b1.width = 30;
            int x = 0;
            if (Test.STATE.ticks <= 450 && Test.STATE.ticks >= 300) {
                x = (int) ((Test.STATE.ticks - 300) * 2 + 50);
            } else if (Test.STATE.ticks <= 600 && Test.STATE.ticks > 450) {
                x = (int) (-(Test.STATE.ticks - 450) * 2 + 350);
            }
            if (Test.STATE.ticks <= 14600 && Test.STATE.ticks >= 14300) {
                x = (int) ((Test.STATE.ticks - 14300) * 0.75 + 100);
            } else if (Test.STATE.ticks <= 14900 && Test.STATE.ticks > 14600) {
                x = (int) (-(Test.STATE.ticks - 14600) * 0.75 + 325);
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

            Test.STATE.bones.add(b1);
            Test.STATE.bones.add(b2);
        }
    }


    /**
     * 锟斤拷锟斤拷模式锟斤拷锟斤拷锟斤拷锟脚癸拷头锟斤拷锟斤拷锟斤拷
     */
    public void crossBones() {

        if (Test.STATE.ticks % 40 == 28) {
            Bone b1 = new Bone();
            b1.direction = 0;
            b1.x = 0;
            b1.y = 600;
            b1.width = 250;
            b1.height = 30;
            b1.speed = 14;
            b1.disappearY = 0;
            b1.randomizeColor();
            Test.STATE.bones.add(b1);

        } else if (Test.STATE.ticks % 40 == 0) {
            Bone b2 = new Bone();
            b2.direction = 2;
            b2.x = 250;
            b2.y = 0;
            b2.width = 250;
            b2.height = 30;
            b2.speed = 14;
            b2.disappearY = 600;
            b2.randomizeColor();
            Test.STATE.bones.add(b2);
        }
    }

    public void crossBonesHorizontal() {
        //大骨头
        if (Test.STATE.ticks % 40 == 20) {
            Bone b1 = new Bone();
            b1.direction = 3;
            b1.x = 600;
            b1.y = 0;
            b1.width = 15;
            b1.height = 450;
            b1.speed = 9;
            b1.disappearX = -100;

            Test.STATE.bones.add(b1);

        } else if (Test.STATE.ticks % 40 == 0) {
            Bone b2 = new Bone();
            b2.direction = 1;
            b2.x = -100;
            b2.y = 450;
            b2.width = 15;
            b2.height = 50;
            b2.speed = 9;
            b2.disappearX = 600;

            Test.STATE.bones.add(b2);
        }
    }

    /**
     * 锟斤拷锟斤拷模式锟斤拷锟斤拷瞬锟狡骨刺癸拷锟斤拷
     *
     * @param direction 锟斤拷锟斤拷锟斤拷锟街凤拷锟斤拷 0锟斤拷 1锟斤拷 2锟斤拷 3锟斤拷
     */
    public void boneSpike(int direction) {

        if (Test.STATE.ticks % 60 == 0) {

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

                    Test.STATE.bones.add(b);
                    this.bluetify(0);
                    //			Test.STATE.player.y=20;
                    Test.STATE.player.gSpeed += 1000.0;
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

                    Test.STATE.bones.add(b1);
                    //	Test.STATE.player.x = 480-Test.STATE.player.width;
                    this.bluetify(1);
                    Test.STATE.player.gSpeed += 1000.0;
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

                    Test.STATE.bones.add(b2);
                    //	Test.STATE.player.y = 480-Test.STATE.player.height;
                    this.bluetify(2);
                    Test.STATE.player.gSpeed += 1000.0;
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

                    Test.STATE.bones.add(b3);
                    //Test.STATE.player.x = 20;
                    this.bluetify(3);
                    Test.STATE.player.gSpeed += 1000.0;
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

        if (Test.STATE.ticks % 10 == 0) {
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

            Test.STATE.bones.add(b);
        }

    }

    /**
     * 锟斤拷锟斤拷模式锟藉：锟斤拷位锟斤拷头锟斤拷锟�
     */
    public void sniperBone(int interval) {

        if (Test.STATE.ticks % interval == 0) {

            Bone b1 = new Bone();
            b1.x = Test.STATE.player.x;
            b1.y = -300;
            b1.width = 15;
            b1.height = 75;
            b1.direction = 2;
            b1.speed = 60;
            b1.disappearY = 700;

            Bone b2 = new Bone();
            b2.y = Test.STATE.player.y;
            b2.x = 800;
            b2.width = 75;
            b2.height = 15;
            b2.direction = 3;
            b2.speed = 60;
            b2.disappearX = -150;
            Bone b3 = new Bone();
            b3.x = Test.STATE.player.x;
            b3.y = 800;
            b3.width = 15;
            b3.height = 75;
            b3.direction = 0;
            b3.speed = 60;
            b3.disappearY = -150;
            Bone b4 = new Bone();
            b4.y = Test.STATE.player.y;
            b4.x = -300;
            b4.width = 75;
            b4.height = 15;
            b4.direction = 1;
            b4.speed = 60;
            b4.disappearX = 750;
            Test.STATE.bones.add(b1);
            Test.STATE.bones.add(b2);
            Test.STATE.bones.add(b3);
            Test.STATE.bones.add(b4);
        }
    }

    /**
     * 锟斤拷锟斤拷模式锟斤拷锟斤拷锟斤拷头双锟津交达拷锟斤拷展锟斤拷锟�(锟斤拷锟斤拷锟斤拷锟斤拷锟�)
     *
     * @param direction 0锟斤拷锟斤拷 1锟斤拷锟斤拷
     */
    public void foldBones(int direction) {
        if (Test.STATE.ticks % 55 == 0) {

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
                Test.STATE.bones.add(b1);
                Test.STATE.bones.add(b2);
                Test.STATE.bones.add(b3);
                Test.STATE.bones.add(b4);
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
                Test.STATE.bones.add(b1);
                Test.STATE.bones.add(b2);
                Test.STATE.bones.add(b3);
                Test.STATE.bones.add(b4);
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
                // Test.STATE.player.x=285;
                // Test.STATE.player.y=215;
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
                // Test.STATE.player.x=285;
                // Test.STATE.player.y=285;
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
                // Test.STATE.player.x=215;
                // Test.STATE.player.y=285;
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
                // Test.STATE.player.x=215;
                // Test.STATE.player.y=215;
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
        Test.STATE.bones.add(b1);
        Test.STATE.bones.add(b2);
    }

    /**
     * 锟斤拷锟斤拷模式锟剿ｏ拷锟斤拷头锟斤拷锟斤拷锟斤拷锟�
     */
    public void laserTrap() {
        if (Test.STATE.ticks % 55 == 0) {
            for (int i = 1; i <= 2; i++) {
                Warning w1 = new Warning();
                w1.x = (int) (Math.random() * 500);
                w1.y = (int) (Math.random() * 500);
                w1.duration = 60;
                w1.maxDuration = 60;
                w1.width = 30;
                w1.height = 30;
                Test.STATE.warnings.add(w1);
            }
        }
        for (int i = 0; i < Test.STATE.warnings.size(); i++) {
            Warning warning = Test.STATE.warnings.get(i);
            if (warning.duration <= 0) {
                Test.STATE.warnings.remove(i);
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
                Test.STATE.bones.add(b1);

                b.x = 0;
                b.y = warning.y;
                b.width = 500;
                b.height = 15;
                b.maxDuration = 75;
                b.duration = 75;
                b.speed = 0;
                b.fadeOut = true;
                b.direction = 0;

                Test.STATE.bones.add(b);
            }
        }


    }

    /**
     * GB炮开门杀
     */
    public void gasterBlasters() {
        if (Test.STATE.ticks % 120 == 0) {
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
            Test.STATE.bones.add(b1);
            Test.STATE.bones.add(b2);
            Test.STATE.bones.add(b3);
            Test.STATE.bones.add(b4);
        } else if (Test.STATE.ticks % 120 == 60) {
            Bone b1 = new Bone();
            b1.x = 0;
            b1.y = 200;
            b1.width = 500;
            b1.height = 100;
            b1.fadeOut = true;
            b1.duration = 15;
            b1.maxDuration = 15;
            b1.speed = 0;
            Test.STATE.bones.add(b1);
        }
    }

    /**
     * 加强GB炮
     */
    public void augmentedGasterBlasters() {
        if (Test.STATE.ticks % 30 == 0) {

            Bone b1 = new Bone();

            if ((Test.STATE.ticks - 20000) % 120 < 60) {

                b1.x = 60 + ((Test.STATE.ticks - 20000) % 60) * 3;
                b1.y = 0;
                b1.width = 50;
                b1.height = 500;
            } else if ((Test.STATE.ticks - 20000) % 120 >= 60) {
                b1.x = 0;
                b1.y = 60 + ((Test.STATE.ticks - 20000) % 60) * 3;
                b1.width = 500;
                b1.height = 50;
            }

            b1.fadeOut = true;
            b1.duration = 30;
            b1.maxDuration = 30;
            b1.speed = 0;

            Test.STATE.bones.add(b1);

        }
    }

    public void blueBone() {


        if (Test.STATE.ticks > 22000 && Test.STATE.ticks <= 22180) {
            if (Test.STATE.ticks % 60 == 30) {
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
                Test.STATE.bones.add(b1);
            } else if (Test.STATE.ticks % 60 == 0) {
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
                Test.STATE.bones.add(b2);
            }
        } else if (Test.STATE.ticks > 22240 && Test.STATE.ticks <= 22420) {
            if (Test.STATE.ticks % 60 == 30) {
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
                Test.STATE.bones.add(b1);
            } else if (Test.STATE.ticks % 60 == 0) {
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
                Test.STATE.bones.add(b2);
            }
        }


    }

    public void spearAttack() {
        if (Test.STATE.ticks % 30 == 0) {
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
                    x = Test.STATE.player.x;
                    y = -100;
                    width = 15;
                    height = 45;
                    spear.disappearY = 600;
                    break;
                case 3:
                    x = 600;
                    y = Test.STATE.player.y;
                    width = 45;
                    height = 15;
                    spear.disappearX = -100;
                    break;
                case 0:
                    x = Test.STATE.player.x;
                    y = 600;
                    width = 15;
                    height = 45;
                    spear.disappearY = -100;
                    break;
                case 1:
                    x = -100;
                    y = Test.STATE.player.y;
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
            Test.STATE.spears.add(spear);
        }
    }

    public void spearAttackTwo() {
        if (Test.STATE.ticks % 40 == 0) {
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
                    x = Test.STATE.player.x;
                    y = -100;
                    width = 15;
                    height = 45;
                    spear.disappearY = 600;
                    break;
                case 3:
                    x = 600;
                    y = Test.STATE.player.y;
                    width = 45;
                    height = 15;
                    spear.disappearX = -100;
                    break;
                case 0:
                    x = Test.STATE.player.x;
                    y = 600;
                    width = 15;
                    height = 45;
                    spear.disappearY = -100;
                    break;
                case 1:
                    x = -100;
                    y = Test.STATE.player.y;
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

            Test.STATE.spears.add(spear);
        }
    }

    public void spearAttackThree() {
        this.greentify();
        if (Test.STATE.ticks % 40 == 0) {
            Spear spear = new Spear();
            int direction = GameTools.getOppositeDirection(Test.STATE.player.shield.direction);
            float x = 0;
            float y = 0;
            int width = 0;
            int height = 0;
            int speed;

            switch (direction) {
                case 2:
                    x = Test.STATE.player.x;
                    y = -100;
                    width = 15;
                    height = 45;
                    spear.disappearY = 600;
                    break;
                case 3:
                    x = 600;
                    y = Test.STATE.player.y;
                    width = 45;
                    height = 15;
                    spear.disappearX = -100;
                    break;
                case 0:
                    x = Test.STATE.player.x;
                    y = 600;
                    width = 15;
                    height = 45;
                    spear.disappearY = -100;
                    break;
                case 1:
                    x = -100;
                    y = Test.STATE.player.y;
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

            Test.STATE.spears.add(spear);
        }
    }

    public void platformOne() {
        Platform platform = new Platform();
        platform.direction = 3;
        platform.x = 600;
        platform.y = 150;
        platform.speed = 6;
        Test.STATE.platforms.add(platform);

    }
}
