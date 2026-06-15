package alpha.model;

import java.awt.Rectangle;

public class Entity {
    public float x;
    public float y;
    public int width;
    public int height;
    public int direction;
    public int speed;
    public Rectangle hitbox = new Rectangle();

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
