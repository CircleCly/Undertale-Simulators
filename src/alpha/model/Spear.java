package alpha.model;

public class Spear extends Entity {
    // 攻击消失的X坐标
    public int disappearX;

    // 攻击消失的Y坐标
    public int disappearY;
    public int damage = 11;
    public String color = "Magenta";

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

    public Spear(float x, float y, int width, int height) {
        super(x, y, width, height);
    }

    public Spear() {
        // TODO Auto-generated constructor stub
    }
}
