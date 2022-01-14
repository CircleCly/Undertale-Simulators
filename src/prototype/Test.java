package prototype;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Test {
	static JFrame frame;

	static Soul player;
	static GamePanel gp;
	static int ticks = 0;
	static Vector<Bone> bones;
	static int deltaX;
	static int deltaY;
	static boolean paused = true;
	static boolean over = false;
    static boolean win=false;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Test();
	}

	public Test() {
		frame = new JFrame();
		gp = new GamePanel();
		player = new Soul(200, 200, 25, 25);
		bones = new Vector<Bone>();

		frame.add(gp);

		Toolkit tk = Toolkit.getDefaultToolkit();

		frame.setSize(tk.getScreenSize().width, tk.getScreenSize().height);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addKeyListener(gp);
		Thread t = new Thread(gp);
		t.start();

		

		deltaX = (Test.frame.getWidth() - 500) / 2;
		deltaY = (Test.frame.getHeight() - 500) / 2;
	}
}

class GamePanel extends JPanel implements Runnable, KeyListener {
	public void paint(Graphics g) {
		g.fillRect(0, 0, Test.frame.getWidth(), Test.frame.getHeight());
		g.setColor(Color.WHITE);
		g.fillRect(Test.deltaX, Test.deltaY, 20, 500);
		g.fillRect(Test.deltaX, Test.deltaY, 500, 20);
		g.fillRect(486 + Test.deltaX, 20 + Test.deltaY, 15, 480);
		g.fillRect(20 + Test.deltaX, 486 + Test.deltaY, 480, 15);
		this.drawStatus(g);

		if (Test.player.hp > 0) {
			this.drawSoul(g);
			this.drawHP(g);
		}
		this.drawBones(Test.bones, g);
		this.drawGameStatus(g);
		if(Test.win){
			this.drawWin(g);
		}
	}

	public void drawSoul(Graphics g) {

		g.setColor(Color.RED);

		g.fillRect(Test.player.x + Test.deltaX, Test.player.y + Test.deltaY, Test.player.width, Test.player.height);
	}

	public void drawHP(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(Test.deltaX, 530 + Test.deltaY, Test.player.hp * 5, 15);
		g.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		g.drawString("HP" + " " + Test.player.hp + "/99", 100 + Test.deltaX, 520 + Test.deltaY);
		g.setColor(Color.PINK);
		g.fillRect(Test.deltaX + Test.player.hp * 5 - Test.player.karma * 5, 530 + Test.deltaY, Test.player.karma * 5,
				15);

	}

	public void drawBones(Vector<Bone> b, Graphics g) {
		g.setColor(Color.GRAY);
		for (int i = 0; i < b.size(); i++) {
			
			g.fillRect(b.get(i).x + Test.deltaX, b.get(i).y + Test.deltaY, b.get(i).width, b.get(i).height);
		}
	}

	public void drawStatus(Graphics g) {
		g.setColor(Color.YELLOW);
		g.setFont(new Font("Comic Sans MS", Font.BOLD, 20));

		g.drawString("Seconds:" + Math.floorDiv(Test.ticks, 30) + "", 200 + Test.deltaX, 520 + Test.deltaY);
		g.drawString("Instructions: 1. Move the red block with ", -450 + Test.deltaX, Test.deltaY);
		g.drawString("[UP] [DOWN] [LEFT] [RIGHT]", -450 + Test.deltaX, Test.deltaY + 25);
		g.drawString("2.Avoid the gray blocks.", -450 + Test.deltaX, Test.deltaY + 50);
		g.drawString("3.When HP is 0,you lose.", -450 + Test.deltaX, Test.deltaY + 75);
		g.drawString("Current Bones:" + Test.bones.size(), -450 + Test.deltaX, Test.deltaY + 100);
	}

	public void drawGameStatus(Graphics g) {
		g.setColor(Color.YELLOW);
		g.setFont(new Font("Comic Sans MS", Font.BOLD, 40));
		if (Test.paused && !Test.over) {
			g.drawString("PRESS [P] TO CONTINUE", Test.deltaX, -20 + Test.deltaY);
		} else if (!Test.paused && !Test.over) {
			g.drawString("PRESS [P] TO PAUSE", Test.deltaX, -20 + Test.deltaY);
		} else if (Test.over) {
			g.setColor(Color.WHITE);
			g.drawString("GAME OVER.....Stay determined!!", Test.deltaX, -20 + Test.deltaY);

		}
	}
	public void drawWin(Graphics g){
		
			g.setColor(Color.YELLOW);
			g.setFont(new Font("Comic Sans MS",Font.BOLD, 100));
			g.drawString("YOU'VE WON!!!!!!", Test.deltaX-150, Test.deltaY+450);
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {

			// 移动玩家

			if (!Test.paused && !Test.over&&!Test.win) {
				try {
					Thread.sleep(1000 / 30);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (Test.player.up && Test.player.y > 20) {
					Test.player.y -= Test.player.speed;

				} else if (Test.player.down && Test.player.y < 480 - Test.player.height) {
					Test.player.y += Test.player.speed;
				} else if (Test.player.left && Test.player.x > 20) {
					Test.player.x -= Test.player.speed;
				} else if (Test.player.right && Test.player.x < 480 - Test.player.width) {
					Test.player.x += Test.player.speed;
				}
				// 移动骨头
				for (int i = 0; i < Test.bones.size(); i++) {

					Test.bones.get(i).move();
				}
				// 更新玩家的碰撞箱
				Test.player.updateHitbox();
				// 攻击时间表
				if (Test.ticks >= 90 && Test.ticks < 150) {
					this.boneSpike(2);
				}

				else if (Test.ticks >= 150 && Test.ticks <= 300) {

					this.boneShaft();
				} else if (Test.ticks > 300 && Test.ticks <= 450) {
					if (Test.ticks % 10 == 0) {
						if (Test.player.hp < 99 && Test.player.hp > 0) {
							Test.player.hp++;
						}
						if (Test.player.karma > 0) {

							Test.player.karma--;
						}
					}
				} else if (Test.ticks > 450 && Test.ticks <= 750) {
					this.crossBones();
				} else if (Test.ticks > 750 && Test.ticks <= 900) {
					this.healPlayer(5);
				} else if (Test.ticks > 900 && Test.ticks <= 1380) {
					this.boneSpike((int) (Math.random() * 4));
				} else if (Test.ticks > 1380 && Test.ticks <= 1530) {
					this.healPlayer(5);
				} else if (Test.ticks > 1530 && Test.ticks <= 1830) {
					this.boneRain(1);
				} else if (Test.ticks > 1830 && Test.ticks <= 1950) {
					this.healPlayer(5);
				} else if (Test.ticks > 1950 && Test.ticks <= 2100) {
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
				} else if (Test.ticks > 2100 && Test.ticks <= 2250) {
					this.healPlayer(5);
				} else if (Test.ticks > 2250 && Test.ticks <= 2400) {
					this.boneRain(3);
				} else if (Test.ticks > 2400 && Test.ticks <= 2550) {
					if (Test.ticks % 5 == 0) {
						if (Test.player.hp < 99 && Test.player.hp > 0) {
							Test.player.hp++;
						}
						if (Test.player.karma > 0) {

							Test.player.karma--;
						}
					}
				} else if (Test.ticks > 2550 && Test.ticks <= 2700) {
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
				} else if (Test.ticks > 2700 && Test.ticks <= 2850) {
					this.healPlayer(5);
				} else if (Test.ticks > 2850 && Test.ticks <= 3000) {
					this.boneRain(0);
				} else if (Test.ticks > 3000 && Test.ticks <= 3150) {
					this.healPlayer(5);
				} else if (Test.ticks > 3150 && Test.ticks <= 3600) {

					this.sniperBone();
				}else if(Test.ticks>3600&&Test.ticks<=3900){
					this.healPlayer(5);
				}else if(Test.ticks>3900&&Test.ticks<=4500){
					if(Math.random()<0.5){
					this.foldBones(0); 
					}else{
					this.foldBones(1);
					}
				}else if(Test.ticks>4500&&Test.ticks<=4800){
					this.healPlayer(3);
				}else if(Test.ticks==4801){
					Test.win=true;
				}
				// Check hit
				for (int i = 0; i < Test.bones.size(); i++) {
					if (Test.player.hitbox.intersects(Test.bones.get(i).hitbox)) {
						Test.player.hp--;

						Test.player.karma += 9;
						if (Test.player.karma > Test.player.hp) {
							Test.player.karma = Test.player.hp;
						}
					}
				}
				// Check Bones disappear
				this.checkBonesDisappear();
				// Karma减血
				if (Test.player.karma > 40) {
					Test.player.karma = 40;
				}
				if (Test.player.karma >= 30 && Test.player.karma <= 40 && Test.player.hp > 1) {
					Test.player.karma--;
					Test.player.hp--;
				} else if (Test.player.karma >= 20 && Test.player.karma < 30 && Test.ticks % 3 == 0
						&& Test.player.hp > 1) {
					Test.player.karma--;
					Test.player.hp--;
				} else if (Test.player.karma >= 10 && Test.player.karma < 20 && Test.ticks % 5 == 0
						&& Test.player.hp > 1) {
					Test.player.karma--;
					Test.player.hp--;
				} else if (Test.player.karma >= 5 && Test.player.karma < 10 && Test.ticks % 10 == 0
						&& Test.player.hp > 1) {
					Test.player.karma--;
					Test.player.hp--;
				} else if (Test.player.karma > 0 && Test.player.hp > 1) {
					if (Test.ticks % 15 == 0) {
						Test.player.karma--;
						Test.player.hp--;
					}

				}
				if (Test.player.karma > 0 && Test.player.hp == 1) {
					Test.player.karma--;
				}
				Test.ticks++;

				if (Test.player.hp < 0) {
					Test.over = true;

				}
				this.repaint();
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_P) {
			Test.paused=!Test.paused;
			
		}
		// TODO Auto-generated method stub
		if (arg0.getKeyCode() == arg0.VK_UP) {

			Test.player.up = true;

		} else if (arg0.getKeyCode() == arg0.VK_RIGHT) {

			Test.player.right = true;

		} else if (arg0.getKeyCode() == arg0.VK_DOWN) {

			Test.player.down = true;

		} else if (arg0.getKeyCode() == arg0.VK_LEFT) {

			Test.player.left = true;

		} 

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getKeyCode() == arg0.VK_UP) {

			Test.player.up = false;

		} else if (arg0.getKeyCode() == arg0.VK_RIGHT) {

			Test.player.right = false;

		} else if (arg0.getKeyCode() == arg0.VK_DOWN) {

			Test.player.down = false;

		} else if (arg0.getKeyCode() == arg0.VK_LEFT) {

			Test.player.left = false;

		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	// 奖励
	public void healPlayer(int interval) {

		if (Test.ticks % interval == 0) {
			if (Test.player.hp < 99 && Test.player.hp > 0) {
				Test.player.hp++;
			}
			if (Test.player.karma > 0) {

				Test.player.karma--;
			}
		}

	}

	// 检查骨头消失
	public void checkBonesDisappear() {
		for (int i = 0; i < Test.bones.size(); i++) {
			if (Test.bones.get(i).direction == 3 && Test.bones.get(i).x <= Test.bones.get(i).disappearX
					|| Test.bones.get(i).direction == 1 && Test.bones.get(i).x >= Test.bones.get(i).disappearX
					|| Test.bones.get(i).direction == 2 && Test.bones.get(i).y >= Test.bones.get(i).disappearY
					|| Test.bones.get(i).direction == 0 && Test.bones.get(i).y <= Test.bones.get(i).disappearY) {
				Test.bones.remove(i);
			}
		}
	}

	// Attacks
	/**
	 * 攻击模式一： 竖排骨头带空攻击
	 * 
	 */
	public void boneShaft() {

		if (Test.ticks % 5 == 0) {
			Bone b1 = new Bone();
			b1.x = 0;
			b1.y = 0;
			b1.width = 30;
			int x = (int) (Math.random() * 75 + 200);
			b1.height = x;
			b1.disappearX = 700;

			Bone b2 = new Bone();
			b2.x = 0;
			b2.y = x + 100;
			b2.width = 30;
			b2.height = 500 - (x + 100);
			b2.disappearX = 700;

			b1.direction = 1;
			b2.direction = 1;
			b1.speed = 15;
			b2.speed = 15;

			Test.bones.add(b1);
			Test.bones.add(b2);
		}
	}

	/**
	 * 攻击模式二：横排骨头交错攻击
	 */
	public void crossBones() {

		if (Test.ticks % 30 == 10) {
			Bone b1 = new Bone();
			b1.direction = 0;
			b1.x = 0;
			b1.y = 600;
			b1.width = 250;
			b1.height = 30;
			b1.speed = 15;
			b1.disappearY = 0;

			Test.bones.add(b1);

		} else if (Test.ticks % 30 == 0) {
			Bone b2 = new Bone();
			b2.direction = 2;
			b2.x = 250;
			b2.y = 0;
			b2.width = 250;
			b2.height = 30;
			b2.speed = 15;
			b2.disappearY = 600;

			Test.bones.add(b2);
		}
	}

	/**
	 * 攻击模式三：瞬移骨刺攻击
	 * 
	 * @param direction
	 *            攻击出现方向： 0上 1右 2下 3左
	 */
	public void boneSpike(int direction) {

		if (Test.ticks % 30 == 0) {

			switch (direction) {
			case 0:
				Bone b = new Bone();
				b.x = 0;
				b.y = -250;
				b.width = 600;
				b.height = 150;
				b.direction = 2;
				b.speed = 11;
				b.disappearY = -80;

				Test.bones.add(b);
				Test.player.y = 75;
				break;
			case 1:
				Bone b1 = new Bone();
				b1.x = 600;
				b1.y = 0;
				b1.width = 150;
				b1.height = 600;
				b1.direction = 3;
				b1.speed = 11;
				b1.disappearX = 380;

				Test.bones.add(b1);
				Test.player.x = 425;
				break;
			case 2:
				Bone b2 = new Bone();
				b2.x = 0;
				b2.y = 600;
				b2.width = 600;
				b2.height = 150;
				b2.direction = 0;
				b2.speed = 11;
				b2.disappearY = 380;

				Test.bones.add(b2);
				Test.player.y = 425;
				break;
			case 3:
				Bone b3 = new Bone();
				b3.x = -250;
				b3.y = 0;
				b3.width = 150;
				b3.height = 600;
				b3.direction = 1;
				b3.speed = 11;
				b3.disappearX = -80;

				Test.bones.add(b3);
				Test.player.x = 75;
				break;

			}
		}
	}

	/**
	 * 攻击模式四：散乱骨点攻击 （不定向）
	 * 
	 * @param direction
	 *            骨头的方向
	 */

	public void boneRain(int direction) {

		if (Test.ticks % 5 == 0) {
			Bone b = new Bone();
			switch (direction) {
			case 0:
				b.y = 550;
				b.x = (int) (Math.random() * 500);
				b.width = 50;
				b.height = 50;
				b.speed = 5;
				b.direction = 0;
				b.disappearY = -100;

				break;
			case 1:
				b.y = (int) (Math.random() * 500);
				b.x = -50;
				b.width = 50;
				b.height = 50;
				b.speed = 5;
				b.direction = 1;
				b.disappearX = 650;

				break;
			case 2:
				b.y = -50;
				b.x = (int) (Math.random() * 500);
				b.width = 50;
				b.height = 50;
				b.speed = 5;
				b.direction = 2;
				b.disappearY = 650;

				break;
			case 3:
				b.y = (int) (Math.random() * 500);
				b.x = 550;
				b.width = 50;
				b.height = 50;
				b.speed = 5;
				b.direction = 3;
				b.disappearX = -100;

				break;
			}

			Test.bones.add(b);
		}

	}

	/**
	 * 攻击模式五：定位骨头冲击
	 */
	public void sniperBone() {

		if (Test.ticks % 45 == 0) {

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
	 * 攻击模式六：骨头双向交错带空攻击(方向参数版)
	 * @param direction 0上下 1左右
	 * 
	 */
     public void foldBones(int direction)
     {
    	 if(Test.ticks%60==0){
    	
    	 if(direction==0){
    	 Bone b1=new Bone();
    	 b1.y=-150;
    	 b1.x=0;
    	 int spacePosition=(int)(Math.random()*200+150);
    	 b1.width=spacePosition;
    	 b1.height=25;
    	 b1.direction=2;
    	 b1.disappearY=700;
    	 b1.speed=16;
    	 Bone b2=new Bone();
    	 b2.y=-150;
    	 b2.x=spacePosition+75;
    	 b2.width=500-b2.x;
    	 b2.height=25;
    	 b2.direction=2;
    	 b2.disappearY=700;
    	 b2.speed=16;
    	 Bone b3=new Bone();
    	 b3.y=650;
    	 b3.x=0;
    	 b3.width=spacePosition;
    	 b3.height=25;
    	 b3.direction=0;
    	 b3.disappearY=-200;
    	 b3.speed=16;
    	 Bone b4=new Bone();
    	 b4.y=650;
    	 b4.x=spacePosition+75;
    	 b4.width=500-b2.x;
    	 b4.height=25;
    	 b4.direction=0;
    	 b4.disappearY=-200;
    	 b4.speed=16;
    	 Test.bones.add(b1);
    	 Test.bones.add(b2);
    	 Test.bones.add(b3);
    	 Test.bones.add(b4);
    	 }else{
    		 Bone b1=new Bone();
        	 b1.y=0;
        	 b1.x=-150;
        	 int spacePosition=(int)(Math.random()*200+150);
        	 b1.width=25;
        	 b1.height=spacePosition;
        	 b1.direction=1;
        	 b1.disappearX=700;
        	 b1.speed=16;
        	 Bone b2=new Bone();
        	 b2.y=spacePosition+75;
        	 b2.x=-150;
        	 b2.height=500-b2.x;
        	 b2.width=25;
        	 b2.direction=1;
        	 b2.disappearX=700;
        	 b2.speed=16;
        	 Bone b3=new Bone();
        	 b3.x=650;
        	 b3.y=0;
        	 b3.height=spacePosition;
        	 b3.width=25;
        	 b3.direction=3;
        	 b3.disappearX=-200;
        	 b3.speed=16;
        	 Bone b4=new Bone();
        	 b4.x=650;
        	 b4.y=spacePosition+75;
        	 b4.height=500-b2.x;
        	 b4.width=25;
        	 b4.direction=3;
        	 b4.disappearX=-200;
        	 b4.speed=16;
        	 Test.bones.add(b1);
        	 Test.bones.add(b2);
        	 Test.bones.add(b3);
        	 Test.bones.add(b4);
    	   }
    	 }
     }
}

class Entity {
	int x;
	int y;
	int width;
	int height;
	int direction;

	Rectangle hitbox = new Rectangle();

	public Entity() {

	}

	public Entity(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.hitbox = new Rectangle(this.x, this.y, this.width, this.height);
	}

	public void updateHitbox() {
		this.hitbox = new Rectangle(this.x, this.y, this.width, this.height);
	}

	public boolean checkHit(Entity e) {
		return this.hitbox.intersects(e.hitbox);
	}

}

class Soul extends Entity {
	int hp = 99;
	int karma = 0;

	boolean up;
	boolean down;
	boolean left;
	boolean right;
	
	int speed = 9;

	public Soul(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.hitbox = new Rectangle(this.x, this.y, this.width, this.height);
	}

}

class Bone extends Entity {
	int damage;
	int speed = 20;
	// 骨头消失的界限 x坐标
	int disappearX;

	// 骨头消失的界限：Y坐标
	int disappearY;

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
}