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
        alpha.AttackScheduler.tick(Test.STATE, this);
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
        alpha.attack.BoneShaftAttack.execute(Test.STATE);
    }


    /**
     * 锟斤拷锟斤拷模式锟斤拷锟斤拷锟斤拷锟脚癸拷头锟斤拷锟斤拷锟斤拷
     */
    public void crossBones() {
        alpha.attack.CrossBonesAttack.execute(Test.STATE);
    }

    public void crossBonesHorizontal() {
        alpha.attack.CrossBonesHorizontalAttack.execute(Test.STATE);
    }

    /**
     * 锟斤拷锟斤拷模式锟斤拷锟斤拷瞬锟狡骨刺癸拷锟斤拷
     *
     * @param direction 锟斤拷锟斤拷锟斤拷锟街凤拷锟斤拷 0锟斤拷 1锟斤拷 2锟斤拷 3锟斤拷
     */
    public void boneSpike(int direction) {
        alpha.attack.BoneSpikeAttack.execute(Test.STATE, direction);
    }

    /**
     * 锟斤拷锟斤拷模式锟侥ｏ拷散锟揭骨点攻锟斤拷 锟斤拷锟斤拷锟斤拷锟斤拷
     *
     * @param direction 锟斤拷头锟侥凤拷锟斤拷
     */

    public void boneRain(int direction) {
        alpha.attack.BoneRainAttack.execute(Test.STATE, direction);
    }

    /**
     * 锟斤拷锟斤拷模式锟藉：锟斤拷位锟斤拷头锟斤拷锟�
     */
    public void sniperBone(int interval) {
        alpha.attack.SniperBoneAttack.execute(Test.STATE, interval);
    }

    /**
     * 锟斤拷锟斤拷模式锟斤拷锟斤拷锟斤拷头双锟津交达拷锟斤拷展锟斤拷锟�(锟斤拷锟斤拷锟斤拷锟斤拷锟�)
     *
     * @param direction 0锟斤拷锟斤拷 1锟斤拷锟斤拷
     */
    public void foldBones(int direction) {
        alpha.attack.FoldBonesAttack.execute(Test.STATE, direction);
    }

    /**
     * 锟斤拷锟斤拷模式锟竭ｏ拷十锟街硷拷锟斤拷
     *
     * @param direction 锟斤拷锟斤拷锟斤拷锟斤拷亩越恰锟�0锟斤拷锟较ｏ拷1锟斤拷锟铰ｏ拷2锟斤拷锟铰ｏ拷3锟斤拷锟斤拷
     */
    public void boneLazer(int direction) {
        alpha.attack.BoneLazerAttack.execute(Test.STATE, direction);
    }

    /**
     * 锟斤拷锟斤拷模式锟剿ｏ拷锟斤拷头锟斤拷锟斤拷锟斤拷锟�
     */
    public void laserTrap() {
        alpha.attack.LaserTrapAttack.execute(Test.STATE);
    }

    /**
     * GB炮开门杀
     */
    public void gasterBlasters() {
        alpha.attack.GasterBlastersAttack.execute(Test.STATE);
    }

    /**
     * 加强GB炮
     */
    public void augmentedGasterBlasters() {
        alpha.attack.AugmentedGasterBlastersAttack.execute(Test.STATE);
    }

    public void blueBone() {
        alpha.attack.BlueBoneAttack.execute(Test.STATE);
    }

    public void spearAttack() {
        alpha.attack.SpearAttack.execute(Test.STATE);
    }

    public void spearAttackTwo() {
        alpha.attack.SpearAttackTwo.execute(Test.STATE);
    }

    public void spearAttackThree() {
        alpha.attack.SpearAttackThree.execute(Test.STATE);
    }

    public void platformOne() {
        alpha.attack.PlatformOneAttack.execute(Test.STATE);
    }
}
