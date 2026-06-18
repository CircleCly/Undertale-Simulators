package alpha.model;

public class Bone extends Entity {
    public int damage;
    public int speed = 20;
    // 攻击消失的X坐标
    public int disappearX;

    // 攻击消失的Y坐标
    public int disappearY;
    // 是否是可持续攻击
    public boolean fadeOut = false;
    // 可持续攻击的持续时间
    public int duration = 10;
    public int maxDuration = 10;
    public String color = "White";

    @Override
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
