package Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Window extends JFrame implements KeyListener, Runnable /*throws Exception(IO)*/ {
	static int w = 1280;
	static int h = 800;
	private Image SHIP_LEFT;
	private Image SHIP_RIGHT;

	BufferedImage buff;
	Graphics g2;
	BufferedImage buff1;
	Graphics g1;

	ArrayList<Enemy> list;
	Plane ship;
	Laser l1;
	Enemy alien1;

	double brake = 4;
	int laserSpeed = 10;
	double acc = 1;
	boolean restart;
	int recent_restart;
	double tempAcc;
	int restarts;
	int enemies;
	int ePos;


	Random r;
	String paint;
	String key;
	Thread t;
	Thread t2;










	public static void main (String[] args) throws IOException {
		new Window();
		//(new Thread(new Window())).start();
	}

	Window() throws IOException {
		addKeyListener(this);

		buff = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		g2 = buff.getGraphics();
		r = new Random();
		buff1 = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		g1 = buff1.getGraphics();
		SHIP_LEFT = ImageIO.read(new File("Image002Left.png"));
		SHIP_RIGHT = ImageIO.read(new File("Image001.png"));
		recent_restart = 0;



		list = new ArrayList<Enemy>();
		ship = new Plane(50, 50, 60, 40, 0, 0, 0, 0, Color.green, new File("Image001.png"));
		l1 = new Laser(100, 100, 30, 10, 0, 0, 0, 0, null);
		enemies = 5;
		ePos = 0;
		for(int i = ePos; ePos <= enemies; ePos++) {
			Enemy currentEnemy = new Enemy(0 + 60 * ePos, 500, 50, 30, 0, 0);
			list.add(currentEnemy);
		}

		this.setSize(w, h);
		this.setBackground(Color.black);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("Game");


		//t = new Thread();
		//t.start();


		while(true){
			repaint();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized void paint(Graphics g) {

		//SHIP PICTURE

		if(tempAcc < 0) {
			ship.img = SHIP_LEFT; //  ImageIO.read(new File("Image002Left.png"))
		}
		else {
			ship.img = SHIP_RIGHT;
		}

		ship.vel = ship.vel*.99;
		g2.clearRect(0, 0, w, h);

		//LASER
		if(l1.visible == true && l1.shot == true) {
			if(tempAcc >= 0) {
				l1.x = ship.x + ship.w;
				l1.y = ship.y + ship.h/2;
				l1.vx = laserSpeed;
				l1.vel = laserSpeed;
			}
			else {
				l1.vx = -laserSpeed;
				l1.vel = laserSpeed;
				l1.x = ship.x;
				l1.y = ship.y + ship.h/2;
			}
			l1.shot = false;
		}
		if(l1.visible == true) {
			l1.draw(g2);
			l1.move();
			l1.borders();
		}



		//SHIP CONTROL
		ship.draw(g2);
		ship.move();
		ship.borders();

		//ENEMIES
		
		if(Math.abs(recent_restart - (int)  System.currentTimeMillis()) < 3000){
			//keep drawing in same place
			//increase some global variable
			for(int i  = 1; i <= enemies; i++) {
				list.remove(enemies- i);
				Enemy currentEnemy = new Enemy(0 + 60 * i, 500, 50, 30, 0, 0);
				list.add(currentEnemy);
				currentEnemy.draw(g2);
			}
		}
		else{
			for(int i = 0; i < enemies; i++) {
				list.get(i).draw(g2);
				list.get(i).move();
				list.get(i).borders();
				ship.collision(list.get(i));
				if(l1.collision(list.get(i))){
					ship.health+=5;
				}
			}
			recent_restart = 0;
		}

		//WRITING HEALTH
		g2.setColor(Color.green);
		g2.setFont(new Font("Arial", Font.PLAIN, 20));
		if(ship.health  < 0)
			ship.health = 0;

		g2.drawString("" + ship.health, w/2, 40);
		g.drawImage(buff, 0, 0, null);

		//RESTART

		//LOSE
		if(ship.health == 0 && restart == false) {

			g2.clearRect(0, 0, w, h);
			g2.setColor(Color.red);
			g2.setFont(new Font("Arial", Font.PLAIN, 50));
			g2.drawString("YOU LOSE", (w/2) - 125, (h)/2 + 22);
			g.drawImage(buff, 0, 0, null);

		}

		//WIN
		if(ship.health > 0) {
			for(int i = 0; i < enemies; i++) {
				if(list.get(i).visible == true) {
					break;
				}
				else if(i == enemies - 1) {
					g2.clearRect(0, 0, w, h);
					g2.setColor(Color.red);
					g2.setFont(new Font("Arial", Font.PLAIN, 50));
					g2.drawString("YOU WIN", (w/2) - 125, (h)/2 + 22);
					g.drawImage(buff, 0, 0, null);
				}
			}
		}

		//RESTARTING
		if(restart == true) {
			g2.setColor(Color.black);
			g2.fillRect(0, 0, w, h);
			g2.clearRect(0, 0, w, h);
			g.clearRect(0, 0, w, h);
			g.drawImage(buff, 0, 0, null);

			ship.x = 50;
			ship.y = 50;
			ship.vx = 0;
			ship.vy = 0;
			ship.ax = 0;
			ship.ay = 0;
			ship.draw(g2);
			g.drawImage(buff, 0, 0, null);
			restart = false;
			recent_restart = (int) System.currentTimeMillis();
			restarts++;
			
			//adding an enemy
			enemies+=2;
			Enemy currentEnemy = new Enemy(0 + 60 * ePos, 500, 50, 30, 0, 0);
			list.add(currentEnemy);
			ePos++;
			Enemy currentEnemy1 = new Enemy(0 + 60 * ePos, 500, 50, 30, 0, 0);
			list.add(currentEnemy1);
			ePos++;
		}
	}



	@Override
	public synchronized void keyPressed(KeyEvent arg0) {

		if(arg0.getKeyChar() == 'r'){
			/*try {
				t.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			restart = true;
			l1.visible = false;
			l1.shot = false;
			ship.health = 100;
		}

		if(arg0.getKeyChar() == 's' && l1.visible != true) {
			l1.visible = true;
			l1.shot = true;
		}



		if(arg0.getKeyCode() == 37) {
			ship.ax = -acc;
			tempAcc = ship.ax;
		}

		if(arg0.getKeyCode() == 38) {
			ship.ay = -acc;
		}

		if(arg0.getKeyCode() == 39) {
			ship.ax = acc;
			tempAcc = ship.ax;

		}

		if(arg0.getKeyCode() == 40) {
			ship.ay = acc;

		}


		if(arg0.getKeyCode() == 32) {
			ship.ax = 0;
			ship.ay = 0;
			ship.vel = ship.vel/brake;
		}
		ship.move();

		ship.ax = 0;
		ship.ay = 0;
		if(restart == true) {
			//notifyAll();
		}

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}


}
