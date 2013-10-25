package Game;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.File;
import java.util.Random;

public class Enemy extends Thing {
	static Random rand;
	static double limit = 2;
	
	Enemy(double x, double y, double w, double h,
			double ax, double ay) {
		super(x, y, w, h, 0, 0, ax, ay, null, new File("alien.png"), true);
		rand = new Random();
		this.vx = limit - 2*limit*rand.nextDouble();
		this.vy = limit - 2*limit*rand.nextDouble();
		
	}
	
	void update(Graphics g, Laser other){
		this.draw(g);
		this.move();
		this.borders();
		this.collision(other);
	}
	
	void move() {
		
		x = x + vx;
		y = y + vy;
		r = new Rectangle((int)x,(int) y,(int) w,(int) h);
		
		
		if((Math.abs(this.vx) - 1) <= limit) {
			this.vx+=(.2) * (.5 - rand.nextDouble()); 
		}
		else if(this.vx  > 0) {
			this.vx+=2*(-rand.nextDouble());
		}
		else {
			this.vx+=2*rand.nextDouble();
		}
		if((Math.abs(this.vy) - 1) <= limit) {
			this.vy+=(.2) * (.5 - rand.nextDouble()); 
		}
		else if(this.vy > 0) {
			this.vy+=2*(-rand.nextDouble());
		}
		else {
			this.vy+=2*rand.nextDouble();
		}

	}

	public boolean collison(Laser other) {
		if(this.r.intersects(other.r)) {
			this.visible = false;
			other.visible = false;
			return true;
		}
		else {
			return false;
		}
	}


}
