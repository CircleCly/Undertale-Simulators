package coloredtiles;
import javax.swing.*;


import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
public class Game {
    static JFrame gameFrame;
    static GamePanel gp;
    static Frisk frisk;
    static Tile[][] tiles;
    //第一个格子表示横坐标，第二个表示纵坐标
    public static final int rows=10;
    public static final int cols=20;
    public static final int tileLength=60;
    public static final int playerLength=20;
    public static int deltaX;
    public static int deltaY;
    public static String statusMessage="Stay Determined!";
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        new Game();
	}
    public Game() {
    tiles=new Tile[cols][rows];
    	
    	//生成一个谜题
    	
    	gp=new GamePanel();
    	gameFrame=new JFrame("UNDERTALE: The Colored Tile");
    	frisk=new Frisk();
    	Game.generatePuzzle();
    	gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	Toolkit toolkit=Toolkit.getDefaultToolkit();
     
    	gameFrame.setSize((int) toolkit.getScreenSize().getWidth(),(int) toolkit.getScreenSize().getHeight());
    	deltaX=300;
    	deltaY=(gameFrame.getHeight()+rows*tileLength)/2-500;
    	gameFrame.setVisible(true);
    	
    	gameFrame.add(gp);
    	gameFrame.addKeyListener(gp);
    	
    	
    }
    public static void generatePuzzle() {
    	frisk.x=0;
    	frisk.y=5;
    	for(int i=0;i<cols;i++) {
    		for(int j=0;j<rows;j++) {
    			double x=Math.random();
    			Tile tile=new Tile();
    			tile.x=i;
    			tile.y=j;
    			
    			if(x<0.14) {
    				tile.type="Red";
    			}else if(x>=0.14&&x<0.28) {
    				tile.type="Blue";
    			}else if(x>=0.28&&x<0.42) {
    				tile.type="Yellow";
    			}else if(x>=0.42&&x<0.56) {
    				tile.type="Purple";
    			}else if(x>=0.56&&x<0.7) {
    				tile.type="Pink";
    			}else if(x>=0.7&&x<0.84) {
    				tile.type="Orange";
    			}else {
    				tile.type="Green";
    			}
    			if((i==0||i==cols-1)&&j==5) {
    				tile.type="Pink";
    			}
    			tiles[i][j]=tile;
    		}
      	}
    }
}
class GamePanel extends JPanel implements KeyListener,Runnable
{
	public void paint(Graphics g) {
		this.brushSurface(g);
		
		this.drawTiles(g);
		this.drawFrisk(g);
		this.drawStartAndEnd(g);
	}
    public void brushSurface(Graphics g) {
    	g.setColor(Color.BLACK);
    	g.fillRect(0, 0, Game.gameFrame.getWidth(),Game.gameFrame.getHeight());
    }
    public void drawFrisk(Graphics g) {
    	g.setFont(new Font("Consolas", Font.BOLD, 20));
    	if(Game.frisk.flavor.equals("Orange")) {
    	g.setColor(Color.ORANGE);
    	g.setFont(new Font("Consolas", Font.BOLD, 20));
    	g.drawString("Flavor: Orange",100,100);
    	}else if(Game.frisk.flavor.equals("Lemon")) {
    	g.setColor(Color.YELLOW);
    	g.drawString("Flavor: Lemons",100,100);
    	}
    	g.setColor(Color.GRAY);
    	g.fillOval(Game.frisk.x*Game.tileLength+Game.deltaX+20, Game.frisk.y*Game.tileLength+Game.deltaY+20,Game.playerLength,Game.playerLength);
    	g.setColor(Color.WHITE);
    	g.drawString(Game.statusMessage, 100, 150);
    }
    public void drawTiles(Graphics g) {
    	for(Tile[] t:Game.tiles) {
    		for(Tile ti: t) {
    			
    			if(ti.type.equals("Red")) {
    				g.setColor(Color.RED);
    			}else if(ti.type.equals("Yellow")) {
    				g.setColor(Color.YELLOW);
    			}else if(ti.type.equals("Blue")) {
    				g.setColor(Color.BLUE);
    			}else if(ti.type.equals("Purple")) {
    				g.setColor(Color.MAGENTA);
    			}else if(ti.type.equals("Pink")) {
    				g.setColor(Color.PINK);
    			}else if(ti.type.equals("Green")) {
    				g.setColor(Color.GREEN);
    			}else if(ti.type.equals("Orange")) {
    				g.setColor(Color.ORANGE);
    			}
    			g.fillRect(ti.x*Game.tileLength+Game.deltaX, ti.y*Game.tileLength+Game.deltaY, Game.tileLength,Game.tileLength);
    		}
    	}
    }
    public void drawStartAndEnd(Graphics g) {
    	g.setColor(Color.WHITE);
    	g.drawString("Start", Game.deltaX-50, 6*Game.tileLength-20+Game.deltaY);
        g.drawString("End", Game.deltaX+Game.cols*Game.tileLength, 6*Game.tileLength-20+Game.deltaY);
    }
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
    public void functionTiles() {
    	for(Tile[] t:Game.tiles) {
    		for(Tile ti: t) {
    			if(ti.x==Game.frisk.x&&ti.y==Game.frisk.y) {
    			ti.tileFunction();
    			}
    		}
    	}
    }
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode()==KeyEvent.VK_UP) {
			Game.frisk.direction=0;
			Game.frisk.moveUp();
		}else if(e.getKeyCode()==KeyEvent.VK_RIGHT) {
			Game.frisk.direction=1;
			Game.frisk.moveRight();
		}else if(e.getKeyCode()==KeyEvent.VK_DOWN) {
			Game.frisk.direction=2;
			Game.frisk.moveDown();
		}else if(e.getKeyCode()==KeyEvent.VK_LEFT) {
			Game.frisk.direction=3;
			Game.frisk.moveLeft();
		}else if(e.getKeyCode()==KeyEvent.VK_R) {
			Game.generatePuzzle();
		}
		
		//Game.frisk.move();
		
		
		Game.frisk.boundCheck();
		Game.frisk.saveLocation();
		this.repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void run() {
		//游戏的主循环
		
	}
}
class Frisk{
	int x;
	int y;
	int lastX=0;
	int lastY=5;
	int direction;
	String flavor="None";
	public void move() {
		switch(this.direction){
		case 0:
		  this.moveUp();
		  break;
		case 1:
		  this.moveRight();
		  break;
		case 2:
		  this.moveDown();
		  break;
		case 3:
		  this.moveLeft();
		  break;
		}
		
	}
    public void moveUp() {
    	this.y--;
    	this.locationCheck();
    }
    public void moveDown() {
    	this.y++;
    	this.locationCheck();
    }
    public void moveLeft() {
    	this.x--;
    	this.locationCheck();
    }
    public void moveRight() {
    	this.x++;
    	this.locationCheck();
    }
    public void saveLocation() {
    	this.lastX=this.x;
    	this.lastY=this.y;
    }
    public void loadLocation() {
    	this.x=this.lastX;
    	this.y=this.lastY;
    }
    public void boundCheck() {
    	if(Game.frisk.x<0||Game.frisk.x>=Game.cols||Game.frisk.y<0||Game.frisk.y>=Game.rows) {
    		Game.frisk.loadLocation();
    	}
    }
    public void locationCheck() {
    	this.boundCheck();
    	Game.gp.functionTiles();
    	this.saveLocation();
    }
}
class Tile{
	String type="";
	int x;
	int y;
	public void tileFunction() {
		
		 
		 if(this.type.equals("Red")) {
			
			Game.frisk.loadLocation();
			Game.statusMessage="Red tiles are impassible!";
		}else if(this.type.equals("Yellow")) {
			Game.frisk.loadLocation();
			Game.statusMessage="Yellow tiles are electric!";
		}else if(this.type.equals("Blue")) {
			if(Game.frisk.flavor.equals("Orange")) {
				Game.frisk.loadLocation();
				Game.statusMessage="You are bitten by some kind of fish!";
			}
			for(Tile e:this.getAdjacantTiles()) {
				if(e.type.equals("Yellow")) {
					Game.frisk.loadLocation();
					Game.statusMessage="Water is a kind of conductor!";
				}
			}
		}else if(this.type.equals("Orange")) {
			Game.frisk.flavor="Orange";
		}
		else if(this.type.equals("Purple")) {
				Game.frisk.flavor="Lemon";
				Game.frisk.move();
				
				Game.statusMessage="Purple tiles are slippery and citric!";
		}
	}
	public Vector<Tile> getAdjacantTiles() {
		Vector<Tile> adjcTiles=new Vector<Tile>();
			if(this.x+1<=Game.cols-1) {
				adjcTiles.add(Game.tiles[this.x+1][this.y]);
			}
			if(this.x-1>=0) {
				adjcTiles.add(Game.tiles[this.x-1][this.y]);
			}
			if(this.y+1<=Game.rows-1) {
				adjcTiles.add(Game.tiles[this.x][this.y+1]);
			}
			if (this.y-1>=0) {
				adjcTiles.add(Game.tiles[this.x][this.y-1]);
			}
		return adjcTiles;
	}
}