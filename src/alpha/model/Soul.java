package alpha.model;

import java.awt.Rectangle;
import alpha.Test;

public class Soul extends Entity {
    public int hp = 92;
    public int hpMax = 92;
    public int karma = 0;

    public boolean up;
    public boolean down;
    public boolean left;
    public boolean right;
    public String soulMode = "Red";
    public int gDirection = 2;
    public float gSpeed = 0;
    public int speed = 12;

    //无敌时间
    public int invincibleFrames = 0;

    //上一帧的位置
    public float lastX;
    public float lastY;
    //护盾（仅限绿色模式）
    public Shield shield = new Shield();

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
